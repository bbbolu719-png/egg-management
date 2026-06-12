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
@TableName("payments")
public class Payment {
    @TableId
    private Long id;
    private Long supplierId;
    private Long purchaseId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
    private String status;
    private String qrImage;
    private String notes;
    private LocalDateTime createdAt;
}
