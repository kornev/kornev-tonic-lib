package tonic.java;

import java.util.List;

public interface ITableDescribe {
    Long getDbId();
    String getDbName();
    Long getTblId();
    String getTblName();
    Long getSdId();
    Long getCdId();
    List<IPartSpec> getPartSpec();
    String getDbLocationUri();
    String getLocation();
}
