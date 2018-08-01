package com.gaminho.pi.activities;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AddPupil.OnAddingPupilListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add_pupils).setOnClickListener(this);
        findViewById(R.id.see_pupils).setOnClickListener(this);

        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().replace(R.id.frame_pupils, new SeePupils()).commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager ft;
        switch(view.getId()){
            case R.id.add_pupils:
                ft = getSupportFragmentManager();
                ft.beginTransaction().replace(R.id.frame_pupils, new AddPupil()).commit();
                break;
            case R.id.see_pupils:
                ft = getSupportFragmentManager();
                ft.beginTransaction().replace(R.id.frame_pupils, new SeePupils()).commit();
                break;
        }
    }

    @Override
    public void onAddingPupil(final Pupil pPupil) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("pupils").push();
        ref.setValue(pPupil, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String toasted = databaseError != null ?
                        databaseError.getMessage() :
                        String.format(Locale.FRANCE, "%s has been added", pPupil.getFirstname());
                Toast.makeText(getApplication(), toasted, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
