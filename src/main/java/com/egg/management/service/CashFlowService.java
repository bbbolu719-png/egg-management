 package com.egg.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.egg.management.entity.CashFlow;

import java.util.Map;

public interface CashFlowService {
    IPage<CashFlow> list(String type, String start, String end, int page, int pageSize);
    Map<String, Object> summary(String start, String end);
}
