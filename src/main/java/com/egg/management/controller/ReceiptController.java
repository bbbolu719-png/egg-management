 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Receipt;
 import com.egg.management.entity.Sale;
 import com.egg.management.mapper.ReceiptMapper;
 import com.egg.management.mapper.SaleMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.math.BigDecimal;
 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/receipts")
 public class ReceiptController {

     @Autowired
     private ReceiptMapper receiptMapper;

     @Autowired
     private SaleMapper saleMapper;

     @GetMapping
     public ApiResponse<List<Receipt>> list() {
         List<Receipt> list = receiptMapper.selectList(
                 new LambdaQueryWrapper<Receipt>().orderByDesc(Receipt::getReceiptDate, Receipt::getId));
         return ApiResponse.ok(list);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody Receipt receipt) {
         receiptMapper.insert(receipt);
         return ApiResponse.ok(Map.of("id", receipt.getId()));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody Receipt receipt) {
         Receipt existing = receiptMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("收款记录不存在");
         }
         receipt.setId(id);
         receipt.setSaleId(existing.getSaleId());
         receiptMapper.updateById(receipt);
         recalcSale(existing.getSaleId());
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Receipt existing = receiptMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("收款记录不存在");
         }
         receiptMapper.deleteById(id);
         recalcSale(existing.getSaleId());
         return ApiResponse.ok(Map.of("id", id));
     }

     private void recalcSale(Long saleId) {
         if (saleId == null) return;
         List<Receipt> receipts = receiptMapper.selectList(
                 new LambdaQueryWrapper<Receipt>().eq(Receipt::getSaleId, saleId));
         BigDecimal totalReceived = receipts.stream()
                 .map(r -> r.getAmount() == null ? BigDecimal.ZERO : r.getAmount())
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
         Sale sale = saleMapper.selectById(saleId);
         if (sale == null || "closed".equals(sale.getReceiptStatus())) return;
         sale.setReceivedAmount(totalReceived);
         if (totalReceived.compareTo(BigDecimal.ZERO) <= 0) {
             sale.setReceiptStatus("unreceived");
         } else if (totalReceived.compareTo(sale.getTotalAmount()) >= 0) {
             sale.setReceiptStatus("received");
         } else {
             sale.setReceiptStatus("partial");
         }
         saleMapper.updateById(sale);
     }
 }
