package com.unir.management.dto;

import java.time.LocalDateTime;

public class RentalRequest {
    private Long vehicleId;
    private String userId; // Assuming userId is String
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public RentalRequest() {}

    public RentalRequest(Long vehicleId, String userId, LocalDateTime startDate, LocalDateTime endDate) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and setters
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}