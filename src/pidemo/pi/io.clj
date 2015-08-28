(ns pidemo.pi.io
  (:gen-class))

(use 'pidemo.pi.parsers)

(defn make-fn [s] (-> s symbol resolve))

(defn filepath->uri [filepath] (str "file://" filepath))

(defn get-uri [uri]
  (if (.startsWith uri "file:")
    (-> uri (subs 7) slurp)
    (slurp uri)))

(defn load-data [uri]
  (->> uri get-uri (parse uri)))

(defn load-config
  "Load multiple config files, including a default file, in decreasing order of importance"
  [config-urls]
  (->> config-urls
       reverse
       (map load-data)
       (apply merge)))

(defn get-resource-as-stream "Obtains resource stream from a JAR file"
 [filename]
  (->> filename (.getResourceAsStream (clojure.lang.RT/baseLoader))))

(defn get-resource "Obtains resource from a JAR file"
 [filename]
  (->> filename get-resource-as-stream slurp))
