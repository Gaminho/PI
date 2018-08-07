package com.gaminho.pi.dialogs;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.gaminho.pi.DatabaseHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public abstract class CustomAddingDialog extends CustomDialog {

    public void addItem(DatabaseHelper.Nodes pNode){
        if(isItemValid()){
            Object item = extractItemFromUI();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = DatabaseHelper.getNodeReference(database, pNode).push();

            ref.setValue(item, (databaseError, databaseReference) -> {

                if(databaseError != null){
                    errorWhileAdding(databaseError);
                } else {
                    addedSuccessfully(item);
                    dismiss();
                }
            });
        }
    }

    @Override
    public boolean positiveClick(DialogInterface dialogInterface, int i) {
        addItem(getItemNode());
        return true;
    }

    abstract DatabaseHelper.Nodes getItemNode();
    abstract boolean isItemValid();
    abstract Object extractItemFromUI();
    abstract TextView getErrorTextView();
    abstract void addedSuccessfully(Object pItem);

    void showError(String pErrorMsg) {
        getErrorTextView().setText(pErrorMsg);
        getErrorTextView().setVisibility(View.VISIBLE);
    }

    void hideError(){
        getErrorTextView().setVisibility(View.GONE);
    }

    void errorWhileAdding(DatabaseError databaseError){
        showError(String.format(Locale.FRANCE, "Error\n%s", databaseError.getMessage()));
    }
}
