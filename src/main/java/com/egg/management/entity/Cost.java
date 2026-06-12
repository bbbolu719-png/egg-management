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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("costs")
public class Cost {
    @TableId
    private Long id;

    @JsonProperty("cost_type")
    private String costType;

    private BigDecimal amount;

    @JsonProperty("cost_date")
    private LocalDate costDate;

    private String notes;
    private LocalDateTime createdAt;
}
