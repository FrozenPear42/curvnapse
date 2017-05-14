package com.bugfullabs.curvnapse.gui;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Control class for toggle button with image and gray-out effect
 */
public class ToggleImageButton extends Button {
    private boolean mIsActive;
    private ColorAdjust mColorAdjust;
    private OnToggleListener mListener;

    /**
     * Create button with graphics
     *
     * @param pImage button image
     */
    public ToggleImageButton(Image pImage) {
        ImageView image = new ImageView(pImage);
        mColorAdjust = new ColorAdjust();
        image.setEffect(mColorAdjust);
        mIsActive = true;
        setGraphic(image);
        setStyle("-fx-border-style: none");
        setOnAction(pActionEvent -> {
            setState(!mIsActive);
            if (mListener != null)
                mListener.onToggle(mIsActive);
        });
    }

    /**
     * Changes state of the button
     *
     * @param pIsOn state
     */
    public void setState(boolean pIsOn) {
        mIsActive = pIsOn;
        if (!mIsActive) {
            mColorAdjust.setBrightness(-0.9f);
        } else {
            mColorAdjust.setBrightness(0.0f);
        }
    }

    /**
     * Sets toggel listener for the button
     *
     * @param pListener listener
     */
    public void setOnToggleListener(OnToggleListener pListener) {
        mListener = pListener;
    }

    /**
     * Simple lambda-friendly listener invoked on button state change
     */
    public interface OnToggleListener {
        /**
         * Invoked on state change
         *
         * @param pState button state
         */
        void onToggle(boolean pState);
    }

}
