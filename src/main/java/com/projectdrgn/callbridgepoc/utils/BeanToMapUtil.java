package com.projectdrgn.callbridgepoc.utils;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanToMapUtil {
    public static Map<String, Object> bean2map(Object bean) throws Exception {
        Map<String, Object> map = new HashMap<>();

        BeanInfo b = Introspector.getBeanInfo(bean.getClass(), Object.class);
        PropertyDescriptor[] pds = b.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            Method m = pd.getReadMethod();
            Object properValue = m.invoke(bean);

            map.put(propertyName, properValue);
        }
        return map;
    }

    public static <T> T map2bean(Map<String, Object> map, Class<T> clz) throws Exception {
        T obj = clz.newInstance();

        BeanInfo b = Introspector.getBeanInfo(clz, Object.class);
        PropertyDescriptor[] pds = b.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            Method setter = pd.getWriteMethod();
            setter.invoke(obj, map.get(pd.getName()));
        }

        return obj;
    }

    public static Map<String, Object> removeUnderline(Map<String, Object> map) {
        Map<String, Object> res = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            res.put(key.replaceAll("_", ""), value);
        }
        return res;
    }
}
