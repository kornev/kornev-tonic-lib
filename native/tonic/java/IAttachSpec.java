package tonic.java;

import java.util.List;

public interface IAttachSpec {
    Long getDbId();
    String getDbName();
    Long getTblId();
    String getTblName();
    List<String> getPartCols();
    List<String> getPartTypes();
    List<String> getPartKeyVals();
    String getPartLocationMove();
    Long getSdId();
    Long getCdId();
    String getLocation();
    String getAddPartitionSql();
    String getDataLocation();
}
