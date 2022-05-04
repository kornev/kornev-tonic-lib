(ns tonic.core
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [farseer.client :as rpc]
            [clj-http.client :as http]
            [clj-http.util :as util])
  (:import (java.io BufferedReader
                    EOFException)))

;; See: https://github.com/dakrone/cheshire/issues/105
(defmethod http/coerce-response-body :json-fixed-types
  [_  {:keys [body] :as resp}]
  (let [^BufferedReader r (io/reader (util/force-stream body) :encoding "UTF-8")]
    (try
     (.mark r 1)
     (let [first-char (int
                       (try (.read r)
                            (catch EOFException _ -1)))]
       (case first-char
         -1 nil
         (do (.reset r)
             (assoc resp :body (json/read r :key-fn keyword)))))
     (finally
      (.close r)))))

(defn req-keys
  [host port]
  (rpc/make-client {:http/url (str "http://" host ":" port "/rpc")
                    :http/as :json-fixed-types}))

(defn table-describe
  [cfg db-name tbl-name]
  (rpc/call cfg :table/describe [db-name tbl-name]))

(defn partition-list
  [cfg tbl-id n]
  (rpc/call cfg :partition/list [tbl-id n]))

(defn partition-parent
  [cfg tbl-id ts]
  (rpc/call cfg :partition/parent [tbl-id ts]))

(defn partition-find
  [cfg tbl-id args]
  (rpc/call cfg :partition/find [tbl-id args]))

(defn partition-attach
  [cfg tbl-id part-vals path]
  (rpc/call cfg :partition/attach [tbl-id part-vals path]))

(defn partition-detach
  [cfg tbl-id part-vals]
  (rpc/call cfg :partition/detach [tbl-id part-vals]))
