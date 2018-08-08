package com.gaminho.pi.beans;

import java.io.Serializable;

public class Pupil implements Serializable {

    private String mID, mFirstname, mLastname;
    private float mHourPrice;
    private int mSex;
    private long mCreation;

    public static final int GIRL = 2;
    public static final int BOY = 3;


    // Necessary for Firebase
    public Pupil() {
    }

    public Pupil(String pFirstName, String pLastName) {
        this.mFirstname = pFirstName;
        this.mLastname = pLastName;
        this.mCreation = System.currentTimeMillis();
    }

    public Pupil(String mFirstName, String mLastName, float mHourPrice, int mSex) {
        this(mFirstName, mLastName);
        this.mHourPrice = mHourPrice;
        this.mSex = mSex;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String mLastname) {
        this.mLastname = mLastname;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public long getCreation() {
        return mCreation;
    }

    public void setCreation(long mCreation) {
        this.mCreation = mCreation;
    }

    public float getHourPrice() {
        return mHourPrice;
    }

    public void setHourPrice(float mHourPrice) {
        this.mHourPrice = mHourPrice;
    }

    public int getSexe() {
        return mSex;
    }

    public void setSexe(int mSex) {
        this.mSex = mSex;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "mID='" + mID + '\'' +
                ", mFirstname='" + mFirstname + '\'' +
                ", mLastname='" + mLastname + '\'' +
                ", mHourPrice=" + mHourPrice +
                ", mSexe=" + mSex +
                ", mCreation=" + mCreation +
                '}';
    }
}
