package com.gaminho.pi.activities.courses;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ActivityCourse extends AppCompatActivity {

    private Spinner mSpinPupil;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_form);

        mCalendar = Calendar.getInstance();
        mSpinPupil = findViewById(R.id.pupil_spinner);

        getPupils();

        findViewById(R.id.btn_datepicker).setOnClickListener(v -> new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
                setDate(mCalendar, year, monthOfYear, dayOfMonth),
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        findViewById(R.id.btn_hourpicker).setOnClickListener(v -> new TimePickerDialog(this, (view, hour, minute) ->
                setHour(mCalendar, hour, minute),
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                true
        ).show());


        findViewById(R.id.add_class).setOnClickListener(v -> {
            Course course = new Course(mSpinPupil.getSelectedItem().toString(), mCalendar.getTimeInMillis());
            addCourse(course);
        });
    }

    private void setDate(Calendar pCalendar, int pYear, int pMonth, int pDayOfMonth){
        pCalendar.set(Calendar.YEAR, pYear);
        pCalendar.set(Calendar.MONTH, pMonth);
        pCalendar.set(Calendar.DAY_OF_MONTH, pDayOfMonth);
        updateUI();
    }

    private void setHour(Calendar pCalendar, int pHour, int pMinute){
        pCalendar.set(Calendar.HOUR_OF_DAY, pHour);
        pCalendar.set(Calendar.MINUTE, pMinute);
        pCalendar.set(Calendar.SECOND, 0);
        pCalendar.set(Calendar.MILLISECOND, 0);
        updateUI();
    }

    private void updateUI(){
        ((TextView)findViewById(R.id.tv_date)).setText(
                new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
                        .format(mCalendar.getTime()));
        ((TextView)findViewById(R.id.tv_hour)).setText(
                new SimpleDateFormat("HH:mm", Locale.FRANCE)
                        .format(mCalendar.getTime()));
    }

    private void getPupils(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pupils");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pMsg;
                List<Pupil> pupils = new ArrayList<>();

                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pupils.add(snapshot.getValue(Pupil.class));
                    }
                    pMsg = String.format(Locale.FRANCE,"%d pupils found", pupils.size());
                    fillSpinner(pupils);
                } catch (Exception e){
                    pMsg = String.format(Locale.FRANCE, "Exception:\n%s ", e.getMessage());
                }
                Toast.makeText(getApplication(), pMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplication(), String.format(Locale.FRANCE, "Failed to read value\n%s ", error.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSpinner(List<Pupil> pPupils){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                pPupils.stream()
                        .map(p-> p.getFirstname() + " " + p.getLastname())
                        .collect(Collectors.toList()));

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinPupil.setAdapter(dataAdapter);
    }

    private void addCourse(Course pCourse) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("classes").push();
        ref.setValue(pCourse, (databaseError, databaseReference) -> {
            String toasted = databaseError != null ?
                    databaseError.getMessage() :
                    String.format(Locale.FRANCE, "Course added\nDate: %s\nPupil: %s",
                            new Date(pCourse.getDate()).toString(),
                            pCourse.getPupilId()
                    );
            Toast.makeText(getApplication(), toasted, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

}
