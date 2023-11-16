package site.prodigal.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.prodigal.entity.Action;
import site.prodigal.entity.ChatRecord;
import site.prodigal.entity.User;

import java.util.List;


public class Protocol {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    public static List<User> toObjectAsListUserReference(String jsonStr){
        try {
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<List<User>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ChatRecord> toObjectAsListRecordReference(String jsonStr){
        try {
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<List<ChatRecord>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
