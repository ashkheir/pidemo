(ns pidemo.routes.home
  (:require [pidemo.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(use 'pidemo.pi.pilifecycle
     'pidemo.pi.vars
     'pidemo.pi.io
     'pidemo.pi.config
     'pidemo.pi.questionmanager
     'ring.util.codec)

(def media-types {:yaml "application/x-yaml" :json "application/json"
                  :text "text/plain" :xml "text/xml" :pdf "application/pdf"})

(defn gen-resp [body media]
  {:status 200 :headers {"Content-Type" (:media media-types)} :body body})

(defn home-page []
  (layout/render "home.html"))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))

  (GET "/questions" []
    ;(-> (get-cfg-url) get-config load-questions-web (gen-resp :json))
    ;(gen-resp (-> (get-cfg-url) get-config load-questions-web) :json)
    {:status 200 :headers {"Content-Type" "application/json"} :body (-> (get-cfg-url) get-config load-questions-web)}
  )

  (GET "/dialogue/:input" [input]
    (-> (get-cfg-url) get-config (dialogue-web (form-decode input) )))

  (GET "/diagnostics" []
    "Diagnostics HTML page"
    ;(layout/render "diagnostics.html")
  )
  (GET "/diagnostics-config-raw" [] (-> (get-cfg-url) get-uri))

  (GET "/diagnostics-config-proc" [] (-> (get-cfg-url) get-config (gen-resp :json)))

  (GET "/diagnostics-language" [] (-> (get-cfg-url) get-config :lang str))

  (GET "/diagnostics-any-mandatory-questions" []
    (-> (get-cfg-url) get-config load-questions any-mandatory-qstns? str))

  ;(GET "/diagnostics-valid-dialogue" []
  ;  (-> ... valid-dialogue? str))

  (GET "/diagnostics-channel" [] (-> (get-cfg-url) get-config :channel str))

  (GET "/diagnostics-actions" [] (-> (get-cfg-url) get-config :actions-url get-uri))

  (GET "/diagnostics-db-url" [] (-> (get-cfg-url) get-config :db-url str))

  (GET "/diagnostics-db-config" []
    ;(-> (get-cfg-url) get-config :db-url get-uri (gen-resp :xml)
    {:status 200 :headers {"Content-Type" "text/xml"} :body (-> (get-cfg-url) get-config :db-url get-uri)}
  )

  (GET "/diagnostics-parse/:type/:s" [type s]
    (let [form-dec-s (form-decode s)
          parsing-fn (->> type (str "RemoveMe-") make-parsing-fn)]
      (println "\n\nDIAGNOSTICS - PARSING FN:"parsing-fn)
      (println "DIAGNOSTICS - PARSING FORM-DECODED INPUT:"form-dec-s)
      (println "DIAGNOSTICS - PARSED:"(parsing-fn form-dec-s))
      (-> form-dec-s parsing-fn
          ;first
          )))

  (GET "/diagnostics-exec-actions" []
    "Execute actions")
)
