 package com.egg.management.service;

 import com.egg.management.dto.SaleRequest;

 import java.math.BigDecimal;
 import java.util.Map;

 public interface SaleService {
     Long createSale(SaleRequest req);
     void updateSale(Long id, SaleRequest req);
     void deleteSale(Long id);
     Map<String, Object> getSale(Long id);
     Map<String, Object> listSales(int page, int pageSize);
     Map<String, Object> recordReceipt(Long id, BigDecimal amount, String receiptDate);
     void closeReceipt(Long id);
 }
