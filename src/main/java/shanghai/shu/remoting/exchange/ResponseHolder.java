package shanghai.shu.remoting.exchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseHolder {
    private static Map<Long,ResponseFuture> responseMap=new ConcurrentHashMap<>();

    public static void put(Long key,ResponseFuture future){
        responseMap.put(key,future);
    }
    public static ResponseFuture get(Long key){
        return responseMap.get(key);
    }
    public static ResponseFuture remove(Long key){
        return responseMap.remove(key);
    }
}
