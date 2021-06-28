package xin.allonsy.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * CommonUtil
 *
 * @author wangshuai
 * @date 2020-07-21 15:56
 */
public class CommonUtil {

    public static String limitedString(String str, int lengthLimit) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() > lengthLimit) {
            str = str.substring(0, lengthLimit) + "...(data too long)";
        }
        return str;
    }

    /**
     * 根据权重随机选择 map的value为权重值
     *
     * @param map
     * @param <T>
     * @return
     */
    private static  <T> T selectKeyByWeight(Map<T, Integer> map) {
        TreeMap<Double, T> weightMap = new TreeMap<>();
        // 先排除权重为0的项
        map.entrySet().removeIf(entry -> entry.getValue() == 0);

        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            // 统一转为double
            double lastWeight = weightMap.size() == 0 ? 0 : weightMap.lastKey();
            // 权重累加
            weightMap.put(entry.getValue().doubleValue() + lastWeight, entry.getKey());
        }

        double randomWeight = weightMap.lastKey() * Math.random();
        SortedMap<Double, T> tailMap = weightMap.tailMap(randomWeight, false);
        return weightMap.get(tailMap.firstKey());
    }

    /**
     * 列表分片
     *
     * @param list
     * @param chunkSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Invalid chunk size: " + chunkSize);
        }
        List<List<T>> chunkList = new ArrayList<>(list.size() / chunkSize);
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunkList.add(list.subList(i, i + chunkSize >= list.size() ? list.size() : i + chunkSize));
        }
        return chunkList;
    }

    /**
     * 异常堆栈toStirng
     *
     * @param e
     * @return
     */
    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * bean copy  为null的字段不copy
     *
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
