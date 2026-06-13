 package com.egg.management.mapper;

 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import org.apache.ibatis.annotations.Param;

 import java.util.List;
 import java.util.Map;

 public interface PurchaseMapperCustom {
     IPage<Map<String, Object>> selectPurchasePage(Page<?> page);
     Map<String, Object> selectPurchaseById(@Param("id") Long id);
     List<Map<String, Object>> selectPurchaseItems(@Param("purchaseId") Long purchaseId);
     List<Map<String, Object>> selectPayments(@Param("purchaseId") Long purchaseId);
 }
