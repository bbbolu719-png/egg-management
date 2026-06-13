package com.egg.management.dto;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseRequest {
    private Long supplierId;
    private String purchaseDate;
    private List<ItemRequest> items;
    private BigDecimal fuelCost;
    private BigDecimal crateCost;
    private BigDecimal bagCost;
    private BigDecimal otherCost;
    private String qrImage;
    private String notes;
    private String status;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }
    public BigDecimal getFuelCost() { return fuelCost; }
    public void setFuelCost(BigDecimal fuelCost) { this.fuelCost = fuelCost; }
    public BigDecimal getCrateCost() { return crateCost; }
    public void setCrateCost(BigDecimal crateCost) { this.crateCost = crateCost; }
    public BigDecimal getBagCost() { return bagCost; }
    public void setBagCost(BigDecimal bagCost) { this.bagCost = bagCost; }
    public BigDecimal getOtherCost() { return otherCost; }
    public void setOtherCost(BigDecimal otherCost) { this.otherCost = otherCost; }
    public String getQrImage() { return qrImage; }
    public void setQrImage(String qrImage) { this.qrImage = qrImage; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
