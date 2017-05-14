package com.bugfullabs.curvnapse.utils;

import com.bugfullabs.curvnapse.powerup.PowerUp;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

/**
 * Simple class that manages all resorces nneded for game
 */
public class ResourceManager {
    /**
     * Singleton instance
     */
    private static ResourceManager ourInstance = new ResourceManager();

    /**
     * Returns instance
     * @return instance
     */
    public static ResourceManager getInstance() {
        return ourInstance;
    }

    /**
     * PowerUp element width
     */
    private static final int POWERUP_WIDTH = 48;
    /**
     * PowerUp element height
     */
    private static final int POWERUP_HEIGHT = 48;

    /**
     * Array of PowerUp textures
     */
    private ArrayList<WritableImage> mPowerUpImages;

    /**
     * Just constructor
     */
    private ResourceManager() {
        Image powerUpSheet = new Image("/resources/power_ups.png");
        mPowerUpImages = new ArrayList<>();

        int cols = (int) (powerUpSheet.getWidth() / POWERUP_WIDTH);
        int rows = (int) (powerUpSheet.getHeight() / POWERUP_HEIGHT);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                WritableImage img = new WritableImage(powerUpSheet.getPixelReader(),
                        POWERUP_WIDTH * x, POWERUP_HEIGHT * y, POWERUP_WIDTH, POWERUP_HEIGHT);
                mPowerUpImages.add(img);
            }
        }
    }

    /**
     * Returns PowerUp image of given type
     * @param pType PowerUp type
     * @return image
     */
    public WritableImage getPowerUpImage(PowerUp.PowerType pType) {
        return mPowerUpImages.get(pType.ordinal());
    }
}
