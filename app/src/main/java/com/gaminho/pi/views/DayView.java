package com.gaminho.pi.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DayView extends LinearLayout {

    private TextView mTVDayLabel, mTVDayNumber, mTVMonth, mTVDayContent;
    private Date mDate;
    private LinearLayout mLVCourses;
    private DayViewListener mListener;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.day_view, this);
        mTVDayLabel = findViewById(R.id.tv_day_label);
        mTVDayNumber = findViewById(R.id.tv_day_number);
        mTVMonth = findViewById(R.id.tv_day_month);
        mTVDayContent = findViewById(R.id.tv_day_content);
        mLVCourses = findViewById(R.id.list_courses);

        mDate = Calendar.getInstance().getTime();
        setDate(mDate);

        findViewById(R.id.day_add_course).setOnClickListener(view -> {
            if(mListener != null) {
                mListener.addCourse(mDate);
            }
        });
        findViewById(R.id.day_more_course).setOnClickListener(view ->
                mLVCourses.setVisibility(mLVCourses.getVisibility() == VISIBLE ? GONE : VISIBLE));
    }

    public void setDate(Date pDate){
        this.mDate = pDate;
        updateDayLabel();
    }

    private void updateDayLabel(){
        mTVDayLabel.setText(new SimpleDateFormat("EE", Locale.FRANCE).format(mDate));
        mTVDayNumber.setText(new SimpleDateFormat("dd", Locale.FRANCE).format(mDate));
        mTVMonth.setText(new SimpleDateFormat("MMM", Locale.FRANCE).format(mDate));

        handleTextColor(mTVDayLabel, mTVDayNumber, mTVMonth);
    }

    private void handleTextColor(TextView... textViews){
        Calendar cal = Calendar.getInstance(Locale.FRENCH);
        int currentDay = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(mDate);
        for(TextView tv : textViews){
            tv.setTextColor(currentDay == cal.get(Calendar.DAY_OF_YEAR) ?
                    ContextCompat.getColor(getContext(), R.color.colorAccent) :
                    ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }

    public void setDayCourses(List<Course> pCourses){
        updateDayContent(pCourses);
    }

    private void updateDayContent(List<Course> pCourses){
        findViewById(R.id.day_more_course).setVisibility(pCourses.isEmpty() ? GONE : VISIBLE);
        String str = "";
        if(!pCourses.isEmpty()){

            str = pCourses.stream().map(course ->
                    String.format(
                            Locale.FRANCE, "%s : %s %s", course.getFriendlyDuration(),
                            course.getPupil().getFirstname(), course.getPupil().getLastname()))
                    .collect(Collectors.joining("\n"));

            mLVCourses.removeAllViews();
            for(Course course : pCourses){
                mLVCourses.addView(new CourseDetail(getContext(), null, course, pCourse -> {
                    if(mListener != null) {
                        mListener.removeCourse(pCourse);
                    } }
                ), LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
        }

        mTVDayContent.setText(str);
    }

    public void addOnDayViewListener(DayViewListener pListener){
        if(this.mListener == null){
            mListener = pListener;
        }
    }

    public interface DayViewListener {
        void addCourse(Date pDate);
        void removeCourse(Course pCourse);
    }
}
