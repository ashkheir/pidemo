(ns pidemo.core
  (:gen-class))

(use 'pidemo.pi.vars
     'pidemo.pi.config
     'pidemo.pi.parsers
     'pidemo.pi.pilifecycle
     'pidemo.httpkitadapter)

(defn -main [& args]
  (let [config (-> args first get-config)]
    (set-urls! (first args) config)
    (case (:channel config)
      "WEB"     (do (println "\nDialogue Channel: Web")
                    (start-app [(:http-port config)]))
      "SMS"     (do (println "\nDialogue Channel: SMS"))
      "CONSOLE" (do (println "\nDialogue Channel: Console")
                    (start-command-line config (second args)))
      "TEST"    (do (println "Dialogue Channel: Test")
                    (test-actions config))
                (do (println "Dialogue Channel: Unknown") nil))))