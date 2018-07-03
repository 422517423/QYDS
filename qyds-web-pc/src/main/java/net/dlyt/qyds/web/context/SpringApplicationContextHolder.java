package net.dlyt.qyds.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by dkzhang on 16/8/20.
 */
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware{
    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static Object getBean(String key){
        return ctx.getBean(key);
    }


}
