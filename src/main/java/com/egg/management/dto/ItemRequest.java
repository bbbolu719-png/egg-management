 package com.egg.management.dto;

 import java.math.BigDecimal;

 public class ItemRequest {
     private Long qualityId;
     private BigDecimal quantity;
     private BigDecimal unitPrice;
     private BigDecimal subtotal;

     public Long getQualityId() { return qualityId; }
     public void setQualityId(Long qualityId) { this.qualityId = qualityId; }
     public BigDecimal getQuantity() { return quantity; }
     public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
     public BigDecimal getUnitPrice() { return unitPrice; }
     public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
     public BigDecimal getSubtotal() { return subtotal; }
     public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
 }
