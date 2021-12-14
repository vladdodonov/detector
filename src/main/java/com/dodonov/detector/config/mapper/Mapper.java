package com.dodonov.detector.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Компонент-обертка для ModelMapper выполняет стандартные преобразования
 */
@Component
public class Mapper {

    private final ModelMapper mapperInstance;

    @Autowired
    public Mapper(ModelMapper mapper) {
        this.mapperInstance = mapper;
    }

    //Одиночное преобразование по классу
    public <S, T> T map(S source, Class<T> targetClass) {
        return mapperInstance.map(source, targetClass);
    }

    //Преобразование в переменную
    public <S, T> void mapTo(S source, T dist) {
        mapperInstance.map(source, dist);
    }

    //Преобразование Списка
    public <S, T> Collection<T> mapList(Collection<S> source, Class<T> targetClass) {
        return source.stream().map(t -> mapperInstance.map(t, targetClass)).collect(Collectors.toList());
    }

}
