package com.egg.management.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("purchases")
public class Purchase {
    @TableId
    private Long id;
    private Long supplierId;
    private LocalDate purchaseDate;
    private BigDecimal totalAmount;
    @JsonProperty("qr_image")
    private String qrImage;
    @JsonProperty("payment_status")
    private String paymentStatus;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
}
