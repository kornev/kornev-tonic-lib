package tonic.java;

import java.util.List;

public interface IPart {
    Long getTblId();
    Long getSdId();
    Long getPartId();
    Long getCreateTime();
    Long getCdId();
    String getPartName();
    String getLocation();
    List<IPartValsSpec> getPartValsSpec();
}
