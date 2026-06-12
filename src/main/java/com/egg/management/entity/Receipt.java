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
@TableName("receipts")
public class Receipt {
    @TableId
    private Long id;
    private Long customerId;
    private Long saleId;
    private BigDecimal amount;
    private LocalDate receiptDate;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
}
