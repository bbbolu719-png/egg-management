 package com.egg.management.dto;

 import java.math.BigDecimal;
 import java.util.List;

 public class SaleRequest {
     private Long customerId;
     private String saleDate;
     private List<ItemRequest> items;
     private String notes;
     private String status;

     public Long getCustomerId() { return customerId; }
     public void setCustomerId(Long customerId) { this.customerId = customerId; }
     public String getSaleDate() { return saleDate; }
     public void setSaleDate(String saleDate) { this.saleDate = saleDate; }
     public List<ItemRequest> getItems() { return items; }
     public void setItems(List<ItemRequest> items) { this.items = items; }
     public String getNotes() { return notes; }
     public void setNotes(String notes) { this.notes = notes; }
     public String getStatus() { return status; }
     public void setStatus(String status) { this.status = status; }
 }
