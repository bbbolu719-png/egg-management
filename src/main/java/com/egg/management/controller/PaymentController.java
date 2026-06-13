 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Payment;
 import com.egg.management.entity.Purchase;
 import com.egg.management.mapper.PaymentMapper;
 import com.egg.management.mapper.PurchaseMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.math.BigDecimal;
 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/payments")
 public class PaymentController {

     @Autowired
     private PaymentMapper paymentMapper;

     @Autowired
     private PurchaseMapper purchaseMapper;

     @GetMapping
     public ApiResponse<List<Payment>> list() {
         List<Payment> list = paymentMapper.selectList(
                 new LambdaQueryWrapper<Payment>().orderByDesc(Payment::getPaymentDate, Payment::getId));
         return ApiResponse.ok(list);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody Payment payment) {
         paymentMapper.insert(payment);
         return ApiResponse.ok(Map.of("id", payment.getId()));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody Payment payment) {
         Payment existing = paymentMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("付款记录不存在");
         }
         payment.setId(id);
         payment.setPurchaseId(existing.getPurchaseId());
         paymentMapper.updateById(payment);
         recalcPurchase(existing.getPurchaseId());
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Payment existing = paymentMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("付款记录不存在");
         }
         paymentMapper.deleteById(id);
         recalcPurchase(existing.getPurchaseId());
         return ApiResponse.ok(Map.of("id", id));
     }

     private void recalcPurchase(Long purchaseId) {
         if (purchaseId == null) return;
         List<Payment> payments = paymentMapper.selectList(
                 new LambdaQueryWrapper<Payment>().eq(Payment::getPurchaseId, purchaseId));
         BigDecimal totalPaid = payments.stream()
                 .map(p -> p.getAmount() == null ? BigDecimal.ZERO : p.getAmount())
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
         Purchase purchase = purchaseMapper.selectById(purchaseId);
         if (purchase == null || "closed".equals(purchase.getPaymentStatus())) return;
         purchase.setPaidAmount(totalPaid);
         if (totalPaid.compareTo(BigDecimal.ZERO) <= 0) {
             purchase.setPaymentStatus("unpaid");
         } else if (totalPaid.compareTo(purchase.getTotalAmount()) >= 0) {
             purchase.setPaymentStatus("paid");
         } else {
             purchase.setPaymentStatus("partial");
         }
         purchaseMapper.updateById(purchase);
     }
 }
