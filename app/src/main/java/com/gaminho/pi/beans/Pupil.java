package com.gaminho.pi.beans;

public class Pupil {

    private String mFirstname, mLastname;

    // Necessary for Firebase
    public Pupil() {
    }

    public Pupil(String mFirstname, String mLastname) {
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
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
}
