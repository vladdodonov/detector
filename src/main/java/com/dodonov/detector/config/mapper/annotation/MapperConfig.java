package com.dodonov.detector.config.mapper.annotation;

import com.dodonov.detector.config.mapper.MapperConfigurer;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MapperConfig {
    Class<? extends MapperConfigurer>[] uses() default {};
}
