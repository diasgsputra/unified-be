package com.example.unified.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class DatabaseConnectionRequest {
  private String name;
  private String url;
  private String username;
  private String password;
  @JsonProperty(value = "database_type")
  private String databaseType;

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
  public String getDatabaseType() {
    return databaseType;
  }
}
