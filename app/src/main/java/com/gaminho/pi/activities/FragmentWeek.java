package com.gaminho.pi.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FragmentWeek extends FirebaseFragment {

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
//        mListItems.clear();
//        mListItems = ((ArrayList<Course>) mListener.getItems(ListItemFragment.LIST_COURSE)).stream().filter(
//                course -> new Date(course.getDate()).after(new Date())
//        ).collect(Collectors.toList());
//
//        mTVCounter.setText(String.format(Locale.FRANCE, "%d foreseen courses", mListItems.size()));
//        double money = 0;
//
//        for(Course course : ((ArrayList<Course>)mListItems)){
//            if (course.getMoney() > 0)
//                money += course.getMoney();
//        }
//
//        mTVCounter.append(String.format(Locale.FRANCE, "\nExpected money: %.2f €", money));
    }

    protected static Date[] getWeekSlot(int pWeekOffset){
        Date[] slot = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.add(Calendar.DAY_OF_YEAR, pWeekOffset * 7);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        slot[0] = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        slot[1] = calendar.getTime();
        return slot;
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


    public static class FragmentAWeek extends FirebaseFragment {

        private int mOffset;
        private Date mStartDate, mEndDate;
        private TextView mTVCourses, mTVMoney;
        private List<CardView> mCVDays = new ArrayList<>();
        private static final int ONE_DAY = 1000 * 60 * 60 * 24;



        public FragmentAWeek() {
        }

        public static FragmentAWeek newInstance(int pOffset) {
            FragmentAWeek fragment = new FragmentAWeek();
            Bundle args = new Bundle();
            args.putInt("offset", pOffset);
            fragment.setArguments(args);
            return fragment;
        }

        private List<Course> getListCourses(){
            return (ArrayList<Course>) mListItems;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Retrieving offset of week
            mOffset = getArguments() != null ? getArguments().getInt("offset") : 0;

            mStartDate = getWeekSlot(mOffset)[0];
            mEndDate = getWeekSlot(mOffset)[1];
        }

        @Override
        protected void refreshView() {
            mListItems.clear();
            getListCourses().addAll(((ArrayList<Course>) mListener.getItems(ListItemFragment.LIST_COURSE))
                    .stream().filter(course -> new Date(course.getDate()).after(this.mStartDate)
                            && new Date(course.getDate()).before(this.mEndDate))
                    .sorted((course1, course2) -> Long.valueOf(course1.getDate()).compareTo(course2.getDate()))
                    .collect(Collectors.toList())
            );

            double money = getListCourses().stream().mapToDouble(Course::getMoney).sum();

            mTVCourses.setText(String.format(Locale.FRANCE, "%d", mListItems.size()));
            mTVMoney.setText(String.format(Locale.FRANCE, "%.2f", money));
            fillDayViews();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.a_week, container, false);

            mTVCourses = view.findViewById(R.id.tv_nb_courses);
            mTVMoney = view.findViewById(R.id.tv_nb_money);

            mCVDays.add(view.findViewById(R.id.lay_day_1));
            mCVDays.add(view.findViewById(R.id.lay_day_2));
            mCVDays.add(view.findViewById(R.id.lay_day_3));
            mCVDays.add(view.findViewById(R.id.lay_day_4));
            mCVDays.add(view.findViewById(R.id.lay_day_5));
            mCVDays.add(view.findViewById(R.id.lay_day_6));
            mCVDays.add(view.findViewById(R.id.lay_day_7));

            Calendar cal = Calendar.getInstance();
            int currentDay = cal.get(Calendar.DAY_OF_YEAR);
            cal.setTime(mStartDate);

            for(CardView cv : mCVDays) {
                ((TextView) cv.findViewById(R.id.tv_day_month)).setText(
                        new SimpleDateFormat("MMM", Locale.FRANCE).format(cal.getTime()));
                ((TextView) cv.findViewById(R.id.tv_day_label)).setText(
                        new SimpleDateFormat("EE", Locale.FRANCE).format(cal.getTime()));
                ((TextView) cv.findViewById(R.id.tv_day_number)).setText(
                        String.format(Locale.FRANCE, "%02d", cal.get(Calendar.DAY_OF_MONTH)));

                if(currentDay == cal.get(Calendar.DAY_OF_YEAR)){
                    cv.findViewById(R.id.tv_day_lay).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    ((TextView) cv.findViewById(R.id.tv_day_number)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ((TextView) cv.findViewById(R.id.tv_day_label)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ((TextView) cv.findViewById(R.id.tv_day_month)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            return view;
        }

        private void fillDayViews(){

            SimpleDateFormat sdf = new SimpleDateFormat("HH'h'MM");

            Calendar cal = Calendar.getInstance();
            cal.setTime(mStartDate);

            for(CardView cv : mCVDays) {

                List<Course> courses = filterCourseByDay(getListCourses(), cal.getTimeInMillis());

                if(!courses.isEmpty()){
                    String str = courses.stream().map(course ->
                            String.format(
                                    Locale.FRANCE, "%s - %s : %s %s",
                                    sdf.format(new Date(course.getDate())),
                                    sdf.format(new Date(course.getDate() + course.getDuration())),
                                    course.getPupil().getFirstname(), course.getPupil().getLastname()))
                            .collect(Collectors.joining("\n"));

                    ((TextView) cv.findViewById(R.id.tv_day_content)).setText(str);
                }

                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        private List<Course> filterCourseByDay(List<Course> pCourses, long midnightTS){
            return pCourses.stream().filter(course ->
                    course.getDate() >= midnightTS && course.getDate() < (midnightTS + ONE_DAY))
                    .collect(Collectors.toList());
        }
    }
}
