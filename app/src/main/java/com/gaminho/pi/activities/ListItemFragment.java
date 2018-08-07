package com.gaminho.pi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListItemFragment extends Fragment {

    private static final String ARG_ITEM_TYPE = "item-type";
    public static final int LIST_PUPIL = 1;
    public static final int LIST_COURSE = 2;
    private int mItemType = LIST_PUPIL;

    private TextView mTVCounter;
    private ListItemListener mListener;

    private List mListItems;
    private RecyclerViewAdapter mAdapter;

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNotificationReceiver);
    }

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
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mNotificationReceiver, new IntentFilter(IndexActivity.UPDATE_LIST));
        updateUI();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemType = getArguments().getInt(ARG_ITEM_TYPE);
        }

        mListItems = new ArrayList();
        mAdapter = new RecyclerViewAdapter(mListItems, mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRoot = inflater.inflate(R.layout.list_layout, container, false);;
        mTVCounter = mRoot.findViewById(R.id.tv_counter);

        RecyclerView mListViewItem = mRoot.findViewById(R.id.list_items);
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

    private void updateUI(){
        mListItems.clear();
        mListItems.addAll(mListener.getItems(mItemType));
        updateTextView(String.format(Locale.FRANCE, "%d items found.", mListItems.size()));
        mAdapter.notifyDataSetChanged();
    }

    public interface ListItemListener {
        void selectItem(Object pItem);
        void addItem(int pListType);
        FirebaseDatabase getDatabase();
        List getItems(int pListType);
    }
}
