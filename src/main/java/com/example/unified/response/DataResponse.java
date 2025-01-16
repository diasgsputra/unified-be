package com.example.unified.response;

import lombok.Data;


@Data
public class DataResponse {
  private String message;
  private DataSourceResponse data;
  public DataResponse(String message, DataSourceResponse data){
    this.message = message;
    this.data = data;
  }
}
