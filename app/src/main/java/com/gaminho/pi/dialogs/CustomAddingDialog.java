package com.gaminho.pi.dialogs;

import android.content.DialogInterface;

import com.gaminho.pi.utils.DatabaseHelper;
import com.gaminho.pi.beans.Pupil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public abstract class CustomAddingDialog extends CustomDialog {

    public void addItem(String pNode){
        if(isItemValid()){
            Object item = extractItemFromUI();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = DatabaseHelper.getNodeReference(database, pNode).push();

            if(item instanceof Pupil) {
                ((Pupil) item).setID(ref.getKey());
            }

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

    abstract String getItemNode();
    abstract boolean isItemValid();
    abstract Object extractItemFromUI();
    abstract void addedSuccessfully(Object pItem);

    void errorWhileAdding(DatabaseError databaseError){
        showError(String.format(Locale.FRANCE, "Error\n%s", databaseError.getMessage()));
    }
}
