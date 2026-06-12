 package com.egg.management.controller;

 import com.egg.management.common.ApiResponse;
 import com.egg.management.dto.SaleRequest;
 import com.egg.management.service.SaleService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.Map;

 @RestController
 @RequestMapping("/api/sales")
 public class SaleController {

     @Autowired
     private SaleService saleService;

     @GetMapping
     public ApiResponse<?> list(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
         return ApiResponse.ok(saleService.listSales(page, pageSize));
     }

     @GetMapping("/{id}")
     public ApiResponse<?> get(@PathVariable Long id) {
         Map<String, Object> sale = saleService.getSale(id);
         if (sale == null) {
             throw new RuntimeException("销售单不存在");
         }
         return ApiResponse.ok(sale);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody SaleRequest req) {
         if (req.getCustomerId() == null || req.getSaleDate() == null
                 || req.getItems() == null || req.getItems().isEmpty()) {
             throw new RuntimeException("缺少必填字段");
         }
         Long id = saleService.createSale(req);
         return ApiResponse.ok(Map.of("id", id));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody SaleRequest req) {
         saleService.updateSale(id, req);
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")

    @PostMapping("/{id}/receive")
    public ApiResponse<?> markReceived(@PathVariable Long id) {
        saleService.markAsReceived(id);
        return ApiResponse.ok(Map.of("id", id));
    }
     public ApiResponse<?> delete(@PathVariable Long id) {
         saleService.deleteSale(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
