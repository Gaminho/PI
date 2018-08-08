package com.gaminho.pi.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.R;
import com.gaminho.pi.activities.IndexActivity;
import com.gaminho.pi.adapters.PupilSpinnerAdapter;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// TODO Deal with earned amount

public class AddCourseDialog extends CustomAddingDialog implements View.OnClickListener {

    private List<Pupil> mPupils;
    private Calendar mCalendar;

    private TextView mTVDate, mTVHour;
    private RadioGroup mRGDuration;
    private Spinner mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getSerializable(IndexActivity.EXTRA_PUPILS_LIST) != null){
            mPupils = (List<Pupil>) getArguments().getSerializable(IndexActivity.EXTRA_PUPILS_LIST);
        } else {
            mPupils = new ArrayList<>();
        }

        mCalendar = Calendar.getInstance();
    }

    @Override
    public View getView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.class_form, null);
    }

    @Override
    public void setUpView(){
        mTVDate = super.mView.findViewById(R.id.tv_date);
        mTVHour = super.mView.findViewById(R.id.tv_hour);
        mRGDuration = super.mView.findViewById(R.id.rg_class_duration);

        mSpinner = super.mView.findViewById(R.id.pupil_spinner);
        PupilSpinnerAdapter dataAdapter = new PupilSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, mPupils);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        super.mView.findViewById(R.id.btn_datepicker).setOnClickListener(this);
        super.mView.findViewById(R.id.btn_hourpicker).setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "Add course";
    }

    private void setDate(Calendar pCalendar, int pYear, int pMonth, int pDayOfMonth){
        pCalendar.set(Calendar.YEAR, pYear);
        pCalendar.set(Calendar.MONTH, pMonth);
        pCalendar.set(Calendar.DAY_OF_MONTH, pDayOfMonth);
        mTVDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                .format(pCalendar.getTime()));
    }

    private void setHour(Calendar pCalendar, int pHour, int pMinute){
        pCalendar.set(Calendar.HOUR_OF_DAY, pHour);
        pCalendar.set(Calendar.MINUTE, pMinute);
        pCalendar.set(Calendar.SECOND, 0);
        pCalendar.set(Calendar.MILLISECOND, 0);
        mTVHour.setText(new SimpleDateFormat("HH:mm", Locale.FRANCE)
                .format(pCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_datepicker:
                new DatePickerDialog(getActivity(), (v, year, monthOfYear, dayOfMonth) ->
                        setDate(mCalendar, year, monthOfYear, dayOfMonth),
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            case R.id.btn_hourpicker:
                new TimePickerDialog(getActivity(), (v, hour, minute) ->
                        setHour(mCalendar, hour, minute),
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        true
                ).show();
                break;
        }
    }

    @Override
    DatabaseHelper.Nodes getItemNode() {
        return DatabaseHelper.Nodes.COURSES;
    }

    @Override
    boolean isItemValid() {
        hideError();
        if(mPupils.get(mSpinner.getSelectedItemPosition()).getID() == null){
            showError("No id for selected pupil");
            return false;
        } else if (mCalendar.getTime().before(new Date())){
            showError("Invalid date");
            return false;
        } else if (mRGDuration.getCheckedRadioButtonId() == -1){
            showError("Please select a duration for the course");
            return false;
        } else {
            return true;
        }
    }

    @Override
    Course extractItemFromUI() {
        String pupilId = mPupils.get(mSpinner.getSelectedItemPosition()).getID();
        long duration = 0;
        switch(mRGDuration.getCheckedRadioButtonId()){
            case R.id.rb60:
                duration = 60 * 1000 * 1000;
                break;
            case R.id.rb90:
                duration = 90 * 1000 * 1000;
                break;
            case R.id.rb120:
                duration = 120 * 1000 * 1000;
                break;
        }
        return new Course(pupilId, mCalendar.getTimeInMillis(), duration);
    }

    @Override
    void addedSuccessfully(Object pItem) {
        Course course = (Course) pItem;
        Toast.makeText(getActivity(), String.format(Locale.FRANCE, "Course added\nDate: %s\nPupil: %s",
                new Date(course.getDate()).toString(),
                course.getPupilId()
        ), Toast.LENGTH_SHORT).show();
    }
}
