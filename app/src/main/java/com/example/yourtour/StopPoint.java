package com.example.yourtour;

import java.io.Serializable;

public class StopPoint implements Serializable {
    public String id,serviceId,name,address,Lat,Long;
    public String arriveAt, leaveAt, minCost, maxCost, serviceTypeId;

    public StopPoint(String id, String serviceId, String name, String address, String Lat, String Long, String arriveAt, String leaveAt, String minCost,String maxCost,String serviceTypeId){
        this.id=id;
        this.serviceId=serviceId;
        this.name = name;
        this.address = address;
        this.Lat=Lat;
        this.Long=Long;
        this.arriveAt=arriveAt;
        this.leaveAt=leaveAt;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.serviceTypeId=serviceTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getArriveAt() {
        return arriveAt;
    }

    public void setArriveAt(String arriveAt) {
        this.arriveAt = arriveAt;
    }

    public String getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(String leaveAt) {
        this.leaveAt = leaveAt;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
