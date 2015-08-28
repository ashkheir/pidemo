(ns pidemo.pi.pilifecycle
  (:gen-class))

(use 'pidemo.pi.io
     'pidemo.pi.config
     'pidemo.pi.parsers
     'pidemo.pi.formatters
     'pidemo.pi.questionmanager
     'pidemo.pi.dialogue
     'pidemo.pi.dialoguemanager
     'pidemo.pi.actionmanager)

(defn load-questions [config]
  (->> :question-urls config load-config (get-questions (:lang config))))

(defn load-questions-web [config]
  (->> config
       load-questions
       (map #(dissoc (val %) :parsing-fn :next-question-rl))
       format-json))

(defn dialogue-web [config raw-input]
  (println "\n\ndialogue-web JSON input:"raw-input)
  (println "(-> raw-input str->natural-number integer?):"(-> raw-input str->natural-number integer?))
  (let [questions  (load-questions config)
        adjectives (:cw-adjectives config)
        nouns      (:cw-nouns config)
        num        (str->natural-number raw-input)
        input      (cond (and (integer? num) (> num 1000000))
                           (get-saved-dialogue num)
                         (contains? questions raw-input)
                           nil
                           ;(-> [] #(add-dialogue-metadata % adjectives nouns)
                           ;    (conj (array-map "Question" raw-input))
                           ;    )
                         :else (parse-json raw-input))
        dialogue   (connectionless input questions
                     (get goodbye-mesgs (:lang config) "Thanks"))]
    (if false ; Dialogue is finished
      (execute-actions [dialogue] (config->actions config))
      (format-json dialogue))))

(defn dialogue-sms [question questions goodbye-mesg phone-number]
  ;(let [dialogue      (phone-number->dialogue phone-number)
  ;      new-dialogue  (connectionless next-question questions goodbye-mesg)
  ;      next-question ()]
  ;  (persist-dialogue dialogue)
  ;  next-question
  ;)
)

(defn test-actions [config]
  (println "\nDialogue:\n"sample-dialogue)
  (execute-actions [sample-dialogue] (config->actions config)))

(defn start-command-line
  "Command line interface"
  [cfg dialogue-ref]
  (let [prev-dialogue (if (true? nil) nil [])
        next-question dialogue-ref
        questions     (load-questions cfg)
        goodbye-mesg  (get goodbye-mesgs (:lang cfg) "Thanks")
        actions       (config->actions cfg)
        new-dialogue  (conn-oriented prev-dialogue
                      next-question questions goodbye-mesg println read
                      (:cw-adjectives cfg) (:cw-nouns cfg))]
    (println "\n\nDialogue:\n"new-dialogue)
    (execute-actions [new-dialogue] actions)))