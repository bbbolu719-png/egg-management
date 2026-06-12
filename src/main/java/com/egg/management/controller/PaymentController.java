 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Payment;
 import com.egg.management.mapper.PaymentMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/payments")
 public class PaymentController {

     @Autowired
     private PaymentMapper paymentMapper;

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
         paymentMapper.updateById(payment);
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Payment existing = paymentMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("付款记录不存在");
         }
         paymentMapper.deleteById(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
