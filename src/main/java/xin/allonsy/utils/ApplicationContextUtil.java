package xin.allonsy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

//    private static String env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static String getProperty(String key){
        return context.getEnvironment().getProperty(key);
    }

//    @Value("${spring.profiles.active}")
//    public void setEnv(String env) {
//        ApplicationContextUtil.env = env;
//    }
//
//    public static String getEnv(){
//        return env;
//    }
}
