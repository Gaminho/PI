package com.gaminho.pi.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public abstract class CustomDialog extends DialogFragment {

    protected View mView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = getView();
        builder.setView(mView);
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
}
