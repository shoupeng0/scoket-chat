package site.prodigal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author spp
 * @date 2020-10-27 15:03
 * 类加载
 **/
public class MethodsLoaderUtils {

    /**
     * 装载所有的class
     * @throws IOException
     */
    public static Map<String,Class<?>> loadClass(String pack) throws IOException {
        System.out.println("start scan pack: " + pack);
        Map<String,Class<?>> allClass = new HashMap<>();
        List<String> className = new ArrayList<>();
        pack = pack.trim().replaceAll("\\.","/");
        scanClass(pack,className);
        className.forEach(e->{
            try {
                Class<?> aClass = Class.forName(e);
                allClass.put(e.substring(e.lastIndexOf(".")+ 1),aClass);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("end scan pack: " + pack);
        return allClass;
    }

    public static Map<String, Method> loadAllMethod(Map<String,Class<?>> allClass){
        System.out.println("start scan methods");
        Map<String, Method> methods = new HashMap<>();
        allClass.forEach((k,v)->{
            for (Method method : v.getMethods()) {
                // 检查方法的声明类是否为Object类或其超类
                if (!method.getDeclaringClass().equals(Object.class)) {
                    methods.put(method.getName(), method);
                }
            }
        });
        System.out.println("end scan methods");
        return methods;
    }

    public static Map<String,Object> newInstance(Map<String,Class<?>> allClass) throws IOException {
        System.out.println("start create instance");
        Map<String,Object> instance = new HashMap<>();
        allClass.forEach((k,v)->{
            Object obj;
            try {
                obj = v.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            for (Method method : v.getMethods()) {
                if (!method.getDeclaringClass().equals(Object.class)) {
                    instance.put(method.getName(), obj);
                }
            }
        });
        System.out.println("end create instance");
        return instance;
    }

    /**
     * 扫描所有的class文件
     * @param pack
     * @throws FileNotFoundException
     */
    private static void scanClass(String pack,List<String> className) throws FileNotFoundException {
        URL url = MethodsLoaderUtils.class.getClassLoader().getResource(pack);
        File file = new File(url.getFile());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileSon : files) {
                if (fileSon.isDirectory()){
                    //递归扫描所有的文件
                    scanClass(pack + "/" + fileSon.getName(),className);
                }else {
                    int beginIndex = fileSon.getName().lastIndexOf(".");
                    if(beginIndex != -1 && ".class".equals(fileSon.getName().substring(beginIndex))){
                        //扫描所有以class结尾的文件
                        //去除尾部的.class后缀,并拼接后缀
                        String cn = pack.replaceAll("/",".") +"." + fileSon.getName().substring(0,fileSon.getName().length() - 6);
                        className.add(cn);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("没有找到需要扫描的文件目录");
        }
    }

}
