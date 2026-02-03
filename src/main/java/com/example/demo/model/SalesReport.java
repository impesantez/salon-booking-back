package com.example.demo.model;

public class SalesReport {
    private String nailTech;
    private Double totalSales;
    private Long appointmentCount;

    public SalesReport(String nailTech, Double totalSales, Long appointmentCount) {
        this.nailTech = nailTech;
        this.totalSales = totalSales;
        this.appointmentCount = appointmentCount;
    }

    public String getNailTech() { return nailTech; }
    public void setNailTech(String nailTech) { this.nailTech = nailTech; }

    public Double getTotalSales() { return totalSales; }
    public void setTotalSales(Double totalSales) { this.totalSales = totalSales; }

    public Long getAppointmentCount() { return appointmentCount; }
    public void setAppointmentCount(Long appointmentCount) { this.appointmentCount = appointmentCount; }
}
