package xin.allonsy.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

}
