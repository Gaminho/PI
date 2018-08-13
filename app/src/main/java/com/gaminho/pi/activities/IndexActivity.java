package com.gaminho.pi.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.FireBaseService;
import com.gaminho.pi.R;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.gaminho.pi.dialogs.AddCourseDialog;
import com.gaminho.pi.dialogs.AddPupilDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FirebaseFragment.FirebaseDataListener, ListItemFragment.ListItemListener {

    //Intent
    public final static String EXTRA_PUPILS_LIST = "m-pupils";
    public final static String BROADCAST_UPDATE_LIST = "update-list";

    private FirebaseDatabase mDatabase;
    private Map<String, Pupil> mPupils = new HashMap<>();
    private Map<String, Course> mCourses = new HashMap<>();

    private ChildEventListener mCoursesCEV = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mCourses.put(dataSnapshot.getKey(), dataSnapshot.getValue(Course.class));
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            mCourses.put(dataSnapshot.getKey(), dataSnapshot.getValue(Course.class));
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            mCourses.remove(dataSnapshot.getKey());
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ChildEventListener mPupilsCEV = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mPupils.put(dataSnapshot.getKey(), dataSnapshot.getValue(Pupil.class));
            Log.d(getClass().getSimpleName(), "OnChildAdded: " + dataSnapshot.getValue());
            Log.d(getClass().getSimpleName(), "OnChildAdded: " + dataSnapshot.getValue(Pupil.class));
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            mPupils.put(dataSnapshot.getKey(), dataSnapshot.getValue(Pupil.class));
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            mPupils.remove(dataSnapshot.getKey());
            sendBroadcast(new Intent(BROADCAST_UPDATE_LIST));
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    FireBaseService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            FireBaseService.LocalBinder binder = (FireBaseService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
//        Intent intent = new Intent(this, FireBaseService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        retrievePupils();
        retrieveCourses();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseHelper.getNodeReference(mDatabase, DatabaseHelper.Nodes.PUPILS)
                .removeEventListener(mPupilsCEV);
        DatabaseHelper.getNodeReference(mDatabase, DatabaseHelper.Nodes.COURSES)
                .removeEventListener(mCoursesCEV);
//        unbindService(mConnection);
//        mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Launch service for firebase
//        startService(new Intent(IndexActivity.this, FireBaseService.class));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view,
//                "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, new FragmentWeek()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment;
        if (id == R.id.nav_pupils) {
            fragment = ListItemFragment.newInstance(ListItemFragment.LIST_PUPIL);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
        } else if (id == R.id.nav_courses) {
            fragment = ListItemFragment.newInstance(ListItemFragment.LIST_COURSE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
        } else if (id == R.id.nav_share) {
            fragment = FragmentWeek.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void selectItem(Object pItem) {
        StringBuffer strB = new StringBuffer("Click on ");
        strB.append(pItem.toString());
//        strB.append(pItem instanceof Course ?
//                "a course with date: " + ((Course) pItem).getDate()
//                : "a pupil with id: : " + ((Pupil)pItem). getFirstname());
        Toast.makeText( this, strB.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addItem(int pListType) {
        switch(pListType){
            case ListItemFragment.LIST_COURSE:
//                Intent intent = new Intent(this, ActivityCourse.class);
//                intent.putExtra(EXTRA_PUPILS_LIST, new ArrayList<>(mPupils.values()));
                AddCourseDialog a = new AddCourseDialog();
                Bundle args = new Bundle();
                args.putSerializable(EXTRA_PUPILS_LIST, new ArrayList<>(mPupils.values()));
                a.setArguments(args);
                a.show(getFragmentManager(), "MyAddingDialog");
                //startActivity(intent);
                break;
            case ListItemFragment.LIST_PUPIL:
//                Intent i = new Intent(IndexActivity.this, DialogActivity.class);
//                startActivity(i);
                new AddPupilDialog().show(getFragmentManager(), "MyPupilAddingDialog");
                break;
        }
    }

    @Override
    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public List getItems(int pListType) {
        fetchCourses();
        List items = new ArrayList();
        switch (pListType){
            case ListItemFragment.LIST_COURSE:
                items.addAll(mCourses.values().stream().filter(course -> course.getPupil() != null).sorted(
                        (course1, course2) -> Long.valueOf(course2.getDate()).compareTo(course1.getDate()))
                        .collect(Collectors.toList()));
                break;
            case ListItemFragment.LIST_PUPIL:
                items.addAll(mPupils.values().stream().sorted(Comparator.comparing(Pupil::getFirstname))
                        .collect(Collectors.toList()));
                break;
        }
        return items;
    }

    private void retrievePupils(){
        if(mDatabase != null){
            DatabaseHelper.getNodeReference(mDatabase, DatabaseHelper.Nodes.PUPILS)
                    .addChildEventListener(mPupilsCEV);
        }
    }

    private void retrieveCourses(){
        if(mDatabase != null){
            DatabaseHelper.getNodeReference(mDatabase, DatabaseHelper.Nodes.COURSES)
                    .addChildEventListener(mCoursesCEV);
        }
    }

    private void fetchCourses(){
        mCourses.forEach((courseId, course) -> {
            if(course.getPupil() == null && mPupils.containsKey(course.getPupilId())){
                course.setPupil(mPupils.get(course.getPupilId()));
                mCourses.put(courseId, course);
                Log.d(getClass().getSimpleName(), "Course " + courseId + " is now linked with a pupil");
            }
        });
    }
    
}
