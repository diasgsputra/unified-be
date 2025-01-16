package com.example.unified.response;

import com.example.unified.entity.DatabaseConnection;
import lombok.Data;

@Data
public class DataSourceResponse {
  private String name;
  private String url;
  private String username;
  private String password;
  public DataSourceResponse(DatabaseConnection c){
    this.name = c.getName();
    this.url = c.getUrl();
    this.username = c.getUsername();
    this.password = c.getPassword();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DataSourceResponse() {
    // Constructor default untuk menangani null values
  }
}
