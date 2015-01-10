(ns expect4clj
  (:import [expect4j Expect4j Closure]))

(defn match-form [name args forms]
  `(~(symbol (str "expect4j.matches." name ".")) ~@args (proxy [Closure] [] (run ~@forms))))

(defmacro glob-match [glob & forms]
  (match-form 'GlobMatch [glob] forms))

(defmacro eof-match [& forms]
  (match-form 'EofMatch [] forms))

(defmacro regex-match [pattern & forms]
  (match-form 'RegExpMatch [pattern] forms))

(defmacro timeout-match [millis & forms]
  (match-form 'TimeoutMatch [millis] forms))

(defn expect [exp & matches]
  (.expect exp (into-array expect4j.matches.Match matches)))
