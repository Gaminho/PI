package com.gaminho.pi.dialogs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddPupilDialog extends CustomAddingDialog {

    private static final int MINIMAL_NAME_LENGTH = 2;
    private static final String PHONE_PATTERN = "^0[1-7][0-9]{8}$";

    private RadioGroup mRGSex;
    private EditText mETPrice, mETFirstName, mETLastName, mETPhone, mETParentPhone, mETAddress;

    @Override
    public View getView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.pupil_form, null);
    }

    @Override
    public void setUpView(){
        mETFirstName = super.mView.findViewById(R.id.pupil_first_name);
        mETLastName = super.mView.findViewById(R.id.pupil_last_name);
        mETPhone = super.mView.findViewById(R.id.pupil_phone);
        mETParentPhone = super.mView.findViewById(R.id.pupil_phone_parent);
        mETAddress = super.mView.findViewById(R.id.pupil_address);
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

    //FIXME check the validation and catch expression of float parsing
    @Override
    boolean isItemValid() {
        hideError();
        try {

            if (mETFirstName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError("Invalid first name");
                return false;
            } else if (mETLastName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError("Invalid last name");
                return false;
            } else if (!isAmountValid(mETPrice.getText().toString())){
                showError("Invalid price");
                return false;
            } else if (mRGSex.getCheckedRadioButtonId() == -1) {
                showError("Please select a sexe for the pupil");
                return false;
            } else if (!isPhoneNumberValid(mETPhone.getText().toString())) {
                showError("Invalid phone number");
                return false;
            } else if (!isPhoneNumberValid(mETParentPhone.getText().toString())) {
                showError("Invalid parent phone number");
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

        Pupil pupil = new Pupil(firstName, lastName, price, sexe);

        if(isPhoneNumberValid(mETPhone.getText().toString())){
            pupil.setPhone(mETPhone.getText().toString());
        }

        if(isPhoneNumberValid(mETParentPhone.getText().toString())){
            pupil.setParentPhone(mETParentPhone.getText().toString());
        }

        return pupil;
    }

    @Override
    void addedSuccessfully(Object pItem) {
        Pupil pupil = (Pupil) pItem;
        Toast.makeText(getActivity(),
                String.format(Locale.FRANCE, "Pupil added\n%s", pupil.getID()),
                Toast.LENGTH_SHORT).show();
    }

    private boolean isPhoneNumberValid(String pPhone){
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(pPhone);
        return (pPhone.length() == 10 && matcher.matches()) || pPhone.length() == 0;
    }

    private boolean isAmountValid(String pAmount){
        if(pAmount.length() <= 0){
            return false;
        } else {
            try {
                return Float.parseFloat(pAmount) >= 0;
            } catch (Exception e){
                return false;
            }
        }
    }
}
