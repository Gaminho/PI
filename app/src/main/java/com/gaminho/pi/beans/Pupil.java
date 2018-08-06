package com.gaminho.pi.beans;

import java.io.Serializable;

public class Pupil implements Serializable {

    private String mID, mFirstname, mLastname;
    private long mCreation;


    // Necessary for Firebase
    public Pupil() {
    }

    public Pupil(String mFirstname, String mLastname) {
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mCreation = System.currentTimeMillis();
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
}
