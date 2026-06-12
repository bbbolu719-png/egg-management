 package com.egg.management.controller;

 import com.egg.management.common.ApiResponse;
 import com.egg.management.dto.PurchaseRequest;
 import com.egg.management.service.PurchaseService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.Map;

 @RestController
 @RequestMapping("/api/purchases")
 public class PurchaseController {

     @Autowired
     private PurchaseService purchaseService;

     @GetMapping
     public ApiResponse<?> list(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
         return ApiResponse.ok(purchaseService.listPurchases(page, pageSize));
     }

     @GetMapping("/{id}")
     public ApiResponse<?> get(@PathVariable Long id) {
         Map<String, Object> purchase = purchaseService.getPurchase(id);
         if (purchase == null) {
             throw new RuntimeException("采购单不存在");
         }
         return ApiResponse.ok(purchase);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody PurchaseRequest req) {
         if (req.getSupplierId() == null || req.getPurchaseDate() == null
                 || req.getItems() == null || req.getItems().isEmpty()) {
             throw new RuntimeException("缺少必填字段");
         }
         Long id = purchaseService.createPurchase(req);
         return ApiResponse.ok(Map.of("id", id));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody PurchaseRequest req) {
         purchaseService.updatePurchase(id, req);
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")

    @PostMapping("/{id}/pay")
    public ApiResponse<?> markPaid(@PathVariable Long id) {
        purchaseService.markAsPaid(id);
        return ApiResponse.ok(Map.of("id", id));
    }
     public ApiResponse<?> delete(@PathVariable Long id) {
         purchaseService.deletePurchase(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
