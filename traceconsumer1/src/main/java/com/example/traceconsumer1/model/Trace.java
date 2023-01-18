package com.example.traceconsumer1.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Trace {
    @Id
    private String traceId;
    private String truck;
    private long lastSeen;
    private double lat;
    private double lng;
    public Trace(){
    }
    public Trace(String traceId, String truck, long lastSeen, double lat, double lng) {
        this.traceId = traceId;
        this.truck=truck;
        this.lastSeen = lastSeen;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "traceId='" + traceId + '\'' +
                ", truck='" + truck + '\'' +
                ", lastSeen=" + lastSeen +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trace trace = (Trace) o;
        return lastSeen == trace.lastSeen && Double.compare(trace.lat, lat) == 0 && Double.compare(trace.lng, lng) == 0 && Objects.equals(traceId, trace.traceId) && Objects.equals(truck, trace.truck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traceId, truck, lastSeen, lat, lng);
    }
}
