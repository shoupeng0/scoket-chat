package site.prodigal.core;

import cn.hutool.core.util.TypeUtil;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.util.AntPathMatcher;
import site.prodigal.entity.Action;
import site.prodigal.entity.ChatRecord;
import site.prodigal.serialization.Protocol;
import site.prodigal.service.impl.ChatRecordServiceImpl;
import site.prodigal.service.impl.UserServiceImpl;
import site.prodigal.utils.MethodsLoaderUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author ShouPeng
 * @description 请求处理类
 * @since 2023/11/8
 */
public class RequestHandler {
    private static final Map<String, Method> METHOD;
    private static final Map<String, Object> OBJS;

    static {
        try {
            Map<String, Class<?>> aClass = MethodsLoaderUtils.loadClass("site.prodigal.service.impl");
            METHOD = MethodsLoaderUtils.loadAllMethod(aClass);
            OBJS = MethodsLoaderUtils.newInstance(aClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object handle(Action action) throws InvocationTargetException, IllegalAccessException {
        String key = action.getPath().substring(1);
        Method method = METHOD.get(key);
        int length = method.getParameterTypes().length;

        //转换为方法的参数类型
        Object[] covertParams = new Object[length];
        for (int i = 0; i < length; i++) {
            Type type = TypeUtil.getParamType(method, i);
            Class<?> klass = null;
            if (type instanceof Class) {
                klass = (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                klass = (Class<?>) parameterizedType.getRawType();
            }
            covertParams[i] = Protocol.OBJECT_MAPPER.convertValue(action.getParams()[i], klass);
        }
        action.setParams(covertParams);
        return method.invoke(OBJS.get(key), covertParams);
    }


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, IOException {
//        Map<String, Class<?>> aClass = MethodsLoaderUtils.loadClass("site.prodigal.service.impl");
//        Map<String, Method> method = MethodsLoaderUtils.loadAllMethod(aClass);
//        Map<String, Object> objs = MethodsLoaderUtils.newInstance(aClass);
//        Method record = method.get("insertRecord");
//        record.invoke(objs.get("insertRecord"),new ChatRecord(null,"7698627","7698627","test3"));
//        System.out.println(handle("/insertRecord", new ChatRecord(null,"7698627","7698627","test")));
    }

}
