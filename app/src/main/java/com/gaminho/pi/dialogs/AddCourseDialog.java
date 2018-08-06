package com.gaminho.pi.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.activities.IndexActivity;
import com.gaminho.pi.adapters.PupilSpinnerAdapter;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCourseDialog extends CustomAddingDialog implements View.OnClickListener {

    private List<Pupil> mPupils;
    private Calendar mCalendar;

    private TextView mTVDate, mTVHour;
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
        super.mView.findViewById(R.id.btn_datepicker).setOnClickListener(this);
        super.mView.findViewById(R.id.btn_hourpicker).setOnClickListener(this);
        mTVDate = super.mView.findViewById(R.id.tv_date);
        mTVHour = super.mView.findViewById(R.id.tv_hour);

        mSpinner = super.mView.findViewById(R.id.pupil_spinner);
        PupilSpinnerAdapter dataAdapter = new PupilSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, mPupils);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
    }

    @Override
    public String getTitle() {
        return "Add pupil";
    }

    @Override
    public boolean positiveClick(DialogInterface dialogInterface, int i) {
        if(isCourseValid()){
            String pupilId = mPupils.get(mSpinner.getSelectedItemPosition()).getID();
            Course course = new Course(pupilId, mCalendar.getTimeInMillis());
//            super.mListener.addItem(course);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("classes").push();
            ref.setValue(course, (databaseError, databaseReference) -> {
                String toasted = databaseError != null ?
                        databaseError.getMessage() :
                        String.format(Locale.FRANCE, "Course added\nDate: %s\nPupil: %s",
                                new Date(course.getDate()).toString(),
                                course.getPupilId()
                        );
                Toast.makeText(getActivity(), toasted, Toast.LENGTH_SHORT).show();
                dismiss();
            });

        } else {
            Log.e(getClass().getSimpleName(), "Invalid course");
        }
        return isCourseValid();
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

    private boolean isCourseValid(){
        if(mPupils.get(mSpinner.getSelectedItemPosition()).getID() == null){
            Toast.makeText(getActivity(), "No id for selected pupil", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mCalendar.getTime().before(new Date())){
            Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
