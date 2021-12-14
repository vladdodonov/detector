package com.dodonov.detector.config.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.NameTokenizer;
import org.modelmapper.spi.NameableType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Конфигурация ModelMapper
 */
@Component
@Slf4j
public class MapperConfig implements FactoryBean<ModelMapper> {
    /**
     * Список конфигов маппера
     */
    private List<MapperConfigurer> configurers;
    private List<MapperConfigurer> accepted = new ArrayList<>();
    private Environment env;
    private ModelMapper modelMapper;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired(required = false)
    public void setConfigurers(List<MapperConfigurer> configurers) {
        this.configurers = configurers;
    }

    /**
     * Создает маппер. Проходит по всем конфигам, передает им текущий инстанц маппера для конфигурирования
     */
    @Override
    public ModelMapper getObject() {
        this.modelMapper = new ModelMapper();

        String profile = "default";
        if (env != null && env.getActiveProfiles().length > 0) {
            profile = env.getActiveProfiles()[0];
        }

        switch (profile) {
            case "dev":
            case "test":
                CompletableFuture.runAsync(config);
                break;
            default:
                config.run();
        }
        return modelMapper;
    }

    private Runnable config = () -> {
        log.info("Initialize ModelMapper");
        long start = System.currentTimeMillis();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSourceNameTokenizer(new CustomCamelCaseNameTokenizer());
        modelMapper.getConfiguration().setDestinationNameTokenizer(new CustomCamelCaseNameTokenizer());

        if (configurers != null) {
            configurers.forEach(c -> acceptConfig(modelMapper, c));
        }
        log.info("ModelMapper has been initialized ({}ms)", System.currentTimeMillis() - start);
    };

    private void acceptConfig(ModelMapper modelMapper, @Nullable MapperConfigurer configurer) {
        if (configurer != null && !accepted.contains(configurer)) {
            accepted.add(configurer);
            com.dodonov.detector.config.mapper.annotation.MapperConfig mapperConfig = configurer.getClass().getAnnotation(com.dodonov.detector.config.mapper.annotation.MapperConfig.class);
            if (mapperConfig != null && mapperConfig.uses().length > 0) {
                Stream.of(mapperConfig.uses()).forEach(c -> acceptConfig(modelMapper, getByClass(c)));
            }
            configurer.configure(modelMapper);
        }
    }

    @Nullable
    private MapperConfigurer getByClass(Class<? extends MapperConfigurer> clazz) {
        return configurers.stream().filter(c -> c.getClass().equals(clazz)).findFirst().orElse(null);
    }

    @Override
    public Class<?> getObjectType() {
        return ModelMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Стандартный NameTokenizer медленный так как использует java.util.regex.Pattern
     */
    private static class CustomCamelCaseNameTokenizer implements NameTokenizer {
        public String[] tokenize(String name, NameableType nameableType) {
            if (name.isEmpty()) return new String[]{""};
            return StringUtils.splitByCharacterTypeCamelCase(name);
        }

        @Override
        public String toString() {
            return "Custom Camel Case";
        }
    }
}
