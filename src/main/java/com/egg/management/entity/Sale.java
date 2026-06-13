package com.egg.management.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@TableName("sales")
public class Sale {
    @TableId
    private Long id;
    private Long customerId;
    private LocalDate saleDate;
    private BigDecimal totalAmount;
    @JsonProperty("receipt_status")
    private String receiptStatus;
    @JsonProperty("received_amount")
    private BigDecimal receivedAmount;
    private String orderNo;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
}
