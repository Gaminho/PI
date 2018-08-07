package com.gaminho.pi.beans;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Course implements Serializable {

    private String mPupilId;
    private long mDate, mDuration;

    private Pupil mPupil;

    // Necessary for Firebase
    public Course() {
    }

    public Course(String pPupilId, long pDate) {
        this.mPupilId = pPupilId;
        this.mDate = pDate;
    }

    public Course(String mPupilId, long mDate, long mDuration) {
        this.mPupilId = mPupilId;
        this.mDate = mDate;
        this.mDuration = mDuration;
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

    @Exclude
    public Pupil getPupil(){return mPupil;}

    @Exclude
    public void setPupil(Pupil pPupil){this.mPupil = pPupil;}

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    @Override
    public String toString() {
        return "Course{" +
                "mPupilId='" + mPupilId + '\'' +
                ", mDate=" + mDate +
                ", mDuration=" + mDuration +
                '}';
    }
}
