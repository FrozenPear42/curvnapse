package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;
import com.bugfullabs.curvnapse.snake.ArcSnakeFragment;
import com.bugfullabs.curvnapse.snake.LineSnakeFragment;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import com.bugfullabs.curvnapse.utils.MathUtils;
import com.bugfullabs.curvnapse.utils.ResourceManager;
import com.bugfullabs.curvnapse.utils.Vec2;
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
        gridCtx.setLineDashes(5, 15);
        gridCtx.strokeRect(0, 0, mWidth, mHeight);
    }

    public synchronized void updatePowerUps(PowerUpEntity pPowerUp) {
        GraphicsContext ctx = mBonusCanvas.getGraphicsContext2D();
        ctx.drawImage(ResourceManager.getInstance().getPowerUpImage(pPowerUp.getType()), pPowerUp.getPosition().x - PowerUpEntity.WIDTH / 2, pPowerUp.getPosition().y - PowerUpEntity.HEIGHT / 2, PowerUpEntity.HEIGHT, PowerUpEntity.HEIGHT);

    }

    public synchronized void update(List<SnakeFragment> pSnakeFragments) {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setLineCap(StrokeLineCap.ROUND);

        pSnakeFragments.forEach(fragment -> {
            mainCtx.setStroke(fragment.getColor().toFXColor());
            mainCtx.setLineWidth(fragment.getWidth());

            if (fragment.getType() == SnakeFragment.Type.LINE) {
                LineSnakeFragment line = (LineSnakeFragment) fragment;
                mainCtx.strokeLine(line.getBegin().x, line.getBegin().y, line.getEnd().x, line.getEnd().y);
            } else {
                ArcSnakeFragment arc = (ArcSnakeFragment) fragment;
                mainCtx.strokeArc(arc.getCenter().x - arc.getRadius(), arc.getCenter().y - arc.getRadius(),
                        2 * arc.getRadius(), 2 * arc.getRadius(),
                        MathUtils.radToDeg(arc.getStartAngle()), MathUtils.radToDeg(arc.getAngle()), ArcType.OPEN);
            }
        });
    }
}
