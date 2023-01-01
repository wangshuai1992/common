package xin.allonsy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext context;

    private static Environment environment;

//    private static String env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment env) {
        environment = env;
    }

    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static String getProperty(String key){
        return environment.getProperty(key);
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
