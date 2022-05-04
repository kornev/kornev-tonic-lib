(ns tonic.java
  (:require [farseer.error :as e]
            [tonic.core :as tonic]))

(definterface IPartSpec
  (^Long getIntegerIdx [])
  (^String getPkeyName [])
  (^String getPkeyType []))

(defrecord PartSpec [INTEGER_IDX PKEY_NAME PKEY_TYPE]
  IPartSpec
  (getIntegerIdx [_] INTEGER_IDX)
  (getPkeyName [_] PKEY_NAME)
  (getPkeyType [_] PKEY_TYPE))

(definterface ITableDescribe
  (^Long getDbId [])
  (^String getDbName [])
  (^Long getTblId [])
  (^String getTblName [])
  (^Long getSdId [])
  (^Long getCdId [])
  (^java.util.List getPartSpec [])
  (^String getDbLocationUri [])
  (^String getLocation []))

(defrecord TableDescribe [DB_ID DB_NAME TBL_ID TBL_NAME SD_ID CD_ID PART_SPEC DB_LOCATION_URI LOCATION]
  ITableDescribe
  (getDbId [_] DB_ID)
  (getDbName [_] DB_NAME)
  (getTblId [_] TBL_ID)
  (getTblName [_] TBL_NAME)
  (getSdId [_] SD_ID)
  (getCdId [_] CD_ID)
  (getPartSpec [_] PART_SPEC)
  (getDbLocationUri [_] DB_LOCATION_URI)
  (getLocation [_] LOCATION))

(definterface IPartValsSpec
  (^Long getIntegerIdx [])
  (^String getPkeyName [])
  (^String getPkeyType [])
  (^String getPartKeyVal []))

(defrecord PartValsSpec [INTEGER_IDX PKEY_NAME PKEY_TYPE PART_KEY_VAL]
  IPartValsSpec
  (getIntegerIdx [_] INTEGER_IDX)
  (getPkeyName [_] PKEY_NAME)
  (getPkeyType [_] PKEY_TYPE)
  (getPartKeyVal [_] PART_KEY_VAL))

(definterface IPart
  (^Long getTblId [])
  (^Long getSdId [])
  (^Long getPartId [])
  (^Long getCreateTime [])
  (^Long getCdId [])
  (^String getPartName [])
  (^String getLocation [])
  (^java.util.List getPartValsSpec []))

(defrecord Part [TBL_ID SD_ID PART_ID CREATE_TIME CD_ID PART_NAME LOCATION PART_SPEC_VAL]
  IPart
  (getTblId [_] TBL_ID)
  (getSdId [_] SD_ID)
  (getPartId [_] PART_ID)
  (getCreateTime [_] CREATE_TIME)
  (getCdId [_] CD_ID)
  (getPartName [_] PART_NAME)
  (getLocation [_] LOCATION)
  (getPartValsSpec [_] PART_SPEC_VAL))

(definterface IAttachSpec
  (^Long getDbId [])
  (^String getDbName [])
  (^Long getTblId [])
  (^String getTblName [])
  (^java.util.List getPartCols [])
  (^java.util.List getPartTypes [])
  (^java.util.List getPartKeyVals [])
  (^String getPartLocationMove [])
  (^Long getSdId [])
  (^Long getCdId [])
  (^String getLocation [])
  (^String getAddPartitionSql [])
  (^String getDataLocation []))

(defrecord AttachSpec [DB_ID DB_NAME TBL_ID TBL_NAME PART_COLS PART_TYPES PART_KEY_VALS PART_LOCATION_MOVE
                       SD_ID CD_ID LOCATION ADD_PARTITION_SQL DATA_LOCATION]
  IAttachSpec
  (getDbId [_] DB_ID)
  (getDbName [_] DB_NAME)
  (getTblId [_] TBL_ID)
  (getTblName [_] TBL_NAME)
  (getPartCols [_] PART_COLS)
  (getPartTypes [_] PART_TYPES)
  (getPartKeyVals [_] PART_KEY_VALS)
  (getPartLocationMove [_] PART_LOCATION_MOVE)
  (getSdId [_] SD_ID)
  (getCdId [_] CD_ID)
  (getLocation [_] LOCATION)
  (getAddPartitionSql [_] ADD_PARTITION_SQL)
  (getDataLocation [_] DATA_LOCATION))

