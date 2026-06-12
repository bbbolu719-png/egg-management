 package com.egg.management.mapper;

 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import org.apache.ibatis.annotations.Param;

 import java.util.List;
 import java.util.Map;

 public interface SaleMapperCustom {
     IPage<Map<String, Object>> selectSalePage(Page<?> page);
     Map<String, Object> selectSaleById(@Param("id") Long id);
     List<Map<String, Object>> selectSaleItems(@Param("saleId") Long saleId);
 }
