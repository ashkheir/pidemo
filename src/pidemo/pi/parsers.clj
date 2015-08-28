(ns pidemo.pi.parsers
  ;(import 'package name' ParserIntf)
  (:gen-class))

(require 'clj-yaml.core
         'clj-json.core
         'clojure.data.csv
         'clj-time.core
         'clj-time.coerce
         'clj-time.format)

(use 'opennlp.nlp
     'opennlp.treebank
     'pidemo.pi.vars)

(def nlp-tokens-path   "models/en-token.bin")
(def nlp-pos-tggr-path "models/en-pos-maxent.bin")
(def nlp-date-fin-path "models/namefind/en-ner-date.bin")
(def nlp-loc-fin-path  "models/namefind/en-ner-location.bin")

(def lang-set #{"en" "fr" "nl" "es" "de"})

(defn cascade-parsers [fns s]
  "Evaluate functions until one returns a non-nil value"
  (some #(identity
          (try (% s)
            (catch Exception e
                   (str "While evaluating the function" %
                        "\nCaught the exception:"(.getMessage e)"\n")))) fns))

;(def get-sentences (make-sentence-detector "models/en-sent.bin"))
  (def tokenize      (make-tokenizer         nlp-tokens-path))
;(def detokenize   (make-detokenizer        "models/english-detokenizer.xml"))
(def pos-tag       (make-pos-tagger        nlp-pos-tggr-path))
;(def person-find   (make-name-finder       "models/namefind/en-ner-person.bin"))
  (def date-find     (make-name-finder       nlp-date-fin-path))
  (def loc-find      (make-name-finder       nlp-loc-fin-path))
;(def money-find    (make-name-finder       "models/namefind/en-ner-money.bin"))
;(def org-find      (make-name-finder       "models/namefind/en-ner-organization.bin"))
;(def perc-find     (make-name-finder       "models/namefind/en-ner-percentage.bin"))
  ;(def time-find     (make-name-finder       "models/namefind/en-ner-time.bin"))
;(def chunker       (make-treebank-chunker  "models/en-chunker.bin"))

(defn get-uri-suffix [uri]
  (.toLowerCase (subs uri (inc (.lastIndexOf uri ".")))))

(defn tokenize
  ([str] (tokenize str #"\s+"))
  ;([str] (tokenize str #"\W+"))
  ([str separator]
    (if (clojure.string/blank? str)
      nil
      (clojure.string/split str separator))))

(defn parse-yaml [s]
  (->> s (.split #"\-\-\-") (map clj-yaml.core/parse-string)))

(defn parse-json [s] (clj-json.core/parse-string s))

(defn parse [uri contents]
  (let [suffix (get-uri-suffix uri)]
    (case suffix
      "txt"  (parse-yaml contents)
      "yml"  (parse-yaml contents)
      "yaml" (parse-yaml contents)
      "json" (parse-json contents)
      "csv"  (clojure.data.csv/read-csv contents)
      nil)))

; Parsers for the main types of answers

(defn parse-binary [s]
  (-> s .toLowerCase #{"yes" "y" "true" "t" "correct"} nil? not))

(defn parse-integer [s] (Integer/parseInt s))

(defn str->natural-number [s]
  (let [n (re-matches #"[0-9]+" s)]
    (if (not (nil? n))
      (Integer/parseInt n))))

(defn parse-naturalnumber [s]
  (let [i (Integer/parseInt s)]
    (if (or (zero? i)(pos? i)) i nil)))

(defn pos-int [s]
  (let [i (Integer/parseInt s)]
    (if (pos? i) i nil)))

; Test throwing an exception
(defn t [s] (throw (Exception. "\nEXCEPTION\n")))

(defn parse-positiveinteger [s]
  (let [fns [t pos-int]]
    (cascade-parsers fns s)))

(defn parse-negativeinteger [s]
  (let [i (Integer/parseInt s)]
    (if (neg? i) i nil)))

(defn parse-number [s] (Double/parseDouble s))

(defn parse-negativenumber [s]
  (let [d (Double/parseDouble s)]
    (if (neg? d) d nil)))

(defn parse-positivenumber [s]
  (let [d (Double/parseDouble s)]
    (if (pos? d) d nil)))

; https://github.com/cemerick/utc-dates
(defn parse-date [s] (-> s tokenize date-find))
(defn parse-time [s] "time parser")
(defn parse-location [s] (-> s tokenize loc-find))

(defn parse-text [s] (println "\n\n(get-nlp-tokens-path):"(get-nlp-tokens-path))(-> s tokenize pos-tag))

; http://stackoverflow.com/questions/8614734/how-do-i-implement-a-java-interface-in-clojure
;(defn parse-example []
;  (reify ParserIntf
;    (parse []
;      ())))