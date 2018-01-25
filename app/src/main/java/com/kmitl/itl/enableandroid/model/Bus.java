package com.kmitl.itl.enableandroid.model;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.net.URL;
import java.util.List;

public class Bus {

    @SerializedName("seat")
    String seat;

    @SerializedName("count")
    String count;

    @SerializedName("Busno")
    String busno;

    @SerializedName("ID")
    String iD;

    @SerializedName("DateTime")
    String dateTime;

    public String getSeat() {return this.seat;}
    public void setSeat(String value) {this.seat = value;}

    public String getCount() {return this.count;}
    public void setCount(String value) {this.count = value;}

    public String getBusno() {return this.busno;}
    public void setBusno(String value) {this.busno = value;}

    public String getID() {return this.iD;}
    public void setID(String value) {this.iD = value;}

    public String getDateTime() {return this.dateTime;}
    public void setDateTime(String value) {this.dateTime = value;}

}