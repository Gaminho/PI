package com.gaminho.pi.beans;

public class Course {

    private String mPupilId;
    private long mDate;

    // Necessary for Firebase
    public Course() {
    }

    public Course(String pPupilId, long pDate) {
        this.mPupilId = pPupilId;
        this.mDate = pDate;
    }

    public String getPupilId() {
        return mPupilId;
    }

    public void setPupilId(String mPupilId) {
        this.mPupilId = mPupilId;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long mDate) {
        this.mDate = mDate;
    }
}
