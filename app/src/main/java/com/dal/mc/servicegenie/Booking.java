package com.dal.mc.servicegenie;

import java.util.Calendar;

public class Booking {

    private String serviceName,status,profInfo;
    private Calendar timeNDate;
    private Float cost;

    //Getter method for service name
    public String getServiceName() {
        return serviceName;
    }

    //Setter method for service name
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfInfo() {
        return profInfo;
    }

    public void setProfInfo(String profInfo) {
        this.profInfo = profInfo;
    }

    public Calendar getTimeNDate() {
        return timeNDate;
    }

    public void setTimeNDate(Calendar timeNDate) {
        this.timeNDate = timeNDate;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }



}
