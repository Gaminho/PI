package com.gaminho.pi.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class FragmentWeek extends FirebaseFragment {

    private TextView mTVCounter;


    public FragmentWeek() {
    }

    public static FragmentWeek newInstance() {
        return new FragmentWeek();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRoot = inflater.inflate(R.layout.fragment_week, container, false);;
        mTVCounter = mRoot.findViewById(R.id.tv_foreseen_class_count);
        return mRoot;
    }

    @Override
    protected void refreshView() {
        mListItems.clear();
        mListItems = ((ArrayList<Course>) mListener.getItems(ListItemFragment.LIST_COURSE)).stream().filter(
                course -> new Date(course.getDate()).after(new Date())
        ).collect(Collectors.toList());

        mTVCounter.setText(String.format(Locale.FRANCE, "%d foreseen courses", mListItems.size()));
        double money = 0;

        for(Course course : ((ArrayList<Course>)mListItems)){
            if (course.getMoney() > 0)
                money += course.getMoney();
        }

        mTVCounter.append(String.format(Locale.FRANCE, "\nExpected money: %.2f €", money));
    }
}
