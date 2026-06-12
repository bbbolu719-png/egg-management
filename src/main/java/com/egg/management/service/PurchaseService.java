 package com.egg.management.service;

 import com.egg.management.dto.PurchaseRequest;

 import java.util.Map;

 public interface PurchaseService {
     Long createPurchase(PurchaseRequest req);
     void updatePurchase(Long id, PurchaseRequest req);
     void deletePurchase(Long id);
     Map<String, Object> getPurchase(Long id);
     Map<String, Object> listPurchases(int page, int pageSize);
    void markAsPaid(Long id);
 }
