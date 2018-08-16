package com.gaminho.pi.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.utils.DatabaseHelper;
import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.dialogs.AddCourseDialog;
import com.gaminho.pi.views.DayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FragmentWeek extends Fragment {

    private ViewPager mViewPager;

    public FragmentWeek() {
    }

    public static FragmentWeek newInstance() {
        return new FragmentWeek();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_week, container, false);
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

        MyAdapter(FragmentManager fm) {
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


    public static class FragmentAWeek extends FirebaseFragment implements DayView.DayViewListener {

        private int mOffset;
        private Date mStartDate, mEndDate;
        private TextView mTVCourses, mTVMoney;
        private List<DayView> mDayViews = new ArrayList<>();
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
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.week_page, container, false);

            mTVCourses = view.findViewById(R.id.tv_nb_courses);
            mTVMoney = view.findViewById(R.id.tv_nb_money);

            mDayViews.add(view.findViewById(R.id.day_view_1));
            mDayViews.add(view.findViewById(R.id.day_view_2));
            mDayViews.add(view.findViewById(R.id.day_view_3));
            mDayViews.add(view.findViewById(R.id.day_view_4));
            mDayViews.add(view.findViewById(R.id.day_view_5));
            mDayViews.add(view.findViewById(R.id.day_view_6));
            mDayViews.add(view.findViewById(R.id.day_view_7));

            Calendar cal = Calendar.getInstance(Locale.FRENCH);
            cal.setTime(mStartDate);

            for(DayView dv : mDayViews) {
                dv.setDate(cal.getTime());
                dv.addOnDayViewListener(this);
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            return view;
        }


        private void fillDayViews(){

            Calendar cal = Calendar.getInstance();
            cal.setTime(mStartDate);

            for(DayView dv : mDayViews) {
                dv.setDayCourses(filterCourseByDay(getListCourses(), cal.getTimeInMillis()));
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        private List<Course> filterCourseByDay(List<Course> pCourses, long midnightTS){
            return pCourses.stream().filter(course ->
                    course.getDate() >= midnightTS && course.getDate() < (midnightTS + ONE_DAY))
                    .collect(Collectors.toList());
        }

        @Override
        public void addCourse(Date mDate) {
            AddCourseDialog a = new AddCourseDialog();
            Bundle args = new Bundle();
            args.putSerializable(AddCourseDialog.EXTRA_PUPILS_LIST, new ArrayList<>(mListener.getItems(ListItemFragment.LIST_PUPIL)));
            args.putSerializable(AddCourseDialog.EXTRA_DATE, mDate);
            a.setArguments(args);
            a.show(getActivity().getFragmentManager(), "MyAddingDialog");
        }

        @Override
        public void removeCourse(Course pCourse) {
            mListener.removeItem(DatabaseHelper.COURSES, pCourse.getId());
        }
    }
}
