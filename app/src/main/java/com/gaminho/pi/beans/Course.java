package com.gaminho.pi.beans;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Course implements Serializable {

    private String mId, mPupilId, mChapter, mComment;
    private long mDate, mDuration;
    private float mMoney;

    private Pupil mPupil;

    // Necessary for Firebase
    public Course() {
    }

    public Course(String pPupilId, long pDate) {
        this.mPupilId = pPupilId;
        this.mDate = pDate;
    }

    public Course(String mPupilId, long mDate, long mDuration) {
        this(mPupilId, mDate);
        this.mDate = mDate;
        this.mDuration = mDuration;
    }

    public Course(String mPupilId, long mDate, long mDuration, float mMoney) {
        this(mPupilId, mDate, mDuration);
        this.mMoney = mMoney;
    }

    public Course(String mPupilId, String mChapter, long mDate, long mDuration, float mMoney) {
        this.mPupilId = mPupilId;
        this.mChapter = mChapter;
        this.mDate = mDate;
        this.mDuration = mDuration;
        this.mMoney = mMoney;
    }

    @Exclude
    public String getId() {
        return mId;
    }

    @Exclude
    public void setId(String mId) {
        this.mId = mId;
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

    public float getMoney() {
        return mMoney;
    }

    public void setMoney(float mMoney) {
        this.mMoney = mMoney;
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

    public String getChapter() {
        return mChapter;
    }

    public void setChapter(String mChapter) {
        this.mChapter = mChapter;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    @Override
    public String toString() {
        return "Course{" +
                "mId='" + mId + '\'' +
                ", mPupilId='" + mPupilId + '\'' +
                ", mChapter='" + mChapter + '\'' +
                ", mComment='" + mComment + '\'' +
                ", mDate=" + mDate +
                ", mDuration=" + mDuration +
                ", mMoney=" + mMoney +
                '}';
    }

    @Exclude
    public String getFriendlyDuration(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH'h'mm", Locale.FRANCE);
        return String.format(Locale.FRANCE, "%s - %s",
                sdf.format(new Date(this.getDate())),
                sdf.format(new Date(this.getDate() + this.getDuration() * 60 * 1000)));
    }
}
