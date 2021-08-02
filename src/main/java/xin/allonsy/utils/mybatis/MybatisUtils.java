package xin.allonsy.utils.mybatis;

import ch.qos.logback.classic.ClassicConstants;

public class MybatisUtils {

    /**
     * 开启mybatis debug日志打印完整sql
     * 需要在spring启动流程尽早调用 否则可能不生效
     *
     * @param mapperPackage   mapper包路径
     */
    public static void logCompleteSql(String mapperPackage) {
        CustomDefaultContextSelector.setMapperPackage(mapperPackage);
        Class<CustomDefaultContextSelector> contextSelectorClass = CustomDefaultContextSelector.class;
        // 这里设置环境变量，指向自定义的class
        System.setProperty(ClassicConstants.LOGBACK_CONTEXT_SELECTOR, contextSelectorClass.getName());
    }

}
