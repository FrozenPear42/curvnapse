package com.bugfullabs.curvnapse.utils;

import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindingBank {
    private List<Pair<KeyCode, KeyCode>> mKeys;
    private Map<Pair<KeyCode, KeyCode>, Boolean> mTaken;

    public KeyBindingBank() {
        mKeys = new ArrayList<>();
        mTaken = new HashMap<>();

        mKeys.add(new Pair<>(KeyCode.LEFT, KeyCode.RIGHT));
        mKeys.add(new Pair<>(KeyCode.Q, KeyCode.E));
        mKeys.add(new Pair<>(KeyCode.Z, KeyCode.C));
        mKeys.add(new Pair<>(KeyCode.B, KeyCode.M));
        mKeys.add(new Pair<>(KeyCode.Y, KeyCode.I));
    }

}

