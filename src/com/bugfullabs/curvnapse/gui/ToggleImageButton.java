package com.bugfullabs.curvnapse.gui;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToggleImageButton extends Button {
    private ImageView mImage;
    private boolean mIsActive;
    private ColorAdjust mColorAdjust;
    private OnToggleListener mListener;

    public ToggleImageButton(Image pImage) {
        mImage = new ImageView(pImage);
        mColorAdjust = new ColorAdjust();
        mImage.setEffect(mColorAdjust);
        mIsActive = true;
        setGraphic(mImage);
        setStyle("-fx-border-style: none");
        setOnAction(pActionEvent -> {
            setState(!mIsActive);
            if (mListener != null)
                mListener.onToggle(mIsActive);
        });
    }


    public void setOnToggleListener(OnToggleListener pListener) {
        mListener = pListener;
    }

    public void setState(boolean pIsOn) {
        mIsActive = pIsOn;
        if (!mIsActive) {
            mColorAdjust.setBrightness(-0.9f);
        } else {
            mColorAdjust.setBrightness(0.0f);
        }
    }

    public interface OnToggleListener {
        void onToggle(boolean pIsActive);
    }

}
