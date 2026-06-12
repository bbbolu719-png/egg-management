 package com.egg.management.service.impl;

 import com.egg.management.mapper.InventoryMapper;
 import com.egg.management.service.InventoryService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 @Service
 public class InventoryServiceImpl implements InventoryService {

     @Autowired
     private InventoryMapper inventoryMapper;

     @Override
     public List<Map<String, Object>> getStock() {
         return inventoryMapper.selectStock();
     }

     @Override
     public Map<String, Object> getMovements(Long qualityId) {
         List<Map<String, Object>> purchases = inventoryMapper.selectPurchaseMovements(qualityId);
         List<Map<String, Object>> sales = inventoryMapper.selectSaleMovements(qualityId);
         Map<String, Object> result = new HashMap<>();
         result.put("purchases", purchases);
         result.put("sales", sales);
         return result;
     }
 }
