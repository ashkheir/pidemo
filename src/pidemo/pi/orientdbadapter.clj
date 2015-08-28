(ns pidemo.pi.orientdbadapter
  (:gen-class))

(import com.orientechnologies.orient.server.OServerMain
        com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
        com.orientechnologies.orient.core.record.impl.ODocument)

(def server (atom nil))
(def db-conn (atom nil))

(defn connect-to-in-memory-db [config-url]
  (let [server (OServerMain/create)]
    (->> config-url java.net.URI. java.io.File. (.startup server))
    ;(->> config-url #(subs % 7) slurp (.startup server))
    ;(->> config-url #(subs % 7) java.io.File. (.startup server))
    ;(if (nil? (deref server))
    ;  (reset! server (OServerMain/create))
    (.activate server)
    server))

(defn shutdown-server [server] (.shutdown server))

(defn open-db [database-url]
  (println "\n\nOpening a connection to OrientDB...")
  (ODatabaseDocumentTx. database-url))

(defn close-db [db] (.close db))

(defn create-doc []
  (let [doc (ODocument. "Prova")]
    ;(.field doc "foo" "bar")
    ;(.save doc)
    ))

(defn insert-in-orientdb [server data]
  nil)
