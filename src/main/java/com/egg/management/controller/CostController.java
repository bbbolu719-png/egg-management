package com.egg.management.controller;

import com.egg.management.common.ApiResponse;
import com.egg.management.entity.Cost;
import com.egg.management.mapper.CostMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/costs")
public class CostController {

    @Autowired
    private CostMapper costMapper;

    @GetMapping
    public ApiResponse<?> list() {
        List<Cost> list = costMapper.selectList(
            new LambdaQueryWrapper<Cost>().orderByDesc(Cost::getCostDate).orderByDesc(Cost::getId)
        );
        return ApiResponse.ok(list);
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody Cost cost) {
        costMapper.insert(cost);
        return ApiResponse.ok(Map.of("id", cost.getId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody Cost cost) {
        cost.setId(id);
        costMapper.updateById(cost);
        return ApiResponse.ok(Map.of("id", id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        costMapper.deleteById(id);
        return ApiResponse.ok(Map.of("id", id));
    }
}
