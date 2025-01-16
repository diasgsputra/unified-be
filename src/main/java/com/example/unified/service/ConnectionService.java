package com.example.unified.service;

import com.example.unified.request.DatabaseConnectionRequest;
import com.example.unified.request.TableRequest;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionService {

  public String createDatabase(DatabaseConnectionRequest connection) {
    try {
      if ("mysql".equalsIgnoreCase(connection.getDatabaseType())) {
        // Handle MySQL Database Creation
        createMySqlDatabase(connection);
      } else if ("mongodb".equalsIgnoreCase(connection.getDatabaseType())) {
        // Handle MongoDB Database Creation
        createMongoDbDatabase(connection);
      } else {
        throw new IllegalArgumentException("Unsupported database type: " + connection.getDatabaseType());
      }
      String message =  "Database " + connection.getName() + " created successfully.";
      return message;

    } catch (Exception e) {
      String message = "Failed to create database: " + e.getMessage();
      return message;
    }
  }

  private void createMySqlDatabase(DatabaseConnectionRequest connection) throws Exception {
    DataSource dataSource = DataSourceBuilder.create()
        .url(connection.getUrl())
        .username(connection.getUsername())
        .password(connection.getPassword())
        .build();
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      String createDbSql = "CREATE DATABASE " + connection.getName();
      stmt.executeUpdate(createDbSql);
    }
  }

  private void createMongoDbDatabase(DatabaseConnectionRequest connection) {
    String uri = connection.getUrl();
    if (uri == null || uri.isEmpty()) {
      throw new IllegalArgumentException("MongoDB URI must be provided.");
    }
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase database = mongoClient.getDatabase(connection.getName());
      database.createCollection("default_collection");
    } catch (Exception e) {
      throw new RuntimeException("Failed to create MongoDB database: " + e.getMessage(), e);
    }
  }

//  public List<String> listDatabases(DataSource dataSource) {
//    List<String> databases = new ArrayList<>();
//
//    try (Connection connection = dataSource.getConnection();
//         ResultSet resultSet = connection.getMetaData().getCatalogs()) {
//
//      // Extract database names from the metadata
//      while (resultSet.next()) {
//        databases.add(resultSet.getString("TABLE_CAT"));
//      }
//    } catch (Exception e) {
//      throw new RuntimeException("Failed to list databases: " + e.getMessage(), e);
//    }
//
//    return databases;
//  }
public List<String> listDatabases(TableRequest tableRequest) {
  List<String> databases = new ArrayList<>();

  if ("mysql".equalsIgnoreCase(tableRequest.getDatabaseType())) {
    // Handle MySQL
    DataSource dataSource = DataSourceBuilder.create()
        .url(tableRequest.getUrl()) // URL should not include the database name
        .username(tableRequest.getUsername())
        .password(tableRequest.getPassword())
        .build();

    try (Connection connection = dataSource.getConnection()) {
      // Use the connection to get the list of databases
      ResultSet resultSet = connection.getMetaData().getCatalogs();

      // Extract database names
      while (resultSet.next()) {
        databases.add(resultSet.getString("TABLE_CAT"));
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to list databases in MySQL: " + e.getMessage(), e);
    }
  } else if ("mongodb".equalsIgnoreCase(tableRequest.getDatabaseType())) {
    // Handle MongoDB
    try (MongoClient mongoClient = MongoClients.create(tableRequest.getUrl())) {
      MongoIterable<String> mongoDatabases = mongoClient.listDatabaseNames();
      mongoDatabases.into(databases);
    } catch (Exception e) {
      throw new RuntimeException("Failed to list databases in MongoDB: " + e.getMessage(), e);
    }
  } else {
    throw new IllegalArgumentException("Unsupported database type: " + tableRequest.getDatabaseType());
  }

  return databases;
}


}
