package tonic.java;

import java.util.List;

public interface IDetachSpec {
    String getDbName();
    Long getTblId();
    String getTblName();
    Long getPartId();
    String getPartName();
    List<IPartValsSpec> getPartValsSpec();
    String getPartKeyValStr();
    String getPartLocationUnlink();
    Long getCreateTime();
    Long getSdId();
    Long getCdId();
    String getLocation();
    String getDropPartitionSql();
}
