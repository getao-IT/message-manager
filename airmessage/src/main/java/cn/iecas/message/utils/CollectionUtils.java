package cn.iecas.message.utils;

import java.rmi.MarshalledObject;
import java.util.*;

public class CollectionUtils {

    /**
     * 返回根据key排序的map
     * @param map
     * @return
     */
    public static List<Map.Entry<String, Object>> sortMapByDate(Map<String, Object> map) {
        List<Map.Entry<String, Object>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o1.getKey());
            }
        });

        return list;
    }

    /**
     * List<Map.Entry<K, V>> 转化为 Map<K, V>
     * @param list
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> listToMap(List<Map.Entry<K, V>> list) {
        Map<K, V> map = new HashMap<>();

        if (list == null && list.size() == 0) {
            return map;
        }

        for (Map.Entry<K, V> kvMap : list) {
           if (kvMap != null) {
               map.put(kvMap.getKey(), kvMap.getValue());
           }
        }

        return map;
    }
}
