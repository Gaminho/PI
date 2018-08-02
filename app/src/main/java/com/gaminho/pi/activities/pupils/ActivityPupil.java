package com.gaminho.pi.activities.pupils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class ActivityPupil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pupil_form);

        findViewById(R.id.add_pupil).setOnClickListener(v -> {
            if (isPupilValid()) {
                Pupil p = new Pupil(((EditText) findViewById(R.id.pupilFirstname)).getText().toString(),
                        ((EditText) findViewById(R.id.pupilLastname)).getText().toString());
                addPupil(p);
            }
        });
    }

    private boolean isPupilValid() {
        EditText pFirstname = findViewById(R.id.pupilFirstname);
        EditText pLastname = findViewById(R.id.pupilLastname);
        String msg = null;
        if (pFirstname.getText() == null) {
            msg = "Firstname is missing";
        } else if (pFirstname.getText().length() < 2) {
            msg = "Please, enter a correct firstname";
        } else if (pLastname.getText() == null) {
            msg = "Lastname is missing";
        } else if (pLastname.getText().length() < 2) {
            msg = "Please, enter a correct lastname";
        }

        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addPupil(Pupil pPupil) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("pupils").push();
        ref.setValue(pPupil, (databaseError, databaseReference) -> {
            String toasted = databaseError != null ?
                    databaseError.getMessage() :
                    String.format(Locale.FRANCE, "%s has been added", pPupil.getFirstname());
            Toast.makeText(getApplication(), toasted, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

}
