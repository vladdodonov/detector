package com.dodonov.detector.util.listener;

import com.dodonov.detector.util.annotation.PostInitialize;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

//@Component
@RequiredArgsConstructor
public class PostInitInvokerListener implements ApplicationListener<ContextRefreshedEvent> {
    private final ConfigurableListableBeanFactory factory;

    @Override
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var context = event.getApplicationContext();
        for (String name : context.getBeanDefinitionNames()) {
            var beanDefinition = factory.getBeanDefinition(name);
            var beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName == null) {
                continue;
            }
            var originalBeanClass = Class.forName(beanClassName);
            for (Method method : originalBeanClass.getMethods()) {
                if (method.isAnnotationPresent(PostInitialize.class)) {
                    var bean = context.getBean(name);
                    var proxyClass = bean.getClass();
                    var proxyClassMethod = proxyClass.getMethod(method.getName());
                    proxyClassMethod.invoke(bean);
                }
            }
        }
    }
}
