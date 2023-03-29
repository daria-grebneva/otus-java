package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    EntityClassMetaData entityClassMetaDataObject;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaDataObject) {
        this.entityClassMetaDataObject = entityClassMetaDataObject;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaDataObject.getName().toLowerCase();
    }

    @Override
    public String getSelectByIdSql() {
        String idFieldName = entityClassMetaDataObject.getIdField().getName();
        return "select * from " + entityClassMetaDataObject.getName().toLowerCase() + " where " + idFieldName + " = ?";
    }

    @Override
    public String getInsertSql() {
        String className = entityClassMetaDataObject.getName().toLowerCase();
        List<Field> fieldList = entityClassMetaDataObject.getFieldsWithoutId();

        return "insert into " + className + "(" + getFieldsNamesWithDelimiter(fieldList, ",") + ")"
                + " values (?" + ",?".repeat(fieldList.size() - 1) + ")";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldList = entityClassMetaDataObject.getFieldsWithoutId();
        String idFieldName = entityClassMetaDataObject.getIdField().getName();

        return "update client set " + "(" + getFieldsNamesWithDelimiter(fieldList, " = ?,")
                + " = ? )" + " where " + idFieldName + " = ?";
    }

    private String getFieldsNamesWithDelimiter(List<Field> fieldList, String delimiter) {
        return fieldList.stream().map(Field::getName).collect(Collectors.joining(delimiter));
    }


}
