package com.gaminho.pi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;

public class AddPupil extends Fragment implements View.OnClickListener {

    private OnAddingPupilListener mListener;
    private View mRoot;

    public AddPupil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.pupil_form, container, false);;
        mRoot.findViewById(R.id.add_pupil).setOnClickListener(this);
        return mRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddingPupilListener) {
            mListener = (OnAddingPupilListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddingPupilListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_pupil:
                if(isPupilValid()){
                    Pupil p = new Pupil(((EditText) mRoot.findViewById(R.id.pupilFirstname)).getText().toString(),
                            ((EditText) mRoot.findViewById(R.id.pupilLastname)).getText().toString());
                    mListener.onAddingPupil(p);
                }
                break;
        }
    }

    private boolean isPupilValid(){
        EditText pFirstname =  mRoot.findViewById(R.id.pupilFirstname);
        EditText pLastname =  mRoot.findViewById(R.id.pupilLastname);
        String msg = null;
        if(pFirstname.getText() == null){
            msg = "Firstname is missing";
        } else if(pFirstname.getText().length() < 2){
            msg = "Please, enter a correct firstname";
        } else if(pLastname.getText() == null){
            msg = "Lastname is missing";
        } else if(pLastname.getText().length() < 2){
            msg = "Please, enter a correct lastname";
        }

        if(msg != null){
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public interface OnAddingPupilListener {
        void onAddingPupil(Pupil pPupil);
    }
}
