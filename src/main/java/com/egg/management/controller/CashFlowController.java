 package com.egg.management.controller;

 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.egg.management.common.ApiResponse;
 import com.egg.management.entity.CashFlow;
 import com.egg.management.service.CashFlowService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;

 import java.util.Map;

 @RestController
 @RequestMapping("/api/cashflow")
 public class CashFlowController {

     @Autowired
     private CashFlowService cashFlowService;

     @GetMapping
     public ApiResponse<?> list(@RequestParam(required = false) String type,
                                @RequestParam(required = false) String start,
                                @RequestParam(required = false) String end,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
         IPage<CashFlow> result = cashFlowService.list(type, start, end, page, pageSize);
         Map<String, Object> map = new java.util.LinkedHashMap<>();
         map.put("data", result.getRecords());
         map.put("total", result.getTotal());
         map.put("page", (int) result.getCurrent());
         map.put("pageSize", (int) result.getSize());
         return ApiResponse.ok(map);
     }

     @GetMapping("/summary")
     public ApiResponse<Map<String, Object>> summary(@RequestParam(required = false) String start,
                                                      @RequestParam(required = false) String end) {
         return ApiResponse.ok(cashFlowService.summary(start, end));
     }
 }
