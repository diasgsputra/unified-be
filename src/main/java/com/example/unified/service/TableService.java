package com.example.unified.service;

import com.example.unified.request.TableRequest;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {
public String createTable(TableRequest request) {
  if ("mysql".equalsIgnoreCase(request.getDatabaseType())) {
    // Handle MySQL
    DataSource dataSource = DataSourceBuilder.create()
        .url(request.getUrl()) // URL should not include the database name
        .username(request.getUsername())
        .password(request.getPassword())
        .build();

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {

      // Use the database name
      statement.execute("USE " + request.getDatabaseName());
      statement.execute(request.getQuery());
      return "Table " + request.getTableName() + " created successfully in MySQL database " + request.getDatabaseName() + ".";
    } catch (Exception e) {
      return "Failed to create table in MySQL: " + e.getMessage();
    }
  } else if ("mongodb".equalsIgnoreCase(request.getDatabaseType())) {
    // Handle MongoDB
    if (request.getDatabaseName() == null || request.getDatabaseName().isEmpty()) {
      throw new IllegalArgumentException("MongoDB database name must be provided.");
    }

    try (MongoClient mongoClient = MongoClients.create(request.getUrl())) {
      MongoDatabase database = mongoClient.getDatabase(request.getDatabaseName());

      // Create a collection
      database.createCollection(request.getTableName());
      return "Collection (table) " + request.getTableName() + " created successfully in MongoDB database " + request.getDatabaseName() + ".";
    } catch (Exception e) {
      return "Failed to create collection in MongoDB: " + e.getMessage();
    }
  } else {
    throw new IllegalArgumentException("Unsupported database type: " + request.getDatabaseType());
  }
}

  public List<String> listTables(TableRequest tableRequest) {
    List<String> tables = new ArrayList<>();

    if ("mysql".equalsIgnoreCase(tableRequest.getDatabaseType())) {
      // Handle MySQL
      DataSource dataSource = DataSourceBuilder.create()
          .url(tableRequest.getUrl()) // URL should not include the database name
          .username(tableRequest.getUsername())
          .password(tableRequest.getPassword())
          .build();

      try (Connection connection = dataSource.getConnection()) {
        // Use the provided database name
        ResultSet resultSet = connection.getMetaData().getTables(tableRequest.getDatabaseName(), null, "%", new String[]{"TABLE"});

        // Extract table names
        while (resultSet.next()) {
          tables.add(resultSet.getString("TABLE_NAME"));
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to list tables in MySQL: " + e.getMessage(), e);
      }
    } else if ("mongodb".equalsIgnoreCase(tableRequest.getDatabaseType())) {
      // Handle MongoDB
      if (tableRequest.getDatabaseName() == null || tableRequest.getDatabaseName().isEmpty()) {
        throw new IllegalArgumentException("MongoDB database name must be provided.");
      }

      try (MongoClient mongoClient = MongoClients.create(tableRequest.getUrl())) {
        MongoDatabase database = mongoClient.getDatabase(tableRequest.getDatabaseName());

        // Add all collection names
        for (String collectionName : database.listCollectionNames()) {
          tables.add(collectionName);
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to list collections in MongoDB: " + e.getMessage(), e);
      }
    } else {
      throw new IllegalArgumentException("Unsupported database type: " + tableRequest.getDatabaseType());
    }

    return tables;
  }
}
