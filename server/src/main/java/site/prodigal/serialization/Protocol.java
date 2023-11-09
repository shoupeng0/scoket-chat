package site.prodigal.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.prodigal.entity.Action;

/**
 * @author ShouPeng
 * @date 2023/10/31 15:08
 * @email 7698627@qq.com
 *
 */
public class Protocol {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJsonStr(Object obj){
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        }catch (Exception e){
            return null;
        }

    }

    public static Object toObject(String jsonStr,Class<?> objClass){
        try {
            return OBJECT_MAPPER.readValue(jsonStr,objClass);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
