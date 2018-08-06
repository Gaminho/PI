package com.gaminho.pi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    public enum Nodes {
        PUPILS, COURSES
    }

    private static final String PUPILS_NODE = "pupils";
    private static final String COURSES_NODE = "classes";

    public static DatabaseReference getNodeReference(FirebaseDatabase pDatabase, Nodes pNodes) {
        DatabaseReference ref = null;
        switch (pNodes) {
            case PUPILS:
                ref = pDatabase.getReference().child(PUPILS_NODE);
                break;
            case COURSES:
                ref = pDatabase.getReference().child(COURSES_NODE);
                break;
            default:
                break;
        }
        ref.keepSynced(true);
        return ref;
    }
}
