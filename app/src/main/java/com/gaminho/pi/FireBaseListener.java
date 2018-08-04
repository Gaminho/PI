package com.gaminho.pi;

public interface FireBaseListener {

    enum FireBaseOperation {
        ADDING, EDITING, GETTING
    }

    void onStartFireBaseOperation();
    void onEndFireBaseOperation();
}
