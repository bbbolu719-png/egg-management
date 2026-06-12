 package com.egg.management.service.impl;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import com.egg.management.entity.CashFlow;
 import com.egg.management.mapper.CashFlowMapper;
 import com.egg.management.service.CashFlowService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.math.BigDecimal;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 @Service
 public class CashFlowServiceImpl implements CashFlowService {

     @Autowired
     private CashFlowMapper cashFlowMapper;

     @Override
     public IPage<CashFlow> list(String type, String start, String end, int page, int pageSize) {
         LambdaQueryWrapper<CashFlow> wrapper = new LambdaQueryWrapper<CashFlow>()
                 .orderByDesc(CashFlow::getFlowDate, CashFlow::getId);

         if (type != null && !type.isEmpty()) {
             wrapper.eq(CashFlow::getType, type);
         }
         if (start != null && !start.isEmpty()) {
             wrapper.ge(CashFlow::getFlowDate, start);
         }
         if (end != null && !end.isEmpty()) {
             wrapper.le(CashFlow::getFlowDate, end);
         }

         return cashFlowMapper.selectPage(new Page<>(page, pageSize), wrapper);
     }

     @Override
     public Map<String, Object> summary(String start, String end) {
         // We use raw SQL because GROUP BY is needed
         // For simplicity, fetch income and expense separately
         LambdaQueryWrapper<CashFlow> incomeWrapper = new LambdaQueryWrapper<CashFlow>()
                 .eq(CashFlow::getType, "income");
         LambdaQueryWrapper<CashFlow> expenseWrapper = new LambdaQueryWrapper<CashFlow>()
                 .eq(CashFlow::getType, "expense");

         if (start != null && !start.isEmpty()) {
             incomeWrapper.ge(CashFlow::getFlowDate, start);
             expenseWrapper.ge(CashFlow::getFlowDate, start);
         }
         if (end != null && !end.isEmpty()) {
             incomeWrapper.le(CashFlow::getFlowDate, end);
             expenseWrapper.le(CashFlow::getFlowDate, end);
         }

         List<CashFlow> incomes = cashFlowMapper.selectList(incomeWrapper);
         List<CashFlow> expenses = cashFlowMapper.selectList(expenseWrapper);

         BigDecimal totalIncome = incomes.stream()
                 .map(CashFlow::getAmount)
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
         BigDecimal totalExpense = expenses.stream()
                 .map(CashFlow::getAmount)
                 .reduce(BigDecimal.ZERO, BigDecimal::add);

         Map<String, Object> result = new HashMap<>();
         result.put("income", totalIncome);
         result.put("expense", totalExpense);
         return result;
     }
 }
