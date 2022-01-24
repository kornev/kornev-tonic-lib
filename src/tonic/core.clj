(ns tonic.core
  (:require [farseer.client :as rpc]))

(defn mk-config [host port]
  (rpc/make-client {:http/url (str "http://" host ":" port "/rpc")}))

(defn table-desc
  [cfg dbs-name tbl-name]
  (rpc/call cfg :table/describe [dbs-name tbl-name]))

(defn partition-list
  [cfg tbl-id n]
  (rpc/call cfg :partition/list [tbl-id n]))

(defn partition-prev
  [cfg tbl-id ts]
  (rpc/call cfg :partition/previous [tbl-id ts]))
