(ns chestnut.httpclient
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.string :as str])
  (:import java.lang.System
           [java.nio.file Files OpenOption Paths]
           java.security.cert.CertificateFactory
           java.security.KeyStore
           [javax.net.ssl SSLContext TrustManagerFactory]))

;; Not actually working :(
#_
(defn letsencrypt-ssl-context
  "Java before 8u101 does not recognize Let's Encrypt, so we do a bit of Java
  dancing to load the certificate manually.

  https://stackoverflow.com/a/34111150/1891448"
  []
  (let [key-store      (KeyStore/getInstance (KeyStore/getDefaultType))
        key-store-path (Paths/get (System/getProperty "java.home") (into-array String ["lib" "security" "cacerts"]))
        cert-factory   (CertificateFactory/getInstance "X.509")
        cert-file      (io/input-stream (io/resource "DSTRootCAX3.crt"))
        certificate    (.generateCertificate cert-factory cert-file)
        trust-manager  (TrustManagerFactory/getInstance (TrustManagerFactory/getDefaultAlgorithm))
        ssl-context    (SSLContext/getInstance "TLS")]
    (.load key-store (Files/newInputStream key-store-path (into-array OpenOption ())) (.toCharArray "changeit"))
    (.setCertificateEntry key-store "DSTRootCAX3" certificate)
    (.init trust-manager key-store)
    (.init ssl-context nil (.getTrustManagers trust-manager) nil)
    ssl-context
    #_(SSLContext/setDefault ssl-context)))

;; borrowed from ring
(defn- double-escape [^String x]
  (.replace (.replace x "\\" "\\\\") "$" "\\$"))

(defn percent-encode
  "Percent-encode every character in the given string using either the specified
  encoding, or UTF-8 by default."
  [^String unencoded & [^String encoding]]
  (->> (.getBytes unencoded (or encoding "UTF-8"))
       (map (partial format "%%%02X"))
       (str/join)))

(defn url-encode
  "Returns the url-encoded version of the given string, using either a specified
  encoding or UTF-8 by default."
  [unencoded & [encoding]]
  (str/replace
    unencoded
    #"[^A-Za-z0-9_~.+-]+"
    #(double-escape (percent-encode % encoding))))
;;/borrowed from ring

#_(defn http-post!
  "A very limited wrapper for doing a POST request, submits parameters in the
  URL, rather than the body."
  [url params]
  (let [param-str (str/join "&" (map
                                 (fn [[k v]]
                                   (str (url-encode (name k)) "=" (url-encode v)))
                                 params))
        client    (.. (HttpClients/custom)
                      (setSSLContext (letsencrypt-ssl-context))
                      build)
        post      (HttpPost. (str url "?" param-str))]
    (.execute client post)))

(defn http-post!
  "A very limited wrapper for doing a POST request, submits parameters in the
  URL, rather than the body."
  [url params]
  (let [param-str (str/join "&" (map
                                 (fn [[k v]]
                                   (str (url-encode (name k)) "=" (url-encode v)))
                                 params))
        url (str url "?" param-str)]
    (sh/sh "curl" "-X" "POST" url)))
