 package com.egg.management.mapper;

 import java.util.List;
 import java.util.Map;

 public interface DashboardMapper {
     Map<String, Object> selectTodayIncome();
     Map<String, Object> selectTodayExpense();
     Map<String, Object> selectLastBalance();
     Map<String, Object> selectReceivable();
     Map<String, Object> selectReceived();
     Map<String, Object> selectPayable();
     Map<String, Object> selectPaid();
     List<Map<String, Object>> selectStock();
     List<Map<String, Object>> selectRecentPurchases();
     List<Map<String, Object>> selectRecentSales();
 }
