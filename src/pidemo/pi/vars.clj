(ns pidemo.pi.vars
  (:gen-class))

(defonce ^:dynamic *config-url* (atom ""))
(defn get-cfg-url [] @*config-url*)

(defonce ^:dynamic *nlp-tokens-path*   (atom ""))
(defn get-nlp-tokens-path []   @*nlp-tokens-path*)
(defonce ^:dynamic *nlp-pos-tggr-path* (atom ""))
(defn get-nlp-pos-tggr-path [] @*nlp-pos-tggr-path*)
(defonce ^:dynamic *nlp-date-fin-path* (atom ""))
(defn get-nlp-date-fin-path [] @*nlp-date-fin-path*)
(defonce ^:dynamic *nlp-loc-fin-path*  (atom ""))
(defn get-nlp-loc-fin-path []  @*nlp-loc-fin-path*)

(defonce ^:dynamic *db-url* (atom ""))
(defn get-db-url [] @*db-url*)

(defn set-urls! [cfg-url cfg]
  (swap! *config-url*        str cfg-url)
  (swap! *nlp-tokens-path*   str (:nlp-tokens cfg))
  (swap! *nlp-pos-tggr-path* str (:nlp-pos-tggr cfg))
  (swap! *nlp-date-fin-path* str (:nlp-date-fin cfg))
  (swap! *nlp-loc-fin-path*  str (:nlp-loc-fin cfg))
  (swap! *db-url*            str (:db-url cfg)))