package com.gaminho.pi.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;

import java.util.Locale;


public class AddPupilDialog extends CustomAddingDialog {

    private static final int MINIMAL_NAME_LENGTH = 2;

    private RadioGroup mRGSex;
    private EditText mETPrice, mETFirstName, mETLastName;

    @Override
    public View getView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.pupil_form, null);
    }

    @Override
    public void setUpView(){
        mETFirstName = super.mView.findViewById(R.id.pupil_first_name);
        mETLastName = super.mView.findViewById(R.id.pupil_last_name);
        mETPrice = super.mView.findViewById(R.id.pupil_price);
        mRGSex = super.mView.findViewById(R.id.rg_pupil_sexe);
    }

    @Override
    public String getTitle() {
        return "Add pupil";
    }

    @Override
    DatabaseHelper.Nodes getItemNode() {
        return DatabaseHelper.Nodes.PUPILS;
    }

    @Override
    boolean isItemValid() {
        hideError();
        try {
            if (mETFirstName.getText() == null
                    || mETFirstName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError("Invalid first name");
                return false;
            } else if (mETLastName.getText() == null
                    || mETLastName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError("Invalid last name");
                return false;
            } else if (mETPrice.getText() == null
                    || Float.parseFloat(mETPrice.getText().toString()) <= 0) {
                showError("Invalid price");
                return false;
            } else if (mRGSex.getCheckedRadioButtonId() == -1) {
                showError("Please select a sexe for the pupil");
                return false;
            } else {
                return true;
            }
        } catch(Exception e){
            showError(e.getMessage());
            return false;
        }
    }

    @Override
    Pupil extractItemFromUI() {
        String firstName = mETFirstName.getText().toString();
        String lastName = mETLastName.getText().toString();
        float price = Float.parseFloat(mETPrice.getText().toString());
        int sexe = -1;
        switch(mRGSex.getCheckedRadioButtonId()){
            case R.id.rb_boy:
                sexe = Pupil.BOY;
                break;
            case R.id.rb_girl:
                sexe = Pupil.GIRL;
                break;
        }
        return new Pupil(firstName, lastName, price, sexe);
    }

    @Override
    void addedSuccessfully(Object pItem) {
        Pupil pupil = (Pupil) pItem;
        Toast.makeText(getActivity(),
                String.format(Locale.FRANCE, "Pupil added\n%s", pupil.getID()),
                Toast.LENGTH_SHORT).show();
    }
}
