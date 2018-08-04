package com.gaminho.pi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    public enum Nodes {
        PUPILS, COURSES
    }

    private static final String PUPILS_NODE = "pupils";
    private static final String COURSES_NODE = "courses";

    public static DatabaseReference getNodeReference(Nodes pNodes) {
        DatabaseReference ref = null;
        switch (pNodes) {
            case PUPILS:
                ref = FirebaseDatabase.getInstance().getReference().child(PUPILS_NODE);
                break;
            case COURSES:
                ref = FirebaseDatabase.getInstance().getReference().child(COURSES_NODE);
                break;
            default:
                break;
        }
        return ref;
    }
}
