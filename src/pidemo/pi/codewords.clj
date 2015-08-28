(ns pidemo.pi.codewords
  (:gen-class))

(use 'pidemo.pi.io)

(defn get-codeword [adjectives-url nouns-url]
  (let [adjective (-> adjectives-url load-data first rand-nth)
        noun      (-> nouns-url load-data first rand-nth)]
    ; If the codeword will be used as an ID, check that it hasn't been used before
    ;(if ()
      (str adjective " " noun)
    ;)
    ))