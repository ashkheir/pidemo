(ns pidemo.pi.config
  (:gen-class))

(use 'pidemo.pi.io)

(defn get-config [config-url]
  (let [c    (-> config-url load-data first)
        base (:URL-Base c)]
    {:lang          (:Language c)
     :channel       (-> c :Channel clojure.string/trim .toUpperCase)
     :http-port     (:HTTP-Port c)
     :question-urls (if (string? (:Question-URLs c))
                      [(str base (:Question-URLs c))]
                      (map #(str base %) (:Question-URLs c)))
     :actions-url   (str base (:Actions-URL c))
     :db-url        (str base (:DB-URL c))
     :cw-adjectives (str base (:Codeword-Adjectives c))
     :cw-nouns      (str base (:Codeword-Nouns c))
     :nlp-tokens    (:OpenNLP-EN-Tokens c)
     :nlp-pos-tggr  (:OpenNLP-EN-POS-Tagger c)
     :nlp-date-fin  (:OpenNLP-EN-Date-Finder c)
     :nlp-loc-fin   (:OpenNLP-EN-Location-Finder c)}))

(defn config->actions [cfg] (-> cfg :actions-url str load-data flatten))