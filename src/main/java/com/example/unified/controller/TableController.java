package com.example.unified.controller;

import com.example.unified.request.TableRequest;
import com.example.unified.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {
  @Autowired
  private TableService tableService;

  @PostMapping
  public String createTable(
      @RequestBody TableRequest tableRequest
  ) {
    try {
      return tableService.createTable(tableRequest);
    } catch (Exception e) {
      return "Failed to create table: " + e.getMessage();
    }
  }

@GetMapping
public List<String> listTables(
@RequestBody TableRequest tableRequest
) {
  try {
    return tableService.listTables(tableRequest);
  } catch (Exception e) {
    throw new RuntimeException("Failed to list tables: " + e.getMessage(), e);
  }
}
}
