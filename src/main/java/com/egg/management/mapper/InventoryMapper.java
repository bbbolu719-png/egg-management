 package com.egg.management.mapper;

 import org.apache.ibatis.annotations.Param;

 import java.util.List;
 import java.util.Map;

 public interface InventoryMapper {
     List<Map<String, Object>> selectStock();
     List<Map<String, Object>> selectPurchaseMovements(@Param("qualityId") Long qualityId);
     List<Map<String, Object>> selectSaleMovements(@Param("qualityId") Long qualityId);
 }
