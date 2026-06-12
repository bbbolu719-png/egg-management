 package com.egg.management.controller;

 import com.egg.management.common.ApiResponse;
 import com.egg.management.service.DashboardService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;

 import java.util.Map;

 @RestController
 @RequestMapping("/api/dashboard")
 public class DashboardController {

     @Autowired
     private DashboardService dashboardService;

     @GetMapping
     public ApiResponse<Map<String, Object>> getDashboard() {
         return ApiResponse.ok(dashboardService.getDashboard());
     }
 }
