package com.gaminho.pi.activities.courses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ActivityCourse extends AppCompatActivity {

    private Spinner mSpinPupil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_form);

        mSpinPupil = findViewById(R.id.pupil_spinner);
        getPupils();

        findViewById(R.id.add_class).setOnClickListener(v -> {
            Course course = new Course(mSpinPupil.getSelectedItem().toString(), System.currentTimeMillis());
            addCourse(course);
        });
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
