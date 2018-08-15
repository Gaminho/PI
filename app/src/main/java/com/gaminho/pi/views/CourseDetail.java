package com.gaminho.pi.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;

public class CourseDetail extends LinearLayout {

    private Course mCourse;
    private CourseDetailListener mListener;

    private TextView mTVPupilFirstName, mTVCourseTime;


    public CourseDetail(Context context, @Nullable AttributeSet attrs, @NonNull Course pCourse, @NonNull CourseDetailListener pListener) {
        super(context, attrs);
        this.mCourse = pCourse;
        this.mListener = pListener;
        init();
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course mCourse) {
        this.mCourse = mCourse;
        fillCourseView(this.mCourse);
    }

    private void init(){
        inflate(getContext(), R.layout.course_detail, this);
        mTVPupilFirstName = findViewById(R.id.pupil_first_name);
        mTVCourseTime = findViewById(R.id.course_time);

        fillCourseView(mCourse);
    }

    private void fillCourseView(Course pCourse){
        mTVPupilFirstName.setText(pCourse.getPupil().getFirstname());
        mTVCourseTime.setText(pCourse.getFriendlyDuration());
        findViewById(R.id.pupil_click).setOnClickListener(v -> {
            if(this.mListener != null){
                mListener.delete(pCourse);
                removeAllViews();
            }
        });
    }

    public interface CourseDetailListener {
        void delete(Course pCourse);
    }
}
