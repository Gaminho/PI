package com.gaminho.pi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseFragment extends Fragment {

    protected List mListItems;
    protected FirebaseDataListener mListener;

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNotificationReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mNotificationReceiver, new IntentFilter(IndexActivity.BROADCAST_UPDATE_LIST));
        refreshView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListItems = new ArrayList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FirebaseDataListener) {
            mListener = (FirebaseDataListener) context;
        } else {
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void onBroadcastReceived(){
        refreshView();
    }

    protected abstract void refreshView();


    public interface FirebaseDataListener {
        FirebaseDatabase getDatabase();
        List getItems(int pListType);
    }


}
