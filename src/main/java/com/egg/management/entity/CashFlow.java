package com.egg.management.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cash_flow")
public class CashFlow {
    @TableId
    private Long id;
    private LocalDate flowDate;
    private String type;
    private BigDecimal amount;
    private String category;
    private String refType;
    private Long refId;
    private String description;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;
}
