package shanghai.shu.config;

import org.springframework.context.ApplicationContext;

public class SpringContextHolder {
    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext applicationContext){
        if (SpringContextHolder.applicationContext==null){
            SpringContextHolder.applicationContext=applicationContext;
        }
    }

    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }
    public static <T> T getBean(String name,Class<T> requiredType){
        return applicationContext.getBean(name,requiredType);
    }
}
