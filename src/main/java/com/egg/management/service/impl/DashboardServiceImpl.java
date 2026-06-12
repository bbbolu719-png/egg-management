 package com.egg.management.service.impl;

 import com.egg.management.mapper.DashboardMapper;
 import com.egg.management.service.DashboardService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.util.HashMap;
 import java.util.Map;

 @Service
 public class DashboardServiceImpl implements DashboardService {

     @Autowired
     private DashboardMapper dashboardMapper;

     @Override
     public Map<String, Object> getDashboard() {
         Map<String, Object> todayIncome = dashboardMapper.selectTodayIncome();
         Map<String, Object> todayExpense = dashboardMapper.selectTodayExpense();
         Map<String, Object> balanceRow = dashboardMapper.selectLastBalance();

         Map<String, Object> receivableRow = dashboardMapper.selectReceivable();
         Map<String, Object> receivedRow = dashboardMapper.selectReceived();
         Map<String, Object> payableRow = dashboardMapper.selectPayable();
         Map<String, Object> paidRow = dashboardMapper.selectPaid();

         java.math.BigDecimal receivable = ((java.math.BigDecimal) receivableRow.get("total"))
                 .subtract((java.math.BigDecimal) receivedRow.get("total"));
         java.math.BigDecimal payable = ((java.math.BigDecimal) payableRow.get("total"))
                 .subtract((java.math.BigDecimal) paidRow.get("total"));

         Object balance = balanceRow != null ? balanceRow.get("balance_after") : 0;

         Map<String, Object> today = new HashMap<>();
         today.put("income", todayIncome.get("total"));
         today.put("expense", todayExpense.get("total"));

         Map<String, Object> result = new HashMap<>();
         result.put("today", today);
         result.put("balance", balance);
         result.put("receivable", receivable);
         result.put("payable", payable);
         result.put("stock", dashboardMapper.selectStock());
         result.put("recent_purchases", dashboardMapper.selectRecentPurchases());
         result.put("recent_sales", dashboardMapper.selectRecentSales());

         return result;
     }
 }
