package tonic.java;

import java.util.List;

public interface IPart {
    Integer getTblId();
    Integer getSdId();
    Integer getPartId();
    Integer getCreateTime();
    Integer getCdId();
    String getPartName();
    String getLocation();
    List<IPartSchema> getPartSchema();
}
