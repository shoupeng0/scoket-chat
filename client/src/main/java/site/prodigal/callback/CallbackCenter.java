package site.prodigal.callback;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2023/11/16
 */
public class CallbackCenter {

    private static final Map<String,Object> CALLBACK = new HashMap<>();

    public static void register(String key,Object obj){
        CALLBACK.put(key, obj);
    }

    public static Object get(String key){
        return CALLBACK.get(key);
    }

    public static void clear(){
        CALLBACK.clear();
    }

}
