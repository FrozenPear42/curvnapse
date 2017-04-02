package com.bugfullabs.curvnapse.utils;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class ResourceManager {
    private static ResourceManager ourInstance = new ResourceManager();

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    private static final int POWERUP_WIDTH = 48;
    private static final int POWERUP_HEIGHT = 48;

    private Image mPowerUpSheet;
    private ArrayList<WritableImage> mPowerUpImages;


    private ResourceManager() {
        mPowerUpSheet = new Image("/resources/power_ups.png");
        mPowerUpImages = new ArrayList<>();

        int cols = (int) (mPowerUpSheet.getWidth() / POWERUP_WIDTH);
        int rows = (int) (mPowerUpSheet.getHeight() / POWERUP_HEIGHT);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                WritableImage img = new WritableImage(mPowerUpSheet.getPixelReader(),
                        POWERUP_WIDTH * x, POWERUP_HEIGHT * y, POWERUP_WIDTH, POWERUP_HEIGHT);
                mPowerUpImages.add(img);
            }
        }
    }

    public WritableImage getPowerUpImage(PowerUp.PowerType pType) {
        return mPowerUpImages.get(pType.ordinal());
    }
}
