(ns pidemo.pi.actions
  (:gen-class))

(use 'pidemo.pi.vars
     'pidemo.pi.orientdbadapter)

(defn persist-to-db [data config-url database-url]
  (let [server (connect-to-in-memory-db config-url)
        db     (open-db database-url)]
    ;(create-doc)
    (close-db db)
    (insert-in-orientdb server data)
    (shutdown-server server)
    ))

(defn persist-dialogue
  "Check whether to store (partial or complete) dialogue in the DB"
  [& dlgs]
  (->> dlgs
       (map #(persist-to-db %
                            (get-db-url)
                            ;(get-resource-as-stream "orientdb-in-memory-config.xml")
                            "memory:/temp/test"))
       dorun))

(defn request-dialogue-verification
  "Check whether dialogue should be verified"
  [& dlgs]
  nil)

(defn inform-dialogue-user
  "Assemble info for the user"
  [& dlgs]
  nil)

(defn notify-dialogue-listeners
  "Check who should be notified"
  [& dlgs]
  nil)
