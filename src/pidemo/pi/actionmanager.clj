(ns pidemo.pi.actionmanager
  (:gen-class))

(use 'pidemo.pi.io
     'pidemo.pi.dialogue
     'pidemo.pi.actions)

(defn exec-action [action-str dialogues]
  (let [action-str2 (-> action-str
                        .toLowerCase
                        (clojure.string/replace #"\s+" "-"))
        action-fn   (->> action-str2
                         (str "pidemo.pi.actions/")
                         make-fn)]
    (println "\nExecuting action:" action-str)
    (action-fn dialogues)))

(defn execute-actions [dialogues actions]
  (if (every? valid-dialogue? dialogues)
    (dorun (map #(exec-action % dialogues) actions))))