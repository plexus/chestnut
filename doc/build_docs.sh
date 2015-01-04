cd doc/ && \
npm install && \
node_modules/.bin/gitbook build && \
rm -rf /tmp/_book && \
mv _book /tmp && \
cd .. && \
git co gh-pages && \
rm -rf * && \
cp -r /tmp/_book/* . && \
git add -A && \
git commit -m 'update docs' && \
git push
