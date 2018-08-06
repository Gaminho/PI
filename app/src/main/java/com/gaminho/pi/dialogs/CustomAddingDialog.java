package com.gaminho.pi.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gaminho.pi.R;
import com.gaminho.pi.activities.ListItemFragment;

public abstract class CustomAddingDialog extends DialogFragment {

    protected View mView;
    protected OnAddingDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = getView();
        builder.setView(mView);
        builder.setTitle(getTitle());
        builder.setPositiveButton("positive", (dialogInterface, i) -> {
        });
        builder.setNegativeButton("cancel", (dialogInterface, i) -> dismiss());
        setUpView();
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog) getDialog();
        if(d != null) {
            d.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(v -> positiveClick(d, 1));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemFragment.ListItemListener) {
            mListener = (OnAddingDialogListener) context;
        } else {
            mListener = null;
        }
    }


    public abstract View getView();
    public abstract void setUpView();
    public abstract String getTitle();
    public abstract boolean positiveClick(DialogInterface dialogInterface, int i);

    public interface OnAddingDialogListener{
        void addItem(Object pItemToAdd);
    }
}
