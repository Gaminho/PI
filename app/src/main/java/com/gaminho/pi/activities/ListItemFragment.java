package com.gaminho.pi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.adapters.RecyclerViewAdapter;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListItemFragment extends Fragment {

    private static final String ARG_ITEM_TYPE = "item-type";
    public static final int LIST_PUPIL = 1;
    public static final int LIST_COURSE = 2;
    private int mItemType = LIST_PUPIL;

    private TextView mTVCounter;
    private RecyclerView mListViewItem;
    private ListItemListener mListener;

    public ListItemFragment() {
    }

    public static ListItemFragment newInstance(int itemType) {
        ListItemFragment fragment = new ListItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_TYPE, itemType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemType = getArguments().getInt(ARG_ITEM_TYPE);
        }

        String databaseTable;
        Class itemClass;

        switch(mItemType){
            case LIST_COURSE:
                databaseTable = "classes";
                itemClass = Course.class;
                break;
            case LIST_PUPIL:
                databaseTable = "pupils";
                itemClass = Pupil.class;
                break;
            default:
                databaseTable = "pupils";
                itemClass = Pupil.class;
                break;
        }

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(databaseTable);

        updateTextView("Getting items");
        Class finalItemClass = itemClass;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pMsg;
                List items = new ArrayList<>();

                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if(mItemType == LIST_COURSE){
                            Course course = snapshot.getValue(Course.class);
                            String pupilId = course.getPupilId();

                            Log.d("PI", "1. pupilId: " + pupilId);
                            Query pupilRef = database.getReference().child("pupils").child(pupilId);
                            pupilRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount() == 0){
                                        Log.d("PI", "2. No pupil found with id: " + pupilId);
//                                        items.add(snapshot.getValue(finalItemClass));
                                    } else {
                                        Pupil p = dataSnapshot.getValue(Pupil.class);
                                        Log.d("PI", "3. Pupil found: " + p.getFirstname());
                                        course.setPupil(p);
//                                        items.add(course);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getActivity(), "Unable to get Pupil with id " + pupilId, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //FIXME Add only after getting pupils
                            items.add(course);
                            Log.d("PI", "3. Course added: " + course.getPupil());
                        } else {
                            items.add(snapshot.getValue(finalItemClass));
                        }
                    }
                    pMsg = String.format(Locale.FRANCE,"%d pupils found", items.size());
                    fillListView(items);
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

        View mRoot = inflater.inflate(R.layout.list_layout, container, false);;
        mTVCounter = mRoot.findViewById(R.id.tv_counter);
        mListViewItem = mRoot.findViewById(R.id.list_items);
        mRoot.findViewById(R.id.fab_add_item).setOnClickListener(v -> {
            if(mListener != null){
                this.mListener.addItem(this.mItemType);
            } else {
                Toast.makeText(getContext(), "no listener", Toast.LENGTH_SHORT).show();
            }
        });

        return mRoot;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemListener) {
            mListener = (ListItemListener) context;
        } else {
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateTextView(String pMsg) {
        if (mTVCounter != null) {
            mTVCounter.setText(pMsg);
        }
    }

    private void fillListView(List pPupilList){
        if (mListViewItem != null) {
            mListViewItem.setLayoutManager(new LinearLayoutManager(mListViewItem.getContext()));
            mListViewItem.setAdapter(new RecyclerViewAdapter(pPupilList, mListener, mItemType));
        }
    }

    public interface ListItemListener {
        void selectItem(Object pItem, int pListType);
        void addItem(int pListType);
    }
}
