package com.gaminho.pi.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaminho.pi.DatabaseHelper;
import com.gaminho.pi.R;
import com.gaminho.pi.beans.Pupil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddPupilDialog extends CustomAddingDialog {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final int MINIMAL_NAME_LENGTH = 2;
    private static final String PHONE_PATTERN = "^0[1-7][0-9]{8}$";

    private RadioGroup mRGSex;
    private EditText mETPrice, mETFirstName, mETLastName, mETPhone, mETParentPhone;
    private TextView mTVAddress;

    private Place mPlace = null;


    @Override
    public int getViewResourceId() {
        return R.layout.pupil_form;
    }

    @Override
    public void setUpView(){
        mETFirstName = super.mView.findViewById(R.id.pupil_first_name);
        mETLastName = super.mView.findViewById(R.id.pupil_last_name);
        mETPhone = super.mView.findViewById(R.id.pupil_phone);
        mETParentPhone = super.mView.findViewById(R.id.pupil_phone_parent);
        mTVAddress = super.mView.findViewById(R.id.tv_address);
        mETPrice = super.mView.findViewById(R.id.pupil_price);
        mRGSex = super.mView.findViewById(R.id.rg_pupil_sexe);

        //FIXME: need to find a way to launch place auto complete from dialog fragment
        super.mView.findViewById(R.id.btn_address).setOnClickListener(view -> {
            try {

                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("FR").build();

                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .setFilter(typeFilter)
                                .build(getActivity());

                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(getClass().getSimpleName(), "GooglePlayServicesRepairableException", e);
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(getClass().getSimpleName(), "GooglePlayServicesNotAvailableException", e);
                // TODO: Handle the error.
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                if(mTVAddress != null){
                    mTVAddress.setText(place.getAddress());
                }
                mPlace = place;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(getClass().getSimpleName(), status.getStatusMessage());
                mPlace = null;
            } else if (resultCode == RESULT_CANCELED) {
                Log.e(getClass().getSimpleName(), "Error");
            }
        }
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
            if (mRGSex.getCheckedRadioButtonId() == -1) {
                showError(getString(R.string.pupil_no_sex));
                return false;
            } else if (mETFirstName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError(getString(R.string.pupil_invalid_first_name));
                return false;
            } else if (mETLastName.getText().toString().length() < MINIMAL_NAME_LENGTH) {
                showError(getString(R.string.pupil_invalid_last_name));
                return false;
            } else if (!isPhoneNumberValid(mETPhone.getText().toString())) {
                showError(getString(R.string.pupil_invalid_phone));
                return false;
            } else if (!isPhoneNumberValid(mETParentPhone.getText().toString())) {
                showError(getString(R.string.pupil_invalid_phone_parent));
                return false;
            } else if (!isAmountValid(mETPrice.getText().toString())){
                showError(getString(R.string.pupil_invalid_hourly_price));
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

        if(mPlace != null){
            Log.d(getClass().getSimpleName(), "Place is not null: " + mPlace.toString());
            pupil.setGooglePlace(mPlace);
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