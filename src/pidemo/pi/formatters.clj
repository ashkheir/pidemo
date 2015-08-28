(ns pidemo.pi.formatters
  (:gen-class))

(require ;'clj-yaml.core
         'clj-json.core
         ;'clojure.data.csv
         ;'clj-time.core
         ;'clj-time.coerce
         ;'clj-time.format
         )

(defn format-json [edn] (clj-json.core/generate-string edn))
