 package com.egg.management.service.impl;

 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import com.egg.management.dto.ItemRequest;
 import com.egg.management.dto.PurchaseRequest;
 import com.egg.management.entity.CashFlow;
 import com.egg.management.entity.Purchase;
 import com.egg.management.entity.PurchaseItem;
 import com.egg.management.mapper.*;
 import com.egg.management.service.PurchaseService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import java.math.BigDecimal;
 import java.time.LocalDate;
 import java.util.*;

 @Service
 public class PurchaseServiceImpl implements PurchaseService {

     @Autowired
     private PurchaseMapper purchaseMapper;

     @Autowired
     private PurchaseItemMapper purchaseItemMapper;

     @Autowired
     private CashFlowMapper cashFlowMapper;

     @Autowired
     private PurchaseMapperCustom purchaseMapperCustom;

     @Override
     @Transactional(rollbackFor = Exception.class)
     public Long createPurchase(PurchaseRequest req) {
         // Calculate total amount from items
         BigDecimal totalAmount = BigDecimal.ZERO;
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 totalAmount = totalAmount.add(item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
             }
         }

         // Insert purchase
        Purchase purchase = new Purchase();
        purchase.setSupplierId(req.getSupplierId());
        purchase.setPurchaseDate(LocalDate.parse(req.getPurchaseDate()));
        purchase.setTotalAmount(totalAmount);
        purchase.setQrImage(req.getQrImage());
        purchase.setPaymentStatus("unpaid");
        purchase.setNotes(req.getNotes() != null ? req.getNotes() : "");
        purchase.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
        purchaseMapper.insert(purchase);
         Long purchaseId = purchase.getId();

         // Insert items
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 PurchaseItem pi = new PurchaseItem();
                 pi.setPurchaseId(purchaseId);
                 pi.setQualityId(item.getQualityId());
                 pi.setQuantity(item.getQuantity());
                 pi.setUnitPrice(item.getUnitPrice());
                 pi.setSubtotal(item.getSubtotal());
                 purchaseItemMapper.insert(pi);
             }
         }

        // Insert cash flow
        BigDecimal totalCost = totalAmount;

        CashFlow cashFlow = new CashFlow();
         cashFlow.setFlowDate(LocalDate.parse(req.getPurchaseDate()));
         cashFlow.setType("expense");
         cashFlow.setAmount(totalCost);
         cashFlow.setCategory("purchase");
         cashFlow.setRefType("purchase");
         cashFlow.setRefId(purchaseId);
         cashFlow.setDescription("采购 #" + purchaseId);
         cashFlowMapper.insert(cashFlow);

         return purchaseId;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public void updatePurchase(Long id, PurchaseRequest req) {
         // Update purchase
         BigDecimal totalAmount = BigDecimal.ZERO;
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 totalAmount = totalAmount.add(item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
             }
         }

         Purchase purchase = new Purchase();
         purchase.setId(id);
         purchase.setSupplierId(req.getSupplierId());
         purchase.setPurchaseDate(LocalDate.parse(req.getPurchaseDate()));
        purchase.setTotalAmount(totalAmount);
        purchase.setQrImage(req.getQrImage());
        purchase.setPaymentStatus("unpaid");
        purchase.setNotes(req.getNotes() != null ? req.getNotes() : "");
        purchase.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
        purchaseMapper.updateById(purchase);

         // Delete old items and insert new ones
         purchaseItemMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PurchaseItem>()
                 .eq(PurchaseItem::getPurchaseId, id));

         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 PurchaseItem pi = new PurchaseItem();
                 pi.setPurchaseId(id);
                 pi.setQualityId(item.getQualityId());
                 pi.setQuantity(item.getQuantity());
                 pi.setUnitPrice(item.getUnitPrice());
                 pi.setSubtotal(item.getSubtotal());
                 purchaseItemMapper.insert(pi);
             }
         }
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
    public void markAsPaid(Long id) {
        Purchase purchase = new Purchase();
        purchase.setId(id);
        purchase.setPaymentStatus("paid");
        purchaseMapper.updateById(purchase);
    }

     public void deletePurchase(Long id) {
         purchaseItemMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PurchaseItem>()
                 .eq(PurchaseItem::getPurchaseId, id));
         purchaseMapper.deleteById(id);
     }

     @Override
     public Map<String, Object> getPurchase(Long id) {
         Map<String, Object> purchase = purchaseMapperCustom.selectPurchaseById(id);
         if (purchase == null) {
             return null;
         }
         List<Map<String, Object>> items = purchaseMapperCustom.selectPurchaseItems(id);
         purchase.put("items", items);
         return purchase;
     }

     @Override
     public Map<String, Object> listPurchases(int page, int pageSize) {
         Page<Map<String, Object>> pageObj = new Page<>(page, pageSize);
         IPage<Map<String, Object>> result = purchaseMapperCustom.selectPurchasePage(pageObj);

         // Attach items to each purchase
         List<Map<String, Object>> records = result.getRecords();
         for (Map<String, Object> record : records) {
             Long purchaseId = ((Number) record.get("id")).longValue();
             List<Map<String, Object>> items = purchaseMapperCustom.selectPurchaseItems(purchaseId);
             record.put("items", items);
         }

         Map<String, Object> map = new LinkedHashMap<>();
         map.put("data", records);
         map.put("total", result.getTotal());
         map.put("page", (int) result.getCurrent());
         map.put("pageSize", (int) result.getSize());
         return map;
     }
 }
