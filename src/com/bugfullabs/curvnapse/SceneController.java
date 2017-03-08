package com.bugfullabs.curvnapse;

public class SceneController {
    private static SceneController ourInstance = new SceneController();

    public static SceneController getInstance() {
        return ourInstance;
    }
    private SceneController() {
    }
}
