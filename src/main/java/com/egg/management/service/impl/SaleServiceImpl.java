 package com.egg.management.service.impl;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import com.egg.management.dto.ItemRequest;
 import com.egg.management.dto.SaleRequest;
 import com.egg.management.entity.CashFlow;
 import com.egg.management.entity.Receipt;
 import com.egg.management.entity.Sale;
 import com.egg.management.entity.SaleItem;
 import com.egg.management.mapper.*;
 import com.egg.management.service.SaleService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import java.math.BigDecimal;
 import java.time.LocalDate;
 import java.util.*;

 @Service
 public class SaleServiceImpl implements SaleService {

     @Autowired
     private SaleMapper saleMapper;

     @Autowired
     private SaleItemMapper saleItemMapper;

     @Autowired
     private CashFlowMapper cashFlowMapper;

     @Autowired
     private ReceiptMapper receiptMapper;

     @Autowired
     private SaleMapperCustom saleMapperCustom;

     @Override
     @Transactional(rollbackFor = Exception.class)
     public Long createSale(SaleRequest req) {
         BigDecimal totalAmount = BigDecimal.ZERO;
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 totalAmount = totalAmount.add(item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
             }
         }

         Sale sale = new Sale();
         sale.setCustomerId(req.getCustomerId());
         sale.setSaleDate(LocalDate.parse(req.getSaleDate()));
         sale.setTotalAmount(totalAmount);
         sale.setReceivedAmount(BigDecimal.ZERO);
         sale.setNotes(req.getNotes() != null ? req.getNotes() : "");
         sale.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
         saleMapper.insert(sale);
         Long saleId = sale.getId();
         sale.setOrderNo("XS" + LocalDate.now().toString().replace("-", "") + String.format("%04d", saleId));
         saleMapper.updateById(sale);

         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 SaleItem si = new SaleItem();
                 si.setSaleId(saleId);
                 si.setQualityId(item.getQualityId());
                 si.setQuantity(item.getQuantity());
                 si.setUnitPrice(item.getUnitPrice());
                 si.setSubtotal(item.getSubtotal());
                 saleItemMapper.insert(si);
             }
         }

         CashFlow cashFlow = new CashFlow();
         cashFlow.setFlowDate(LocalDate.parse(req.getSaleDate()));
         cashFlow.setType("income");
         cashFlow.setAmount(totalAmount);
         cashFlow.setCategory("sale");
         cashFlow.setRefType("sale");
         cashFlow.setRefId(saleId);
         cashFlow.setDescription("销售 #" + saleId);
         cashFlowMapper.insert(cashFlow);

         return saleId;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public void updateSale(Long id, SaleRequest req) {
         BigDecimal totalAmount = BigDecimal.ZERO;
         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 totalAmount = totalAmount.add(item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
             }
         }

         Sale sale = new Sale();
         sale.setId(id);
         sale.setCustomerId(req.getCustomerId());
         sale.setSaleDate(LocalDate.parse(req.getSaleDate()));
         sale.setTotalAmount(totalAmount);
         sale.setNotes(req.getNotes() != null ? req.getNotes() : "");
         sale.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
         saleMapper.updateById(sale);

         // Recalculate receipt status based on current received_amount vs new total_amount
         Sale existing = saleMapper.selectById(id);
         if (!"closed".equals(existing.getReceiptStatus())) {
             String newStatus;
             if (existing.getReceivedAmount() == null || existing.getReceivedAmount().compareTo(BigDecimal.ZERO) <= 0) {
                 newStatus = "unreceived";
             } else if (existing.getReceivedAmount().compareTo(totalAmount) >= 0) {
                 newStatus = "received";
             } else {
                 newStatus = "partial";
             }
             Sale statusUpdate = new Sale();
             statusUpdate.setId(id);
             statusUpdate.setReceiptStatus(newStatus);
             saleMapper.updateById(statusUpdate);
         }

         saleItemMapper.delete(new LambdaQueryWrapper<SaleItem>()
                 .eq(SaleItem::getSaleId, id));

         if (req.getItems() != null) {
             for (ItemRequest item : req.getItems()) {
                 SaleItem si = new SaleItem();
                 si.setSaleId(id);
                 si.setQualityId(item.getQualityId());
                 si.setQuantity(item.getQuantity());
                 si.setUnitPrice(item.getUnitPrice());
                 si.setSubtotal(item.getSubtotal());
                 saleItemMapper.insert(si);
             }
         }
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public Map<String, Object> recordReceipt(Long id, BigDecimal amount, String receiptDate) {
         // Get sale to find customer_id
         Sale sale = saleMapper.selectById(id);

         // Insert receipt record
         Receipt receipt = new Receipt();
         receipt.setSaleId(id);
         receipt.setCustomerId(sale.getCustomerId());
         receipt.setAmount(amount);
         receipt.setReceiptDate(LocalDate.parse(receiptDate));
         receipt.setStatus("received");
         receiptMapper.insert(receipt);

         // Update received_amount
         BigDecimal newReceived = sale.getReceivedAmount().add(amount);
         sale.setReceivedAmount(newReceived);

         // Determine receipt status
         String status;
         if (newReceived.compareTo(sale.getTotalAmount()) >= 0) {
             status = "received";
         } else {
             status = "partial";
         }
         sale.setReceiptStatus(status);
         saleMapper.updateById(sale);

         // Insert cash flow
         CashFlow cashFlow = new CashFlow();
         cashFlow.setFlowDate(LocalDate.parse(receiptDate));
         cashFlow.setType("income");
         cashFlow.setAmount(amount);
         cashFlow.setCategory("receipt");
         cashFlow.setRefType("receipt");
         cashFlow.setRefId(receipt.getId());
         cashFlow.setDescription("销售收款 #" + sale.getId() + " 第" + receipt.getId() + "笔");
         cashFlowMapper.insert(cashFlow);

         Map<String, Object> result = new LinkedHashMap<>();
         result.put("received_amount", newReceived);
         result.put("receipt_status", status);
         result.put("receipt_id", receipt.getId());
         return result;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public void closeReceipt(Long id) {
         Sale sale = new Sale();
         sale.setId(id);
         sale.setReceiptStatus("closed");
         saleMapper.updateById(sale);
     }

     @Override
     public void deleteSale(Long id) {
         saleItemMapper.delete(new LambdaQueryWrapper<SaleItem>()
                 .eq(SaleItem::getSaleId, id));
         saleMapper.deleteById(id);
     }

     @Override
     public Map<String, Object> getSale(Long id) {
         Map<String, Object> sale = saleMapperCustom.selectSaleById(id);
         if (sale == null) {
             return null;
         }
         List<Map<String, Object>> items = saleMapperCustom.selectSaleItems(id);
         sale.put("items", items);
         List<Map<String, Object>> receipts = saleMapperCustom.selectReceipts(id);
         sale.put("receipts", receipts);
         return sale;
     }

     @Override
     public Map<String, Object> listSales(int page, int pageSize) {
         Page<Map<String, Object>> pageObj = new Page<>(page, pageSize);
         IPage<Map<String, Object>> result = saleMapperCustom.selectSalePage(pageObj);

         List<Map<String, Object>> records = result.getRecords();
         for (Map<String, Object> record : records) {
             Long saleId = ((Number) record.get("id")).longValue();
             List<Map<String, Object>> items = saleMapperCustom.selectSaleItems(saleId);
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
