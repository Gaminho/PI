package com.gaminho.pi.activities.pupils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;

public class PupilEditFragment extends PupilFragment {

    private TextView mTVPupilName;
    private EditText mETPupilDetails;



    public PupilEditFragment() {
    }

    public static PupilEditFragment newInstance(String pPupilId) {
        PupilEditFragment fragment = new PupilEditFragment();
        Bundle args = new Bundle();
        args.putString(ActivityPupil.EXTRA_PUPIL_ID, pPupilId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pupil_fragment_edit, container, false);

        mETPupilDetails = view.findViewById(R.id.tv_pupil);
        mTVPupilName = view.findViewById(R.id.pupil_first_name);

        return view;
    }

    @Override
    protected void onPupilRetrieved(Pupil pPupil) {
        mETPupilDetails.setText(pPupil.toString());
        mTVPupilName.setText(pPupil.getFullName());
    }
}
