 package com.egg.management.controller;

 import com.egg.management.common.ApiResponse;
 import com.egg.management.service.InventoryService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/inventory")
 public class InventoryController {

     @Autowired
     private InventoryService inventoryService;

     @GetMapping
     public ApiResponse<List<Map<String, Object>>> getStock() {
         return ApiResponse.ok(inventoryService.getStock());
     }

     @GetMapping("/{qualityId}/movements")
     public ApiResponse<?> getMovements(@PathVariable Long qualityId) {
         return ApiResponse.ok(inventoryService.getMovements(qualityId));
     }
 }
