package cn.iecas.message.utils.convert;

import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    /**
     * Map对象转换为实体对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将实体类对象转换为Map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.entrySet()) {
                map.put((String) key, beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将Map转化为List
     * @param map
     * @return
     */
    public static List mapToList(Map<String, String> map) {
        List list = new ArrayList();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }
}
