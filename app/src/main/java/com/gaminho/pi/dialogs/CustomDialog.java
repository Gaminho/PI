package com.gaminho.pi.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gaminho.pi.R;

public abstract class CustomDialog extends DialogFragment {

    protected View mView;
    private TextView mTVError;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.default_dialog, null);

        mTVError = view.findViewById(R.id.tv_error);
        mTVError.setVisibility(View.GONE);

        mView = getView();
        ((FrameLayout) view.findViewById(R.id.dialog_content)).addView(mView);

        builder.setView(view);
        setUpView();

        builder.setTitle(getTitle());
        builder.setPositiveButton("positive", (dialogInterface, i) -> { /* use to cancel default dismissing */ });
        builder.setNegativeButton("cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if(d != null) {
            // Add custom action for positive click
            d.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(v -> positiveClick(d, 1));
        }
    }

    public abstract View getView();
    public abstract void setUpView();
    public abstract String getTitle();
    public abstract boolean positiveClick(DialogInterface dialogInterface, int i);

    void showError(String pErrorMsg) {
        mTVError.setText(pErrorMsg);
        mTVError.setVisibility(View.VISIBLE);
    }

    void hideError(){
        mTVError.setVisibility(View.GONE);
    }
}
