package com.gaminho.pi.activities.pupils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.gaminho.pi.beans.Pupil;
import com.gaminho.pi.interfaces.DatabaseListener;
import com.gaminho.pi.utils.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public abstract class PupilFragment extends Fragment implements ValueEventListener {

    protected String mPupilId;
    protected Pupil mPupil;
    protected DatabaseListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPupilId = getArguments() != null ?
                getArguments().getString(ActivityPupil.EXTRA_PUPIL_ID) : null;

        DatabaseHelper.getNodeReference(this.mListener.getDatabase(), DatabaseHelper.PUPILS)
                .child(mPupilId).addListenerForSingleValueEvent(this);
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mPupil = dataSnapshot.getValue(Pupil.class);
        onPupilRetrieved(mPupil);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(getClass().getSimpleName(), "Could not retrieve pupil with Id " + mPupilId);
    }

    protected abstract void onPupilRetrieved(Pupil pPupil);
}
