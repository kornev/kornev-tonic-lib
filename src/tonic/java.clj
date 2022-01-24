(ns tonic.java
  (:require [farseer.error :as e]
            [tonic.core :as tonic]))

(definterface ITableDesc
  (^Integer getDbId [])
  (^Integer getTblId [])
  (^Integer getSdId [])
  (^Integer getCdId [])
  (^String getDbLocationUri [])
  (^String getLocation []))

(defrecord TableDesc [DB_ID TBL_ID SD_ID CD_ID DB_LOCATION_URI LOCATION]
  ITableDesc
  (getDbId [_] DB_ID)
  (getTblId [_] TBL_ID)
  (getSdId [_] SD_ID)
  (getCdId [_] CD_ID)
  (getDbLocationUri [_] DB_LOCATION_URI)
  (getLocation [_] LOCATION))

(definterface IPartSchema
  (^Integer getIntegerIdx [])
  (^String getPkeyName [])
  (^String getPkeyType [])
  (^String getPartKeyVal []))

(defrecord PartSchema [INTEGER_IDX PKEY_NAME PKEY_TYPE PART_KEY_VAL]
  IPartSchema
  (getIntegerIdx [_] INTEGER_IDX)
  (getPkeyName [_] PKEY_NAME)
  (getPkeyType [_] PKEY_TYPE)
  (getPartKeyVal [_] PART_KEY_VAL))

(definterface IPart
  (^Integer getTblId [])
  (^Integer getSdId [])
  (^Integer getPartId [])
  (^Integer getCreateTime [])
  (^Integer getCdId [])
  (^String getPartName [])
  (^String getLocation [])
  (^java.util.List getPartSchema []))

(defrecord Part [TBL_ID SD_ID PART_ID CREATE_TIME CD_ID PART_NAME LOCATION PART_SCHEMA]
  IPart
  (getTblId [_] TBL_ID)
  (getSdId [_] SD_ID)
  (getPartId [_] PART_ID)
  (getCreateTime [_] CREATE_TIME)
  (getCdId [_] CD_ID)
  (getPartName [_] PART_NAME)
  (getLocation [_] LOCATION)
  (getPartSchema [_] PART_SCHEMA))

(defn- response->result
  [{:keys [result error] :as m}]
  (cond
    (contains? m :result) (not-empty result)
    (contains? m :error) (e/error! error)
    :else (throw
            (ex-info "Wrong data structure in response; Expected: contains :result or :error field" m))))

(defn- obj->Part [m]
  (when m
    (map->Part (update m :PART_SCHEMA #(mapv map->PartSchema %)))))

(defn- obj->TableDesc [m]
  (when m
    (map->TableDesc m)))

(definterface ITonic
  (^java.util.Optional tableDescribe [^String var1 ^String var2])
  (^java.util.Optional partitionLast [^Integer var1])
  (^java.util.Optional partitionPrev [^Integer var1 ^Integer var2]))

(gen-class
  :name "tonic.java.Tonic"
  :implements [tonic.java.ITonic]
  :init "init-Tonic"
  :state "state"
  :constructors {[String Integer] []})

(defn- -init-Tonic [host port]
  [[] (tonic/mk-config host port)])

(defn -tableDescribe [this dbs-name tbl-name]
  (let [cfg (.state this)]
    (-> (tonic/table-desc cfg dbs-name tbl-name)
        response->result
        obj->TableDesc
        java.util.Optional/ofNullable)))

(defn -partitionLast [this tbl-id]
  (let [cfg (.state this)]
    (-> (tonic/partition-list cfg tbl-id 1)
        response->result
        first
        obj->Part
        java.util.Optional/ofNullable)))

(defn -partitionPrev [this tbl-id ts]
  (let [cfg (.state this)]
    (-> (tonic/partition-prev cfg tbl-id ts)
        response->result
        obj->Part
        java.util.Optional/ofNullable)))
