package tonic.java;

import java.util.Optional;

public interface ITonic {
    Optional<ITableDesc> tableDescribe(String var1, String var2);
    Optional<IPart> partitionLast(Integer var1);
    Optional<IPart> partitionPrev(Integer var1, Integer var2);
}
