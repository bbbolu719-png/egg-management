 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.Customer;
 import com.egg.management.mapper.CustomerMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/customers")
 public class CustomerController {

     @Autowired
     private CustomerMapper customerMapper;

     @GetMapping
     public ApiResponse<List<Customer>> list(@RequestParam(required = false) String search) {
         LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>()
                 .orderByDesc(Customer::getId);
         if (search != null && !search.isEmpty()) {
             wrapper.like(Customer::getName, search)
                     .or(w -> w.like(Customer::getPhone, search));
         }
         return ApiResponse.ok(customerMapper.selectList(wrapper));
     }

     @GetMapping("/{id}")
     public ApiResponse<Customer> get(@PathVariable Long id) {
         Customer customer = customerMapper.selectById(id);
         if (customer == null) {
             throw new RuntimeException("客户不存在");
         }
         return ApiResponse.ok(customer);
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody Customer customer) {
         if (customer.getName() == null || customer.getName().isEmpty()) {
             throw new RuntimeException("名称不能为空");
         }
         customerMapper.insert(customer);
         return ApiResponse.ok(Map.of("id", customer.getId()));
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody Customer customer) {
         Customer existing = customerMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("客户不存在");
         }
         customer.setId(id);
         customerMapper.updateById(customer);
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         Customer existing = customerMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("客户不存在");
         }
         customerMapper.deleteById(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