(definterface IDetachSpec
  (^String getDbName [])
  (^Long getTblId [])
  (^String getTblName [])
  (^Long getPartId [])
  (^String getPartName [])
  (^java.util.List getPartValsSpec [])
  (^String getPartKeyValStr [])
  (^String getPartLocationUnlink [])
  (^Long getCreateTime [])
  (^Long getSdId [])
  (^Long getCdId [])
  (^String getLocation [])
  (^String getDropPartitionSql []))

(defrecord DetachSpec [DB_NAME TBL_ID TBL_NAME PART_ID PART_NAME PART_SPEC_VAL PART_KEY_VAL_STR PART_LOCATION_UNLINK
                       CREATE_TIME SD_ID CD_ID LOCATION DROP_PARTITION_SQL]
  IDetachSpec
  (getDbName [_] DB_NAME)
  (getTblId [_] TBL_ID)
  (getTblName [_] TBL_NAME)
  (getPartId [_] PART_ID)
  (getPartName [_] PART_NAME)
  (getPartValsSpec [_] PART_SPEC_VAL)
  (getPartKeyValStr [_] PART_KEY_VAL_STR)
  (getPartLocationUnlink [_] PART_LOCATION_UNLINK)
  (getCreateTime [_] CREATE_TIME)
  (getSdId [_] SD_ID)
  (getCdId [_] CD_ID)
  (getLocation [_] LOCATION)
  (getDropPartitionSql [_] DROP_PARTITION_SQL))

(defn- obj->TableDescribe [m]
  (when m
    (map->TableDescribe (update m :PART_SPEC #(mapv map->PartSpec %)))))

(defn- response->result
  [{:keys [result error] :as m}]
  (cond
    (contains? m :result) (not-empty result)
    (contains? m :error) (e/error! error)
    :else (throw
            (ex-info "Wrong data structure in response; Expected: contains :result or :error field" m))))

(defn- obj->Part [m]
  (when m
    (map->Part (update m :PART_SPEC_VAL #(mapv map->PartValsSpec %)))))

(definterface ITonic
  (^java.util.Optional tableDescribe [^String var1 ^String var2])
  (^java.util.Optional partitionLast [^Long var1])
  (^java.util.Optional partitionParent [^Long var1 ^Long var2])
  (^java.util.Optional partitionFind [^Long var1 ^java.util.List var2])
  (^tonic.java.IAttachSpec partitionAttach [^Long var1 ^java.util.List var2 ^String var3])
  (^tonic.java.IDetachSpec partitionDetach [^Long var1 ^java.util.List var2]))

(gen-class
  :name "tonic.java.Tonic"
  :implements [tonic.java.ITonic]
  :init "init-Tonic"
  :state "state"
  :constructors {[String Integer] []})

(defn- -init-Tonic [host port]
  [[] (tonic/req-keys host port)])

(defn -tableDescribe [this db-name tbl-name]
  (let [cfg (.state this)]
    (-> (tonic/table-describe cfg db-name tbl-name)
        response->result
        obj->TableDescribe
        java.util.Optional/ofNullable)))

(defn -partitionLast [this tbl-id]
  (let [cfg (.state this)]
    (-> (tonic/partition-list cfg tbl-id 1)
        response->result
        first
        obj->Part
        java.util.Optional/ofNullable)))

(defn -partitionParent [this tbl-id ts]
  (let [cfg (.state this)]
    (-> (tonic/partition-parent cfg tbl-id ts)
        response->result
        obj->Part
        java.util.Optional/ofNullable)))

(defn -partitionFind [this tbl-id args]
  (let [cfg (.state this)]
    (-> (tonic/partition-find cfg tbl-id args)
        response->result
        obj->Part
        java.util.Optional/ofNullable)))

(defn -partitionAttach
  [this tbl-id part-vals path]
  (let [cfg (.state this)]
    (-> (tonic/partition-attach cfg tbl-id part-vals path)
        response->result
        map->AttachSpec)))

(defn -partitionDetach
  [this tbl-id part-vals]
  (let [cfg (.state this)]
    (-> (tonic/partition-detach cfg tbl-id part-vals)
        response->result
        (update :PART_SPEC_VAL #(mapv map->PartValsSpec %))
        map->DetachSpec)))
