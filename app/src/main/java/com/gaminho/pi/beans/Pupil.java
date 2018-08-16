package com.gaminho.pi.beans;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Locale;

public class Pupil implements Serializable {

    private String mID, mFirstname, mLastname, mPhone, mParentPhone;
    private float mHourPrice;
    private int mSex;
    private long mCreation;
    private MyPlace pPlace;

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

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getParentPhone() {
        return mParentPhone;
    }

    public void setParentPhone(String mParentPhone) {
        this.mParentPhone = mParentPhone;
    }

    public MyPlace getPlace() {
        return pPlace;
    }

    public void setPlace(MyPlace pPlace) {
        this.pPlace = pPlace;
    }

    public void setGooglePlace(Place pPlace) {
        this.pPlace = new MyPlace(pPlace);
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "mID='" + mID + '\'' +
                ", mFirstname='" + mFirstname + '\'' +
                ", mLastname='" + mLastname + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mParentPhone='" + mParentPhone + '\'' +
                ", mHourPrice=" + mHourPrice +
                ", mSex=" + mSex +
                ", mCreation=" + mCreation +
                ", pPlace=" + pPlace +
                '}';
    }

    @Exclude
    public String getFullName(){
        return String.format(Locale.FRANCE, "%s %s", this.mFirstname, this.mLastname);
    }
}
