package com.gaminho.pi.interfaces;

import com.gaminho.pi.utils.DatabaseHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface DatabaseListener {

    FirebaseDatabase getDatabase();
    List getItems(int pListType);
    void addItem(int pListType);
    default void removeItem(String pNode, String pChildKey){
        if(getDatabase() != null){
            DatabaseHelper.getNodeReference(getDatabase(), pNode).child(pChildKey).removeValue();
        }
    }
}
