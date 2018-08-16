package com.gaminho.pi.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    public static final String PUPILS = "pupils";
    public static final String COURSES = "classes";

    public static DatabaseReference getNodeReference(FirebaseDatabase pDatabase, String pNodes) {
        DatabaseReference ref = pDatabase.getReference();
        switch (pNodes) {
            case PUPILS:
                ref = ref.child(PUPILS);
                break;
            case COURSES:
                ref = ref.child(COURSES);
                break;
            default:
                break;
        }
        return ref;
    }
}
