package com.gaminho.pi.beans;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.util.HashMap;

public class MyPlace extends HashMap<String, Object> implements Serializable {

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private String mName, mAddress;
    private Double mLongitude, mLatitude;


    public MyPlace() {
    }

    public MyPlace(Place pPlace){
        this.put(NAME, pPlace.getName().toString());
        this.put(ADDRESS, pPlace.getAddress().toString());
        this.put(LATITUDE, pPlace.getLatLng().latitude);
        this.put(LONGITUDE, pPlace.getLatLng().longitude);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    @Override
    public String toString() {
        return "MyPlace{" +
                "mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mLongitude=" + mLongitude +
                ", mLatitude=" + mLatitude +
                '}';
    }
}
