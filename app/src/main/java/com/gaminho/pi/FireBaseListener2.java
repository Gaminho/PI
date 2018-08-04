package com.gaminho.pi;

public interface FireBaseListener2 {

    enum FireBaseOperation {
        ADDING, EDITING, GETTING
    }

    void onStartFireBaseOperation();
    void onEndFireBaseOperation();
}
