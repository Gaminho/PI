package com.gaminho.pi.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsCourse extends LinearLayout {

    private List<Course> mCourses = new ArrayList<>();

    public DetailsCourse(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        TextView tv = new TextView(context);
        tv.setText("Salut 1");
        addView(tv, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public List<Course> getCourses() {
        return mCourses;
    }

    public void setCourses(List<Course> mCourses) {
        this.mCourses = mCourses;
        updateView();
    }

    private void updateView(){
        removeAllViews();
        LayoutInflater  mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll;
        for(Course course : mCourses){
            ll = (LinearLayout) mInflater.inflate(R.layout.course_detail, this, false);
            ((TextView) ll.findViewById(R.id.pupil_first_name)).setText(course.getPupilId());
            ll.findViewById(R.id.pupil_click).setOnClickListener(view -> click(course.getPupil()));
            addView(ll, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
    }

    private void click(Pupil pPupil){
        Toast.makeText(getContext(), pPupil.toString(), Toast.LENGTH_SHORT).show();
    }
}
