package tonic.java;

import java.util.Optional;
import java.util.List;

public interface ITonic {
    Optional<ITableDescribe> tableDescribe(String var1, String var2);
    Optional<IPart> partitionLast(Long var1);
    Optional<IPart> partitionParent(Long var1, Long var2);
    Optional<IPart> partitionFind(Long var1, List<String> var2);
    IAttachSpec partitionAttach(Long var1, List<Object> var2, String var3);
    IDetachSpec partitionDetach(Long var1, List<Object> var2);
}
