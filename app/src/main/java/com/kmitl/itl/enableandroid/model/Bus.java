package com.kmitl.itl.enableandroid.model;

import org.parceler.Parcel;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

//TODO rename this root
@Root(name = "PlaceSearchResponse", strict = false)
@Parcel
public class Bus {

    @Element(name = "ID", required = false)
    String id;
    @Element(name = "Busno", required = false)
    String bunNumber;
    @Element(name = "seat", required = false)
    int seat;
    @Element(name = "count", required = false)
    int count;
    @Element(name = "DateTime", required = false)//yyyy-MM-ddTHH:mm:ss
    String dateTime;

    public Bus() {
    }

    public Bus(String id, String bunNumber, int seat, int count, String dateTime) {
        this.id = id;
        this.bunNumber = bunNumber;
        this.seat = seat;
        this.count = count;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBunNumber() {
        return bunNumber;
    }

    public void setBunNumber(String bunNumber) {
        this.bunNumber = bunNumber;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
