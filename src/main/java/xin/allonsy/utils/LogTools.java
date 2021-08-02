package xin.allonsy.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.MDC;

public class LogTools {

    private static final String PROC_ID = "PROCID";
    private static final String PROC_ID_CHILD_ID = "PROCID_CHILD_ID";

    public LogTools() {}

    public static void addMarker() {
        MDC.put(PROC_ID, "[" + RandomStringUtils.randomAlphanumeric(10, 11) + "]");
        MDC.put(PROC_ID_CHILD_ID, "0");
    }

    public static void removeMarker() {
        MDC.remove(PROC_ID);
        MDC.remove(PROC_ID_CHILD_ID);
    }

    public static void addMarker(String marker) {
        if (StringUtils.isEmpty(marker)) {
            addMarker();
        } else {
            MDC.put(PROC_ID, marker);
            MDC.put(PROC_ID_CHILD_ID, "0");
        }
    }

    public static String getMarker() {
        return MDC.get(PROC_ID);
    }

    public static String getNextSubMarker() {
        int i = NumberUtils.toInt(MDC.get(PROC_ID_CHILD_ID), 0);
        i++;
        MDC.put(PROC_ID_CHILD_ID, String.valueOf(i));
        return StringUtils.replace(getMarker(), "]", "-" + i + "]");
    }

}
