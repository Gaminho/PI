package com.gaminho.pi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.gaminho.pi.interfaces.DatabaseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class FirebaseFragment extends Fragment {

    protected List mListItems;
    protected DatabaseListener mListener;

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mNotificationReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(mNotificationReceiver, new IntentFilter(IndexActivity.BROADCAST_UPDATE_LIST));
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
        if (context instanceof DatabaseListener) {
            mListener = (DatabaseListener) context;
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

}
