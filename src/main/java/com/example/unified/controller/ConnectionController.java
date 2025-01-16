package com.example.unified.controller;

import com.example.unified.request.DatabaseConnectionRequest;
import com.example.unified.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
  @Autowired
  private ConnectionService connectionService;

  @PostMapping(value="/create")
  public String createDatabase( @RequestBody DatabaseConnectionRequest connection) {
    return connectionService.createDatabase(connection);
  }

  @GetMapping("/list")
  public List<String> listDatabases(@RequestParam String url,
                                    @RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam(defaultValue = "com.mysql.cj.jdbc.Driver") String driverClass) {
    try {
      // Build the DataSource dynamically
      DataSource dataSource = DataSourceBuilder.create()
          .url(url)
          .username(username)
          .password(password)
          .driverClassName(driverClass)
          .build();

      // Call the service to list databases
      return connectionService.listDatabases(dataSource);
    } catch (Exception e) {
      throw new RuntimeException("Failed to list databases: " + e.getMessage(), e);
    }
  }
}
