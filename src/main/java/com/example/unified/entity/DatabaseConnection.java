package com.example.unified.entity;

import com.example.unified.request.DatabaseConnectionRequest;
import lombok.Data;

@Data
public class DatabaseConnection {
  private String name;
  private String url;
  private String username;
  private String password;

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

  public DatabaseConnection(DatabaseConnectionRequest d){
    this.name = d.getName();
    this.url = d.getUrl();
    this.username = d.getUsername();
    this.password = d.getPassword();
  }
}
