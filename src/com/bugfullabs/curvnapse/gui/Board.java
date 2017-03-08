package com.bugfullabs.curvnapse.gui;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Board extends Pane {
    private int mWidth;
    private int mHeight;
    private Canvas mMainCanvas;
    private Canvas mBonusCanvas;
    private Canvas mGridCanvas;

    public Board(int pWidth, int pHeight) {
        mWidth = pWidth;
        mHeight = pHeight;
        mMainCanvas = new Canvas(mWidth, mHeight);
        mBonusCanvas = new Canvas(mWidth, mHeight);
        mGridCanvas = new Canvas(mWidth, mHeight);
        getChildren().add(mMainCanvas);
        getChildren().add(mBonusCanvas);
        getChildren().add(mGridCanvas);

        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.BLACK);
        mainCtx.fillRect(0, 0, mWidth, mHeight);

        GraphicsContext gridCtx = mGridCanvas.getGraphicsContext2D();
        gridCtx.setLineWidth(5);
        gridCtx.setStroke(Color.WHITE);
        gridCtx.strokeRect(0, 0, mWidth, mHeight);
    }

    public void update() {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.GREEN);
        mainCtx.fillArc(100, 100, 10, 10, 0, 360, ArcType.ROUND);
    }
}
