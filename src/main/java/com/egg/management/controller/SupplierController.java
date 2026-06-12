 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Supplier;
 import com.egg.management.mapper.SupplierMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;

 @RestController
 @RequestMapping("/api/suppliers")
 public class SupplierController {

     @Autowired
     private SupplierMapper supplierMapper;

     @GetMapping
     public ApiResponse<List<Supplier>> list(@RequestParam(required = false) String search) {
         LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<Supplier>()
                 .orderByDesc(Supplier::getId);
         if (search != null && !search.isEmpty()) {
             wrapper.like(Supplier::getName, search)
                     .or(w -> w.like(Supplier::getPhone, search));
         }
         return ApiResponse.ok(supplierMapper.selectList(wrapper));
     }

     @GetMapping("/{id}")
     public ApiResponse<Supplier> get(@PathVariable Long id) {
         Supplier supplier = supplierMapper.selectById(id);
         if (supplier == null) {
             throw new RuntimeException("供应商不存在");
         }
         return ApiResponse.ok(supplier);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody Supplier supplier) {
         if (supplier.getName() == null || supplier.getName().isEmpty()) {
             throw new RuntimeException("名称不能为空");
         }
         supplierMapper.insert(supplier);
         return ApiResponse.ok(java.util.Map.of("id", supplier.getId()));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody Supplier supplier) {
         Supplier existing = supplierMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("供应商不存在");
         }
         supplier.setId(id);
         supplierMapper.updateById(supplier);
         return ApiResponse.ok(java.util.Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Supplier existing = supplierMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("供应商不存在");
         }
         supplierMapper.deleteById(id);
         return ApiResponse.ok(java.util.Map.of("id", id));
     }
 }
