package com.wahook_java.wahook.Service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.wahook_java.wahook.Configuration.DBConfig;
import com.wahook_java.wahook.Configuration.WahookDBConfig;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
// import java.util.HashMap;
import java.util.LinkedHashMap;
// import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import queryResult
// import com.wahook_java.wahook.Model.QueryResult;


/**
 * Ini adalah contoh untuk setting multiple database
 * Tahapan:
 * 1. Tambahkan config DB ke application.properties
 * 2. Setting cara getter dan setter di folder Configuration (contoh tersedia)
 * 3. Setting method query di sini (contoh tersedia)
 */


@Repository
@Component
public class DBService {
    
    // private MariaDbDataSource dataSource1, dataSource2; //untuk multiple database
    // private MariaDbDataSource dataSource2;
    private HikariDataSource dataSource1, dataSource2;
    int cpuCount = Runtime.getRuntime().availableProcessors();

    @Autowired
    public DBService(DBConfig dbConfig, WahookDBConfig  wahookDBConfig) {

        HikariConfig dataSource1 = new HikariConfig();
        HikariConfig dataSource2 = new HikariConfig();

        
        // this.dataSource2 = new MariaDbDataSource();
        try {
            // System.err.println("DBConfig: " + dbConfig.getUrl());
            // System.err.println("DBConfig: " + dbConfig.getUsername());
            // System.err.println("DBConfig: " + dbConfig.getPassword());
            
            
            /**
             * Configurasi MainDB 
             */
            dataSource1.setJdbcUrl(dbConfig.getUrl());
            dataSource1.setUsername(dbConfig.getUsername());
            dataSource1.setPassword(dbConfig.getPassword());
            dataSource1.setMaximumPoolSize(1);
            this.dataSource1 = new HikariDataSource(dataSource1);
            

            /**
             * Configurasi secondary database
             */

    
            dataSource2.setJdbcUrl(wahookDBConfig.getUrl());
            dataSource2.setUsername(wahookDBConfig.getUsername());
            dataSource2.setPassword(wahookDBConfig.getPassword());
            dataSource2.setMaximumPoolSize(1);
            this.dataSource2 = new HikariDataSource(dataSource2);

        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    /**
     * Query Lama
     */
    @SuppressWarnings("unchecked")
    public <T> T query(String query)  {
        List<Map<String, Object>> result = new ArrayList<>();
        // List<QueryResult> result = new ArrayList<>();
        Connection connection = null;
        Map<String, Object> row = new LinkedHashMap<>();
        try {
            
            connection = dataSource1.getConnection();
            
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            

            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                row.clear();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue == null ? "NULL" : columnValue);
                }
                result.add(new LinkedHashMap<>(row));
            }

        } catch (Exception e) {
            return (T) ("Error closing connection: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                return (T) ("Error closing connection: " + e.getMessage());
            }
        }

        return (T) result;
    }
    
    /**
     * 
     * Query Transaksional hanya untuk insert, update, delete
     * bukan untuk select
     * 
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Object> queryTransaksional(List<String> listSql){
        List<Object> results = new ArrayList<>();
        Connection connection = null;

        try {
            connection = dataSource1.getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            for (String sql : listSql) {
                int affectedRows = statement.executeUpdate(sql);
                results.add(Map.of("affectedRows", affectedRows));
            }

            connection.commit();
            
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Rollback successful");
                } catch (SQLException ex) {
                    System.out.println("Error occurred during rollback: " + ex.getMessage());
                }
            }
            
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
        return results;
    }

    
    //This code is for exceute secondary database
    @SuppressWarnings("unchecked")
    public <T> T query_wahook(String query){
        List<Map<String, Object>> result = new ArrayList<>();
        // List<QueryResult> result = new ArrayList<>();
        Connection connection = null;
        try {

            //Pastekan datasource disini
            //===============================================================
            connection = dataSource2.getConnection();
            //===============================================================

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> row = new LinkedHashMap<>();

            while (resultSet.next()) {
                row.clear();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue == null ? "NULL" : columnValue);
                }
                result.add(new LinkedHashMap<>(row));
            }

        } catch (Exception e) {
            return (T) ("Error closing connection: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                return (T) ("Error closing connection: " + e.getMessage());
            }
        }

        return (T) result;
    }

    /**
     * Async version of the query method
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(cpuCount); // Adjust the pool size as needed
    public CompletableFuture<Object> queryAsync(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<Map<String, Object>> result = new ArrayList<>();
            Connection connection = null;

            try {
                connection = dataSource1.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                java.sql.ResultSetMetaData metaData =  resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
                        row.put(columnName, columnValue == null ? "NULL" : columnValue);
                    }
                    result.add(row);
                }

            } catch (Exception e) {
                // Handle the exception (you might want to log it or propagate it)
                e.printStackTrace();
                throw new RuntimeException("Error executing query: " + e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    // Handle the exception (you might want to log it or propagate it)
                    e.printStackTrace();
                    throw new RuntimeException("Error closing connection: " + e.getMessage());
                }
            }

            return result;
        }, executorService);
    }

    
    private final ExecutorService executorWahookService = Executors.newFixedThreadPool(cpuCount); // Adjust the pool size as needed
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> queryAsyncWahook(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<Map<String, Object>> result = new ArrayList<>();
            Connection connection = null;

            try {
                connection = dataSource2.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                java.sql.ResultSetMetaData metaData =  resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
                        row.put(columnName, columnValue == null ? "NULL" : columnValue);
                    }
                    result.add(row);
                }

            } catch (Exception e) {
                // Handle the exception (you might want to log it or propagate it)
                e.printStackTrace();
                throw new RuntimeException("Error executing query: " + e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    // Handle the exception (you might want to log it or propagate it)
                    e.printStackTrace();
                    throw new RuntimeException("Error closing connection: " + e.getMessage());
                }
            }

            return (T) result;
        }, executorWahookService);
    }
}
