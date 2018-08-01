package com.gaminho.pi.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SeePupils extends Fragment {

    private TextView mTVPupilsCount;
    private ListView mListViewPupils;

    public SeePupils() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pupils");
        updateTextView("Getting pupils");
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
                    fillListView(pupils);
                } catch (Exception e){
                    pMsg = String.format(Locale.FRANCE, "Exception:\n%s ", e.getMessage());
                }

                updateTextView(pMsg);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                updateTextView(
                        String.format(Locale.FRANCE, "Failed to read value\n%s ", error.getMessage()));
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRoot = inflater.inflate(R.layout.see_pupils, container, false);;
        mTVPupilsCount = mRoot.findViewById(R.id.pupils_count);
        mListViewPupils = mRoot.findViewById(R.id.lv_pupils);
        return mRoot;
    }

    private void updateTextView(String pMsg) {
        if (mTVPupilsCount != null) {
            mTVPupilsCount.setText(pMsg);
        }
    }

    private void fillListView(List<Pupil> pPupilList){
        if (mListViewPupils != null) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, pPupilList.stream().map(p -> p.getFirstname()).collect(Collectors.toList()));
            mListViewPupils.setAdapter(adapter);
        }
    }
}
