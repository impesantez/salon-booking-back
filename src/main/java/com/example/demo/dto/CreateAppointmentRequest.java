package com.example.demo.dto;

import java.util.List;

public class CreateAppointmentRequest {
    private String clientName;
    private String clientEmail;   // ✅ NEW
    private String clientPhone;   // ✅ OPTIONAL NEW
    private String date;
    private String startTime;
    private String endTime;
    private Long nailTechId;
    private List<Long> serviceIds;
    private String notes;

    // Getters & Setters
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public Long getNailTechId() { return nailTechId; }
    public void setNailTechId(Long nailTechId) { this.nailTechId = nailTechId; }

    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
