 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.EggQuality;
 import com.egg.management.mapper.EggQualityMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.dao.DuplicateKeyException;
 import org.springframework.web.bind.annotation.*;

 import java.util.List;
 import java.util.Map;

 @RestController
 @RequestMapping("/api/qualities")
 public class EggQualityController {

     @Autowired
     private EggQualityMapper eggQualityMapper;

     @GetMapping
     public ApiResponse<List<EggQuality>> list() {
         return ApiResponse.ok(eggQualityMapper.selectList(
                 new LambdaQueryWrapper<EggQuality>().orderByAsc(EggQuality::getId)));
     }

     @PostMapping
     public ApiResponse<?> create(@RequestBody EggQuality quality) {
         try {
             eggQualityMapper.insert(quality);
             return ApiResponse.ok(Map.of("id", quality.getId()));
         } catch (DuplicateKeyException e) {
             throw new RuntimeException("品质名称已存在");
         }
     }

     @PutMapping("/{id}")
     public ApiResponse<?> update(@PathVariable Long id, @RequestBody EggQuality quality) {
         EggQuality existing = eggQualityMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("品质不存在");
         }
         quality.setId(id);
         try {
             eggQualityMapper.updateById(quality);
         } catch (DuplicateKeyException e) {
             throw new RuntimeException("品质名称已存在");
         }
         return ApiResponse.ok(Map.of("id", id));
     }

     @DeleteMapping("/{id}")
     public ApiResponse<?> delete(@PathVariable Long id) {
         EggQuality existing = eggQualityMapper.selectById(id);
         if (existing == null) {
             throw new RuntimeException("品质不存在");
         }
         eggQualityMapper.deleteById(id);
         return ApiResponse.ok(Map.of("id", id));
     }
 }
