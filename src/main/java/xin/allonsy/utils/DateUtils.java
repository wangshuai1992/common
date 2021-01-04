package xin.allonsy.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DateUtils
 *
 * @author wangshuai
 * @date 2019-09-06 15:24
 */
public class DateUtils {

    /**
     * 格式1
     */
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 锁对象
     */
    private static final Object LOCK_OBJ = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (LOCK_OBJ) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>
                    // 替代原来直接new SimpleDateFormat
                    tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    /**
     * 获取整日时分信息
     *
     * @return
     */
    public static String[] getFields() {
        DecimalFormat df = new DecimalFormat("00");
        String[] fields = new String[24 * 60];
        int k = 0;
        for (int j = 0; j < 24; j++) {
            String hour = df.format(j);
            for (int m = 0; m < 60; m++) {
                String minute = df.format(m);
                String field = hour + ":" + minute;
                fields[k++] = field;
            }
        }
        return fields;
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat, 这样每个线程只会有一个SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * Title: : parse Description: 按照指定格式转换Date字符
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            if (StringUtils.isEmpty(dateStr)) {
                return null;
            }
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("解析日期出错");
        }
    }

    /**
     * 获取指定时间的unix时间
     *
     * @param day
     * @param hour
     * @return
     */
    public static long getExpireUnixTime(int day, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获取当前时间所在年的周数
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前时间所在年的最大周数
     *
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }

    /**
     * 获取某年的第几周的开始日期
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar)c.clone();
        cal.add(Calendar.DATE, week * 7);
        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年的第几周的结束日期
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar)c.clone();
        cal.add(Calendar.DATE, week * 7);
        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取当前时间所在周的开始日期
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取当前时间所在周的结束日期
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar c = new GregorianCalendar();
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            c.set(year, month - 1, 31);
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            c.set(year, month - 1, 30);
        } else if (month == 2) {
            c.set(year, 2, 1);
            c.add(Calendar.DATE, -1);
        } else {
            c.set(year, 11, 31);
        }
        return c.getTime();
    }

    /**
     * 获取每月天数
     *
     * @param month
     * @return
     */
    public static int getDaysByMonth(int month) {
        int days = 30;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            days = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            days = 30;
        } else if (month == 2) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            Date date = getLastDayOfMonth(year, month);
            String dd = format(date, "yyMMdd");
            days = Integer.parseInt(dd.substring(4));
        }
        return days;
    }

    /**
     * 去掉时分秒
     *
     * @param date
     * @return Date
     */
    public static Date getYmdTime(Date date) {
        if (date == null) {
            return (new Date());
        }
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day.getTime();
    }

    /**
     * 根据传入的开始时间和结束时间处理
     *
     * @param startDateFrom
     * @param startDateTo
     * @return 返回开始时间 2018-06-27 00:00:00 结束时间 2018-06-27 23:59:59
     */
    public static Date[] getTime(Date startDateFrom, Date startDateTo) {
        Date[] result = new Date[2];
        Date start;
        Date end;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startDateFrom);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        start = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(startDateTo);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        end = calendar2.getTime();

        result[0] = start;
        result[1] = end;
        return result;
    }

    /**
     * 根据单位获取传入日期的上下区间 0 每天, 1 每周, 2 每月, 3 每年
     *
     * @param date
     * @param unit
     * @return 返回长度为2的日期数组，第一个元素为开始，第二个元素为结束
     */
    public static Date[] getTimePeriod(Date date, int unit) {
        Date[] result = new Date[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        Date start;
        Date end;

        switch (unit) {
            case 0: // day
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                start = calendar.getTime();
                calendar.add(Calendar.DATE, 1);
                end = calendar.getTime();
                break;
            case 1: // week
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (1 == dayWeek) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }
                // 重新获得当前日期是一个星期的第几天
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                // 给当前日期减去星期几与一个星期第一天的差值
                calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);

                start = calendar.getTime();
                // 下周1
                calendar.add(Calendar.DATE, 7);
                end = calendar.getTime();
                break;
            case 2: // month
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                start = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                end = calendar.getTime();
                break;
            case 3: // year
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                start = calendar.getTime();
                calendar.add(Calendar.YEAR, 1);
                end = calendar.getTime();
                break;
            default:
                throw new RuntimeException("unknow time unit");
        }
        result[0] = start;
        result[1] = end;
        return result;
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取下一日
     *
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        if (null == date) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 获取某个日期当天的最后一秒
     *
     * @param date
     * @return
     */
    public static Date getLastSecondOfDay(Date date) {
        if (null == date) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 取得当前时间，格式为yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getDateYmdhms() {
        Date date = new Date();
        return format(date, DATE_FORMAT_YYYYMMDDHHMMSS);
    }

    public static Date Milliseconds2Date(Long milliseconds) {
        if (Objects.isNull(milliseconds)) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return c.getTime();
    }

    public static void main(String[] args) {
        int year = 2017;
        // int week = 27;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(today);
        System.out.println("当前时间:current date = " + sdf.format(today));
        System.out.println("当前第几周:getWeekOfYear = " + getWeekOfYear(today));
        System.out.println("年度总周数:getMaxWeekNumOfYear = " + getMaxWeekNumOfYear(year));
        System.out.println("本周第一天:getFirstDayOfWeek = " + sdf.format(getFirstDayOfWeek(year, 0)));
        System.out.println("本周最后一天:getLastDayOfWeek = " + sdf.format(getLastDayOfWeek(year, 0)));
        System.out.println("本周第一天:getFirstDayOfWeek = " + sdf.format(getFirstDayOfWeek(today)));
        System.out.println("本周最后一天:getLastDayOfWeek = " + sdf.format(getLastDayOfWeek(today)));
        System.out.println("1月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 1)));
        System.out.println("2月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 2)));
        System.out.println("3月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 3)));
        System.out.println("4月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 4)));
        System.out.println("5月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 5)));
        System.out.println("6月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 6)));
        System.out.println("7月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 7)));
        System.out.println("8月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 8)));
        System.out.println("9月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 9)));
        System.out.println("10月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 10)));
        System.out.println("11月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 11)));
        System.out.println("12月份最后一天：" + sdf.format(getLastDayOfMonth(2016, 12)));

    }
}
