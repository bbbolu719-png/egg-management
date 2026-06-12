 package com.egg.management.service;

 import java.util.List;
 import java.util.Map;

 public interface InventoryService {
     List<Map<String, Object>> getStock();
     Map<String, Object> getMovements(Long qualityId);
 }
