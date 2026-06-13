 package com.egg.management.service.impl;

 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import com.egg.management.dto.ItemRequest;
 import com.egg.management.dto.PurchaseRequest;
 import com.egg.management.entity.CashFlow;
 import com.egg.management.entity.Payment;
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
     private PaymentMapper paymentMapper;

     @Autowired
     private PurchaseMapperCustom purchaseMapperCustom;

     @Override
     @Transactional(rollbackFor = Exception.class)
     public Long createPurchase(PurchaseRequest req) {
         BigDecimal totalAmount = BigDecimal.ZERO;
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 totalAmount = totalAmount.add(item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
             }
         }

         Purchase purchase = new Purchase();
         purchase.setSupplierId(req.getSupplierId());
         purchase.setPurchaseDate(LocalDate.parse(req.getPurchaseDate()));
         purchase.setTotalAmount(totalAmount);
         purchase.setFuelCost(req.getFuelCost());
         purchase.setCrateCost(req.getCrateCost());
         purchase.setBagCost(req.getBagCost());
         purchase.setOtherCost(req.getOtherCost());
         purchase.setQrImage(req.getQrImage());
         purchase.setPaymentStatus("unpaid");
         purchase.setPaidAmount(BigDecimal.ZERO);
         purchase.setNotes(req.getNotes() != null ? req.getNotes() : "");
         purchase.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
         purchaseMapper.insert(purchase);
         Long purchaseId = purchase.getId();
         purchase.setOrderNo("CG" + LocalDate.now().toString().replace("-", "") + String.format("%04d", purchaseId));
         purchaseMapper.updateById(purchase);

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

         BigDecimal totalCost = totalAmount;
         if (req.getFuelCost() != null) totalCost = totalCost.add(req.getFuelCost());
         if (req.getCrateCost() != null) totalCost = totalCost.add(req.getCrateCost());
         if (req.getBagCost() != null) totalCost = totalCost.add(req.getBagCost());
         if (req.getOtherCost() != null) totalCost = totalCost.add(req.getOtherCost());

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
         purchase.setFuelCost(req.getFuelCost());
         purchase.setCrateCost(req.getCrateCost());
         purchase.setBagCost(req.getBagCost());
         purchase.setOtherCost(req.getOtherCost());
         purchase.setQrImage(req.getQrImage());
         purchase.setNotes(req.getNotes() != null ? req.getNotes() : "");
         purchase.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
         purchaseMapper.updateById(purchase);

         // Recalculate payment status based on current paid_amount vs new total_amount
         Purchase existing = purchaseMapper.selectById(id);
         if (!"closed".equals(existing.getPaymentStatus())) {
             String newStatus;
             if (existing.getPaidAmount() == null || existing.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
                 newStatus = "unpaid";
             } else if (existing.getPaidAmount().compareTo(totalAmount) >= 0) {
                 newStatus = "paid";
             } else {
                 newStatus = "partial";
             }
             Purchase statusUpdate = new Purchase();
             statusUpdate.setId(id);
             statusUpdate.setPaymentStatus(newStatus);
             purchaseMapper.updateById(statusUpdate);
         }

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
     public Map<String, Object> recordPayment(Long id, BigDecimal amount, String method, String paymentDate) {
         // Get purchase to find supplier_id
         Purchase purchase = purchaseMapper.selectById(id);

         // Insert payment record
         Payment payment = new Payment();
         payment.setPurchaseId(id);
         payment.setSupplierId(purchase.getSupplierId());
         payment.setAmount(amount);
         payment.setPaymentDate(LocalDate.parse(paymentDate));
         payment.setMethod(method != null ? method : "cash");
         payment.setStatus("paid");
         paymentMapper.insert(payment);

         // Update paid_amount
         BigDecimal newPaid = purchase.getPaidAmount().add(amount);
         purchase.setPaidAmount(newPaid);

         // Determine payment status
         String status;
         if (newPaid.compareTo(purchase.getTotalAmount()) >= 0) {
             status = "paid";
         } else {
             status = "partial";
         }
         purchase.setPaymentStatus(status);
         purchaseMapper.updateById(purchase);

         // Insert cash flow
         CashFlow cashFlow = new CashFlow();
         cashFlow.setFlowDate(LocalDate.parse(paymentDate));
         cashFlow.setType("expense");
         cashFlow.setAmount(amount);
         cashFlow.setCategory("payment");
         cashFlow.setRefType("payment");
         cashFlow.setRefId(payment.getId());
         cashFlow.setDescription("采购付款 #" + purchase.getId() + " 第" + payment.getId() + "笔");
         cashFlowMapper.insert(cashFlow);

         Map<String, Object> result = new LinkedHashMap<>();
         result.put("paid_amount", newPaid);
         result.put("payment_status", status);
         result.put("payment_id", payment.getId());
         return result;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public void closePayment(Long id) {
         Purchase purchase = new Purchase();
         purchase.setId(id);
         purchase.setPaymentStatus("closed");
         purchaseMapper.updateById(purchase);
     }

     @Override
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
         List<Map<String, Object>> payments = purchaseMapperCustom.selectPayments(id);
         purchase.put("payments", payments);
         return purchase;
     }

     @Override
     public Map<String, Object> listPurchases(int page, int pageSize) {
         Page<Map<String, Object>> pageObj = new Page<>(page, pageSize);
         IPage<Map<String, Object>> result = purchaseMapperCustom.selectPurchasePage(pageObj);

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
