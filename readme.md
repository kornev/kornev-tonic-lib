# Tonic

## Using Tonic

```scala
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

import tonic.java.{ ITonic, Tonic }

val rpc: ITonic = new Tonic("127.0.0.1", 8080)

for {
  table <- rpc.tableDescribe("processing", "user_agg").toScala
  last  <- rpc.partitionLast(table.getTblId).toScala
  prev  <- rpc.partitionPrev(table.getTblId, last.getCreateTime).toScala
} {
  val schema = prev.getPartSchema.asScala.head

  println(s"ITableDesc{tblId=${table.getTblId},dbLocationUri=${table.getDbLocationUri}};")
  println(s"IPart{createTime=${prev.getCreateTime},location=${prev.getLocation}};")
  println(s"IPartSchema{pkeyName=${schema.getPkeyName},partKeyVal=${schema.getPartKeyVal}};")
}
```

```text
ITableDesc{tblId=59,dbLocationUri=hdfs://ensime/warehouse/tablespace/managed/hive/processing.db};
IPart{createTime=1626659658,location=hdfs://ensime/warehouse/tablespace/external/hive/processing.db/user_agg/dt=2021-07-18};
IPartSchema{pkeyName=dt,partKeyVal=2021-07-18};
```

## Prerequisites

* Java 8, 11
* Gin

## Getting Tonic

SBT users may add this to their `Dependencies.scala`:

```scala
resolvers += "clojars" at "https://repo.clojars.org/"

libraryDependencies += "com.ensime" % "tonic" % "0.1"
```

## License

Copyright (C) 2022 Vadim Kornev.  
Distributed under the MIT License.
