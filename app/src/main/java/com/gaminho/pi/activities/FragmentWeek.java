package com.gaminho.pi.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class FragmentWeek extends FirebaseFragment {

    private TextView mTVCounter;
    private ViewPager mViewPager;


    public FragmentWeek() {
    }

    public static FragmentWeek newInstance() {
        return new FragmentWeek();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_week, container, false);;
        mTVCounter = view.findViewById(R.id.tv_foreseen_class_count);
        mViewPager = view.findViewById(R.id.viewpager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MyAdapter m = new MyAdapter(getFragmentManager());
        viewPager.setAdapter(m);
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


    public static class MyAdapter extends FragmentStatePagerAdapter {

        private static final int NUMBER_OF_PAGES = 6;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentAWeek.newInstance(position);
        }
    }


    public static class FragmentAWeek extends Fragment {

        private int mOffset;
        private Date mStartDate, mEndDate;

        public FragmentAWeek() {
        }

        public static FragmentAWeek newInstance(int pOffset) {
            FragmentAWeek fragment = new FragmentAWeek();
            Bundle args = new Bundle();
            args.putInt("offset", pOffset);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Retrieving offset of week
            mOffset = getArguments() != null ? getArguments().getInt("offset") : 0;

            //Getting slot dates
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            calendar.add(Calendar.DAY_OF_YEAR, mOffset * 7);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            mStartDate = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 6);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            mEndDate = calendar.getTime();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.a_week, container, false);

            String label = String.format(Locale.FRANCE, "Lu. %s - Di. %s",
                    new SimpleDateFormat("dd-MM-yyyy").format(mStartDate),
                    new SimpleDateFormat("dd-MM-yyyy").format(mEndDate));

            ((TextView) view.findViewById(R.id.tv_week_label)).setText(label);

//            mTVLabel = view.findViewById(R.id.tv_week_label);
            return view;
        }
    }
}
