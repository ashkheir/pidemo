(ns pidemo.pi.rules
  (:gen-class))

(use 'pidemo.pi.io
     'pidemo.pi.parsers)

(defn rule-dsl->rule-fn [tokens]
  (println "\n\nRULE DSL")
  (loop [rls (partition-by #(or (-> % .toLowerCase (= "if"))
                                (-> % .toLowerCase (= "else"))) tokens)
        ]
    ()))

(defn get-rule [questions-raw]
  (let [rule-str (:Next-Question questions-raw)
        tokens   (tokenize rule-str)]
    (if (and (-> tokens count (> 3)) (-> tokens first .toLowerCase (= "if")))
      (rule-dsl->rule-fn tokens)
      rule-str)))

; TODO: Only for illustrating branching during the demo. Remove afterwards.
(defn sample-rule [answer] "Description")

(defn custom-rule->next-qstn [next-qstn-rl-str answer-parsed]
  ((->> next-qstn-rl-str
        (str "pidemo.pi.rules/")
        make-fn)
   answer-parsed))

(defn get-next-qstn [question answer-parsed questions]
  (let [next-qstn-rl  (:next-question-rl question)]
    (println "\nRules - Parsed answer:" answer-parsed)
    (println "Rules - Next question:" next-qstn-rl "\n")
    (if (nil? answer-parsed)
      ("\nRules: Parsed answer is nil")
      (cond (nil? next-qstn-rl)                nil ; No more questions
            (contains? questions next-qstn-rl) next-qstn-rl ; Question name?
            (fn? next-qstn-rl) (next-qstn-rl answer-parsed) ; DSL rule?
            :else (custom-rule->next-qstn next-qstn-rl answer-parsed)))))