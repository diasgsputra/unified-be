package com.example.unified.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableRequest {
  private String name;
  private String url;
  private String username;
  private String password;
  private String query;
  @JsonProperty(value = "database_name")
  private String databaseName;
  @JsonProperty(value = "database_type")
  private String databaseType;
  @JsonProperty(value = "table_name")
  private String tableName;

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getQuery() {
    return query;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public String getDatabaseType() {
    return databaseType;
  }

  public String getTableName() {
    return tableName;
  }
}
