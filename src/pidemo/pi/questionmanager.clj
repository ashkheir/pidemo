(ns pidemo.pi.questionmanager
  (:gen-class))

(use 'pidemo.pi.io
     'pidemo.pi.parsers
     'pidemo.pi.rules)

(defn make-parsing-fn
  ""
  [question-name]
  (let [type-parser (->> question-name .toLowerCase (.split #"\-") last
                         (#(str "pidemo.pi.parsers/parse-" %)) make-fn)
        parser      (if (nil? type-parser) parse-text type-parser)]
    parser))

(defn index-questions
  ""
  [idx m lang]
  (let [question-name (m idx)]
    {question-name
      {:mandatory     (if (true? (m :Mandatory))
                        true
                        false)
       :text          (if (and (not (nil? lang))
                               (contains? m (keyword (str "Text-"  
                                                     (.toUpperCase lang)))))
                        (m (keyword (str "Text-" (.toUpperCase lang))))
                        (m :Text))
       :clarification (if (and (not (nil? lang))
                               (contains? m (keyword (str "Clarification-"  
                                                     (.toUpperCase lang)))))
                        (m (keyword (str "Clarification-" (.toUpperCase lang))))
                        (m :Clarification))
       :parsing-fn       (make-parsing-fn question-name)
       :next-question-rl (get-rule m)}}))

(defn index-qstns-by-name
  ""
  [lang questions]
  (reduce #(conj % (index-questions :Name %2 lang)) {} questions))

(defn check-questions [raw-questions indexed-questions]
  (println "\n\nQuestions (raw):\n" raw-questions)
  (->> indexed-questions
       ;(map #(-> % val :parsing-fn))
       ;(map #(((val %) :parsing-fn) "foo"))
       (println "\n\nQuestions (processed):\n")))

(defn valid-questions? [questions]
  true) ;(map #() questions))

(defn any-mandatory-qstns? [qstns-raw]
  (true? (some #(true? (:Mandatory %)) qstns-raw)))

(defn any-branching-qstns? [qstns-raw] ())

(defn any-template-qstns? [qstns-indexed]
  (not-every? #(zero? (count (re-seq #"\{\{\w+\}\}" ((val %) :text))))
              qstns-indexed))

(defn get-questions [lang raw-questions]
  (let [indexed-questions (index-qstns-by-name lang raw-questions)]
    (check-questions raw-questions indexed-questions)
    (if (valid-questions? indexed-questions)
      indexed-questions
      {})))