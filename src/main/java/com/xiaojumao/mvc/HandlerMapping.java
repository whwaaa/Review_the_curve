package com.xiaojumao.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 11:02
 * @Modified By:
 */

/**
 * 映射器,里面包含了大量的网址和方法的对应关系
 */
public class HandlerMapping {
    private static Map<String, MVCMapping> data = new HashMap<>();

    public static MVCMapping get(String uri) {
        return data.get(uri);
    }

    public static void load(InputStream is){
        Properties ppt = new Properties();
        try {
            ppt.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collection<Object> values = ppt.values();
        for (Object value : values) {
            try {
                // 加载配置文件中的每一个类
                Class c = Class.forName((String) value);
                // 创建类的对象
                Object obj = c.getConstructor().newInstance();
                // 获取类的所有方法
                Method[] methods = c.getMethods();
                for (Method method : methods) {
                    Annotation[] as = method.getAnnotations();
                    if(as != null){
                        for (Annotation a : as) {
                            if(a instanceof ResponseBody){
                                // 此方法用于返回字符串给用户
                                MVCMapping mmaping = new MVCMapping(obj, method, ResponseType.TEXT);
                                Object o = data.put(((ResponseBody) a).value(), mmaping);
                                System.out.println("添加了方法: " + method.getName() + "   请求地址: " + ((ResponseBody) a).value());
                                if(o != null){
                                    throw new RuntimeException("请求地址重复: " + ((ResponseBody) a).value());
                                }
                            }else if(a instanceof  ResponseView){
                                // 此方法用于返回视图给用户
                                MVCMapping mmaping = new MVCMapping(obj, method, ResponseType.VIEW);
                                Object o = data.put(((ResponseView) a).value(), mmaping);
                                System.out.println("添加了方法: " + method.getName() + "   请求地址: " + ((ResponseView) a).value());
                                if(o != null){
                                    throw new RuntimeException("请求地址重复: " + ((ResponseView) a).value());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 映射对象,每一个对象封装了一个方法,用于处理请求
     */
    public static class MVCMapping{
        private Object obj;
        private Method method;
        private ResponseType type;

        public MVCMapping() {
        }

        public MVCMapping(Object obj, Method method, ResponseType type) {
            this.obj = obj;
            this.method = method;
            this.type = type;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public ResponseType getType() {
            return type;
        }

        public void setType(ResponseType type) {
            this.type = type;
        }
    }
}
