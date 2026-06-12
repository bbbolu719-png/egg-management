 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Receipt;
 import com.egg.management.mapper.ReceiptMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/receipts")
 public class ReceiptController {

     @Autowired
     private ReceiptMapper receiptMapper;

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
         receiptMapper.updateById(receipt);
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Receipt existing = receiptMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("收款记录不存在");
         }
         receiptMapper.deleteById(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
