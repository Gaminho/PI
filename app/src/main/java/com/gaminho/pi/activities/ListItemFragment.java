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

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.FireBaseListener2;
import com.gaminho.pi.R;
import com.gaminho.pi.adapters.RecyclerViewAdapter;
import com.gaminho.pi.beans.Course;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListItemFragment extends Fragment implements FireBaseListener2 {

    private static final String ARG_ITEM_TYPE = "item-type";
    public static final int LIST_PUPIL = 1;
    public static final int LIST_COURSE = 2;
    private int mItemType = LIST_PUPIL;

    private TextView mTVCounter;
    private RecyclerView mListViewItem;
    private ListItemListener mListener;

    private List mListItems;
    private RecyclerViewAdapter mAdapter;

    public ListItemFragment() {
    }

    public static ListItemFragment newInstance(int itemType) {
        ListItemFragment fragment = new ListItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_TYPE, itemType);
        fragment.setArguments(args);
        return fragment;
    }

    private void getItems(){
        DatabaseReference itemRef;
        Class itemClass;
        mListItems.clear();

        switch(mItemType){
            case LIST_COURSE:
                itemRef = DatabaseHelper.getNodeReference(DatabaseHelper.Nodes.COURSES);
                itemClass = Course.class;
                break;
            case LIST_PUPIL:
                itemRef = DatabaseHelper.getNodeReference(DatabaseHelper.Nodes.PUPILS);
                itemClass = Pupil.class;
                break;
            default:
                itemRef = DatabaseHelper.getNodeReference(DatabaseHelper.Nodes.PUPILS);
                itemClass = Pupil.class;
                break;
        }

        Class finalItemClass = itemClass;
        this.onStartFireBaseOperation();

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mListItems.add(snapshot.getValue(finalItemClass));
                    }
                    Log.d("PI", String.format(Locale.FRANCE,"%d items found", mListItems.size()));
                } catch (Exception e){
                    Log.d("PI", String.format(Locale.FRANCE, "Exception:\n%s ", e.getMessage()));
                }
                onEndFireBaseOperation();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("PI", String.format(Locale.FRANCE, "Failed to read value\n%s ", error.getMessage()));
                onEndFireBaseOperation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemType = getArguments().getInt(ARG_ITEM_TYPE);
        }

        mListItems = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(mListItems, mListener, mItemType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRoot = inflater.inflate(R.layout.list_layout, container, false);;
        mTVCounter = mRoot.findViewById(R.id.tv_counter);
        mListViewItem = mRoot.findViewById(R.id.list_items);
        mListViewItem.setLayoutManager(new LinearLayoutManager(mListViewItem.getContext()));
        mListViewItem.setAdapter(mAdapter);

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

    private void refreshListView(){
        if (mListViewItem != null) {
            Log.d("PI", "refreshListView");
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onStartFireBaseOperation() {
        updateTextView("Getting items");
        Log.d("PI", "Start getting " + mItemType);
    }

    @Override
    public void onEndFireBaseOperation() {
        Log.d("PI", "End getting " + mItemType);

        // If items are courses, retrieve the pupils attached by pupilId of courses
        if(mItemType == LIST_COURSE){

            updateTextView("Getting pupils for " + mListItems.size() + " courses");

            for(int i = 0 ; i < mListItems.size() ; i++){
                Course c = (Course) mListItems.get(i);
                int finalI = i;
                DatabaseHelper.getNodeReference(DatabaseHelper.Nodes.PUPILS).child(c.getPupilId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount() > 0){
                                    c.setPupil(dataSnapshot.getValue(Pupil.class));
                                    mListItems.set(finalI, c);
                                    Log.d("PI", "Pupil found (" + dataSnapshot.getKey() + ")");
                                    refreshListView();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("PI", "Cancel operation " + databaseError.getDetails());
                            }
                });
            }
        }
        // Otherwise, we can display the list
        else {
            refreshListView();
        }
        updateTextView(String.format(Locale.FRANCE, "%d items found.", mListItems.size()));
    }

    public interface ListItemListener {
        void selectItem(Object pItem, int pListType);
        void addItem(int pListType);
    }
}
