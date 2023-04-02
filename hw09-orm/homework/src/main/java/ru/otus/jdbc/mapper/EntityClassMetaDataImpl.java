package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    Class<T> objectClass;

    public EntityClassMetaDataImpl(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public String getName() {
        return objectClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return (Constructor<T>) Arrays.stream(objectClass.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .get();
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(objectClass.getDeclaredFields())
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
    }
}
