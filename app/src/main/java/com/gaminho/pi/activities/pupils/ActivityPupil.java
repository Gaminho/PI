package com.gaminho.pi.activities.pupils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.gaminho.pi.interfaces.DatabaseListener;
import com.gaminho.pi.utils.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ActivityPupil extends AppCompatActivity implements ValueEventListener, DatabaseListener {

    //Intent
    public final static String EXTRA_PUPIL = "pupil-id";

    private FirebaseDatabase mDatabase;
    private String mPupilId;
    private Pupil mPupil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil);
        mDatabase = FirebaseDatabase.getInstance();
        mPupilId = getIntent().getStringExtra(EXTRA_PUPIL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pupil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            return true;
        } else if (id == R.id.action_remove){
            this.removeItem(DatabaseHelper.PUPILS, mPupilId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper.getNodeReference(mDatabase, DatabaseHelper.PUPILS).child(mPupilId).addListenerForSingleValueEvent(this);
    }

    private void fillPupilInfo(Pupil pPupil){
        ((TextView) findViewById(R.id.tv_pupil)).setText(pPupil.toString());
        ((TextView) findViewById(R.id.pupil_first_name)).setText(pPupil.getFullName());
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(getClass().getSimpleName(), "Pupil retrieved: " + dataSnapshot.getValue(Pupil.class));
        mPupil = dataSnapshot.getValue(Pupil.class);
        fillPupilInfo(mPupil);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(getClass().getSimpleName(), "Could not retrieve pupil with Id " + mPupilId);
    }

    @Override
    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public List getItems(int pListType) {
        return null;
    }

    @Override
    public void addItem(int pListType) {

    }

    @Override
    public void removeItem(String pNode, String pChildKey) {
        DatabaseListener.super.removeItem(pNode, pChildKey);
        finish();
    }
}
