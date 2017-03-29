package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.snake.LineSnakeFragment;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;

import java.util.List;

public class Board extends VBox {
    private StackPane mRoot;
    private int mWidth;
    private int mHeight;
    private Canvas mMainCanvas;
    private Canvas mBonusCanvas;
    private Canvas mGridCanvas;

    public Board(int pWidth, int pHeight) {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        mRoot = new StackPane();
        mWidth = pWidth;
        mHeight = pHeight;
        mMainCanvas = new Canvas(mWidth, mHeight);
        mBonusCanvas = new Canvas(mWidth, mHeight);
        mGridCanvas = new Canvas(mWidth, mHeight);

        mRoot.getChildren().add(mMainCanvas);
        mRoot.getChildren().add(mBonusCanvas);
        mRoot.getChildren().add(mGridCanvas);
        getChildren().add(mRoot);

        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.BLACK);
        mainCtx.fillRect(0, 0, mWidth, mHeight);

        GraphicsContext gridCtx = mGridCanvas.getGraphicsContext2D();
        gridCtx.setLineWidth(5);
        gridCtx.setStroke(Color.WHITE);
        gridCtx.strokeRect(0, 0, mWidth, mHeight);
    }

    public synchronized void update(List<SnakeFragment> pSnakeFragments) {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setStroke(Color.GREEN);
        mainCtx.setLineWidth(5);
        mainCtx.setLineCap(StrokeLineCap.ROUND);

        pSnakeFragments.forEach(fragment -> {
            if (fragment.getType() == SnakeFragment.Type.LINE) {
                LineSnakeFragment line = (LineSnakeFragment) fragment;
                mainCtx.strokeLine(line.getBegin().x, line.getBegin().y, line.getEnd().x, line.getEnd().y);
            } else {
                mainCtx.fillArc(100, 100, 10, 10, 0, 360, ArcType.ROUND);
            }
        });
    }
}
