package com.egg.management.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("customers")
public class Customer {
    @TableId
    private Long id;
    private String name;
    private String contact;
    private String phone;
    private String address;
    private String notes;
    private String qrImage;
    private LocalDateTime createdAt;
}
