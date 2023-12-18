package com.wahook_java.wahook.Model;
import java.util.Map;
import java.util.LinkedHashMap;

public class QueryResult {
    private final Map<String, Object> row;

    public QueryResult() {
        this.row = new LinkedHashMap<>();
    }

    public void put(String columnName, Object columnValue) {
        row.put(columnName, columnValue == null ? "NULL" : columnValue);
    }

    public Map<String, Object> getRow() {
        return row;
    }
}
