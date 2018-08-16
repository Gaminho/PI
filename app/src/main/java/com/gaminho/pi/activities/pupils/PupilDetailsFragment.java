package com.gaminho.pi.activities.pupils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.gaminho.pi.views.TextWithLabel;

public class PupilDetailsFragment extends PupilFragment {

    private TextView mTVPupilName, mTVPupilDetails;
    private CardView mCVPupilPhone;

    public PupilDetailsFragment() {
    }

    public static PupilDetailsFragment newInstance(String pPupilId) {
        PupilDetailsFragment fragment = new PupilDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ActivityPupil.EXTRA_PUPIL_ID, pPupilId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pupil_fragment_details, container, false);

        mTVPupilDetails = view.findViewById(R.id.tv_pupil);
        mTVPupilName = view.findViewById(R.id.pupil_first_name);

        mCVPupilPhone = view.findViewById(R.id.cv_phone);

        return view;
    }

    @Override
    protected void onPupilRetrieved(Pupil pPupil) {
        mTVPupilDetails.setText(pPupil.toString());
        mTVPupilName.setText(pPupil.getFullName());

        ((TextWithLabel) mCVPupilPhone.findViewById(R.id.twl_phone)).setValue(mPupil.getPhone());
        ((TextWithLabel) mCVPupilPhone.findViewById(R.id.twl_parent_phone)).setValue(mPupil.getParentPhone());

        mCVPupilPhone.setVisibility(
                (TextUtils.isEmpty(pPupil.getPhone()) && TextUtils.isEmpty(pPupil.getParentPhone())) ?
                        View.GONE : View.VISIBLE
        );

    }
}
