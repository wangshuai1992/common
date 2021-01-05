package xin.allonsy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    private static RedisTemplate getRedisTemplate(){
        if(redisTemplate == null){
            // redisTemplate对应的bean的名称
            String redisBeanName = ApplicationContextUtil.getProperty("xin.allonsy.redis.beanname");
            if (StringUtils.isEmpty(redisBeanName)) {
                redisBeanName = "allonsyStringRedisTemplate";
            }
            redisTemplate = (RedisTemplate) ApplicationContextUtil.getBean(redisBeanName);
        }
        return redisTemplate;
    }

    /**
     * 设值过期时间 单位小时
     * @param key
     * @param expireMilliSeconds
     */
    public static void expire(String key, long expireMilliSeconds){
        getRedisTemplate().expire(key, expireMilliSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * exists 方法
     * @param key
     * @return
     */
    public static boolean exists(String key){
        return getRedisTemplate().hasKey(key);
    }
    /**
     * set 方法
     * @param key
     * @param value
     */
    public static void set(String key, String value){
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * 设值 有超时时间
     * @param key
     * @param value
     * @param expireTime 单位为秒
     */
    public static void set(String key, String value, long expireTime){
        set(key, value, expireTime, TimeUnit.SECONDS);
    }


    /**
     * get 方法
     * @param key
     * @return
     */
    public static long get(String key){
        return Long.valueOf(getValue(key).toString());
    }

    /**
     * get 方法
     * @param key
     * @return
     */
    public static Object getValue(String key){
        return getRedisTemplate().opsForValue().get(key);
    }

    /**
     * 设值 有超时时间
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     */
    public static void set(String key, String value, long expireTime, TimeUnit timeUnit){
        getRedisTemplate().opsForValue().set(key, value, expireTime, timeUnit);
    }

    /**
     * 递减
     * @param key
     * @param delta
     * @return 减去delta值之后的值
     * 这个key值不存在的话 先设值key值为0 减去delta再返回
     */
    public static long decrBy(String key, long delta) {
        return getRedisTemplate().opsForValue().increment(key, -delta);
    }

    public static long decr(String key) {
        return getRedisTemplate().opsForValue().increment(key, -1);
    }

    /**
     * 递增
     * @param key
     * @param delta
     * @return 加上delta值之后的值
     * 这个key值不存在的话 先设值key值为0 加上delta再返回
     */
    public static long incrBy(String key, long delta){
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /**
     *
     * @param key
     * @return
     */
    public static long incr(String key){ return getRedisTemplate().opsForValue().increment(key, 1L);
    }

    /**
     * setnx 方法
     * @param key
     * @param value
     * @return key值如果不存在 设值键值 并返回true
     * 如果存在 什么都不做 并返回false
     */
    public static boolean setnx(String key, long value){
        return setnx(key, String.valueOf(value));
    }

    /**
     * setnx 方法
     * @param key
     * @param value
     * @return key值如果不存在 设值键值 并返回true
     * 如果存在 什么都不做 并返回false
     */
    public static boolean setnx(String key, String value){
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }

    /**
     * hexists 方法
     * @param key
     * @param hashKey
     * @return
     */
    public static boolean hexists(String key, String hashKey){
        return getRedisTemplate().opsForHash().hasKey(key, hashKey);
    }

    /**
     * hsetnx 方法
     * @param key
     * @param hashKey
     * @param hashValue
     * @return hashKey值如果不存在 设值键值 并返回true
     * 如果存在 什么都不做 并返回false
     */
    public static boolean hsetnx(String key, String hashKey, String hashValue){
        return getRedisTemplate().opsForHash().putIfAbsent(key, hashKey, hashValue);
    }

    /**
     * hdel 方法
     * @param key
     * @param hashKeys
     * @return 返回删除成功的数量
     */
    public static long hdel(String key, String... hashKeys){
        return getRedisTemplate().opsForHash().delete(key, hashKeys);
    }

    /**
     * del 方法
     */
    public static Boolean del(String key) {
        return getRedisTemplate().delete(key);
    }

    /**
     * hget 方法
     * @param key
     * @param hashKey
     * @return
     */
    public static String hget(String key, String hashKey){
        Object object = getRedisTemplate().opsForHash().get(key, hashKey);
        return object == null ? "" : object.toString();
    }

    /**
     * hgetall 方法
     * @param key
     * @return
     */
    public static Map<String, String> hgetAll(String key){
        return getRedisTemplate().opsForHash().entries(key);
    }
}
