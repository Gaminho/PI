package com.gaminho.pi.activities.pupils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.gaminho.pi.R;
import com.gaminho.pi.activities.FragmentWeek;
import com.gaminho.pi.activities.ListItemFragment;
import com.gaminho.pi.interfaces.DatabaseListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ActivityPupil extends AppCompatActivity implements DatabaseListener {

    //Intent
    public final static String EXTRA_PUPIL_ID = "pupil-id";
    public final static String EXTRA_ACTION = "pupil-action";
    public final static int ACTION_DETAILS = 0;
    public final static int ACTION_EDIT = 1;

    private FirebaseDatabase mDatabase;
    private String mPupilId;
    private PupilFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pupil_activity);
        mDatabase = FirebaseDatabase.getInstance();
        mPupilId = getIntent().getStringExtra(EXTRA_PUPIL_ID);

        PupilFragment fragment = getIntent().getIntExtra(EXTRA_ACTION, ACTION_DETAILS) == ACTION_EDIT ?
                PupilEditFragment.newInstance(mPupilId) :
                PupilDetailsFragment.newInstance(mPupilId);

        loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pupil, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(mCurrentFragment instanceof PupilDetailsFragment);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!(mCurrentFragment instanceof PupilDetailsFragment)) {
            loadFragment(PupilDetailsFragment.newInstance(mPupilId));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            loadFragment(PupilEditFragment.newInstance(mPupilId));
            return true;
        } else if (id == R.id.action_remove){
            //FIXME add Confirmation Dialog
            //this.removeItem(DatabaseHelper.PUPILS, mPupilId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(PupilFragment pFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.pupil_fragment, pFragment).commit();
        mCurrentFragment = pFragment;
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
