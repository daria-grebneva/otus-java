package ru.otus.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    try {
                        List<Object> m = new ArrayList<>();
                        m.add(rs.getLong(getIdFieldName()));
                        m.addAll(getFieldsValuesFromFromDb(rs));
                        return ((Constructor<T>) entityClassMetaData.getConstructor()).newInstance(m.toArray());
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objectList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    T obj = (T) entityClassMetaData.getConstructor().newInstance(rs.getLong(getIdFieldName()), getFieldsValuesFromFromDb(rs));
                    objectList.add(obj);
                }
                return objectList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    getFieldsValuesFromObject(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    List.of(getFieldsValuesFromObject(client), client.getClass().getField(getIdFieldName())));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private String getIdFieldName() {
        return entityClassMetaData.getIdField().getName();
    }

    private List<String> getFieldsValuesFromFromDb(ResultSet rs) {
        List<Field> fieldList = entityClassMetaData.getFieldsWithoutId();
        return fieldList.stream().map(Field::getName).map(l -> {
            try {
                String res = rs.getString(l);
                return res.isEmpty() ? " " : rs.getString(l);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private List<Object> getFieldsValuesFromObject(T client) {
        var valuesList = new ArrayList<>();
        List<Field> fieldList = entityClassMetaData.getFieldsWithoutId();
        var methods = client.getClass().getDeclaredMethods();

        for (Field f : fieldList) {
            Method method = Arrays.stream(methods).filter(m -> m.getName().toLowerCase().contains(f.getName())).findFirst().get();
            try {
                Object obj = method.invoke(client);
                valuesList.add(obj != null ? obj : "");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return valuesList;
    }
}
