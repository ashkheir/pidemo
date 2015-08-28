(ns pidemo.pi.dialoguemanager
  (:gen-class))

(use 'pidemo.pi.io
     'pidemo.pi.codewords
     'pidemo.pi.dialogue
     'pidemo.pi.rules)

(defn get-date-time-str []
  (clj-time.format/unparse (clj-time.format/formatters :date-hour-minute)
                           (clj-time.core/now)))

(defn add-dialogue-metadata [dlg adjectives-url nouns-url]
  (into (vector (array-map "ID" (System/currentTimeMillis))
                (array-map "Source-ID" (get-codeword adjectives-url nouns-url))
                (array-map "Date" (get-date-time-str))
                (array-map "Location" "Unspecified"))
        dlg))

(def goodbye-mesgs {"en" "Thank you. You will receive practical information shortly."
                    "fr" "foo bar"})

(defn question-name->question-text [questions question-name]
  (->> question-name questions :text))

(defn question-name->answer-parsed [questions dialogue question-name]
  ((->> question-name questions :parsing-fn)
   ()))

(defn fill-question-template
  ""
  [questions dialogue question-name]
  (->> question-name
       (question-name->question-text questions)
       #(clojure.string/replace % #"\{\{\w+\}\}" 
         (question-name->answer-parsed questions dialogue
                                       (subs % 2 (- (.length %) 2))))))

(defn req-clarification?
  ""
  [question clarified-previously]
  (let [answer-parsed nil
        mandatory     nil]
    (if (nil? answer-parsed)
      (if (or mandatory (not clarified-previously))
        nil))))

(defn conn-oriented [dialogue current-question-name questions goodbye-mesg
                     question-io-fn answer-io-fn adjectives-url nouns-url]
  ; TODO: move metadata to the beginning of the dialogue
  (let [current-qstn (questions current-question-name)]
    (if (nil? current-qstn)
      (do
        (question-io-fn "\n" goodbye-mesg)
        (add-dialogue-metadata dialogue adjectives-url nouns-url))
      (do
        (->> :text current-qstn (question-io-fn "\n")) ; Ask user the question
        (let [answer-raw    (answer-io-fn)
              answer-parsed ((:parsing-fn current-qstn) answer-raw)]
          (recur (conj dialogue (array-map "Question" current-question-name 
                                           "Answer" answer-raw))
                 (get-next-qstn current-qstn answer-parsed questions)
                 questions goodbye-mesg question-io-fn answer-io-fn adjectives-url nouns-url))))))

(defn connectionless [input questions goodbye-mesg]
  (println "\n\nConnectionless user input:"input)
  (let [current-question-name (-> input last first last)
        answer-raw            (-> input last last last)
        current-qstn          (questions current-question-name)
        answer-parsed         ((:parsing-fn current-qstn) answer-raw)
        next-question (get-next-qstn current-qstn answer-parsed questions)]
    (if (nil? next-question)
      goodbye-mesg
      (conj input {"Question" next-question}
                  ; TODO: Only for the web demo - remove afterwards
                  {"Parsed-Answer" (first ((:parsing-fn current-qstn) 
                                   answer-raw))}))))