(ns pidemo.pi.dialogue
  (:gen-class))

(def sample-dialogue [{"ID" 1437946736344}{"Source-ID" 144}
                      {"Date" "2015-07-26T21:38"} {"Location" "Unspecified"}
                      {"Question" "Incident-Location",
                       "Answer" "It happened near Paris."}])

(defn dialogue->id [dialogue] nil)

(defonce ^:dynamic *dialogue-id-map* (atom {}))

(defn save-dialogue [dialogue]
  (println "\n\nsave-dialogue")
  (swap! *dialogue-id-map* assoc (dialogue->id dialogue) dialogue))

(defn get-saved-dialogue [id]
  (println "\n\nget-saved-dialogue")
  (@*dialogue-id-map* id))

(defn valid-dialogue? [dialogue]
  (println "\n\nvalid-dialogue?:\n"dialogue)
  (if (and (vector? dialogue)
           (< 3 (count dialogue))
           (-> dialogue first (get "ID") number?)
           (-> dialogue second (get "Source-ID") nil? not)
           (-> dialogue last (get "Question") string?))
    true
    (do (println "\nInvalid dialogue")
        false)))