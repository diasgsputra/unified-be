package com.example.unified.controller;

import com.example.unified.constant.DatabaseTypeConstant;
import com.example.unified.request.DatabaseConnectionRequest;
import com.example.unified.request.TableRequest;
import com.example.unified.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/list")
  public List<String> listDatabases(@RequestBody TableRequest tableRequest) {
    try {
      return connectionService.listDatabases(tableRequest);
    } catch (Exception e) {
      throw new RuntimeException("Failed to list databases: " + e.getMessage(), e);
    }
  }

  @GetMapping("/database-types")
  public List<String> getDatabaseTypes() {
    return DatabaseTypeConstant.DATABASE_TYPES;
  }
}
