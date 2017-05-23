package com.bugfullabs.curvnapse.gui;


import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;
import com.bugfullabs.curvnapse.snake.ArcSnakeFragment;
import com.bugfullabs.curvnapse.snake.HeadSnakeFragment;
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

import java.util.LinkedList;
import java.util.List;

/**
 * Control class for game board
 */
public class Board extends VBox {
    private int mWidth;
    private int mHeight;
    private Canvas mMainCanvas;
    private Canvas mHeadCanvas;
    private Canvas mBonusCanvas;
    private Canvas mGridCanvas;

    /**
     * Create board in given dimensions
     *
     * @param pWidth  width
     * @param pHeight height
     */
    public Board(int pWidth, int pHeight) {
        super(10.0f);
        setPadding(new Insets(10.0f));
        setAlignment(Pos.CENTER);
        StackPane root = new StackPane();
        mWidth = pWidth;
        mHeight = pHeight;
        mMainCanvas = new Canvas(mWidth, mHeight);
        mHeadCanvas = new Canvas(mWidth, mHeight);
        mBonusCanvas = new Canvas(mWidth, mHeight);
        mGridCanvas = new Canvas(mWidth, mHeight);

        root.getChildren().add(mMainCanvas);
        root.getChildren().add(mHeadCanvas);
        root.getChildren().add(mBonusCanvas);
        root.getChildren().add(mGridCanvas);
        getChildren().add(root);

        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.BLACK);
        mainCtx.fillRect(0, 0, mWidth, mHeight);

        GraphicsContext gridCtx = mGridCanvas.getGraphicsContext2D();
        gridCtx.setLineWidth(5);
        gridCtx.setStroke(Color.WHITE);
        gridCtx.setLineDashes(5, 15);
        gridCtx.strokeRect(0, 0, mWidth, mHeight);
    }

    /**
     * Redraw PowerUp layer
     *
     * @param pPowerUp PowerUps to be drawn
     */
    public synchronized void updatePowerUps(LinkedList<PowerUpEntity> pPowerUp) {
        GraphicsContext ctx = mBonusCanvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, mWidth, mHeight);
        pPowerUp.forEach(powerUp ->
                ctx.drawImage(ResourceManager.getInstance().getPowerUpImage(powerUp.getType()),
                        powerUp.getPosition().x - PowerUpEntity.WIDTH / 2,
                        powerUp.getPosition().y - PowerUpEntity.HEIGHT / 2,
                        PowerUpEntity.HEIGHT,
                        PowerUpEntity.HEIGHT));
    }

    /**
     * Draw given {@link SnakeFragment}s
     *
     * @param pSnakeFragments fragments to be drawn
     */
    public synchronized void update(List<SnakeFragment> pSnakeFragments) {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        GraphicsContext headCtx = mHeadCanvas.getGraphicsContext2D();

        mainCtx.setLineCap(StrokeLineCap.ROUND);
        headCtx.clearRect(0, 0, mWidth, mHeight);

        pSnakeFragments.forEach(fragment -> {
            mainCtx.setStroke(fragment.getColor().toFXColor());
            mainCtx.setLineWidth(fragment.getWidth());
            mainCtx.setFill(Color.WHITE);

            headCtx.setFill(fragment.getColor().toFXColor());
            headCtx.setLineWidth(fragment.getWidth());


            if (fragment.getType() == SnakeFragment.Type.LINE) {
                LineSnakeFragment line = (LineSnakeFragment) fragment;
                mainCtx.strokeLine(line.getBegin().x, line.getBegin().y, line.getEnd().x, line.getEnd().y);

//                DEBUG - Vector direction
//                double x1 = line.getBegin().x + 10 * Math.cos(line.getAngle());
//                double y1 = line.getBegin().y + 10 * Math.sin(line.getAngle());
//                mainCtx.strokeLine(line.getBegin().x, line.getBegin().y, x1, y1);
//                mainCtx.fillArc(x1 - 3, y1 - 3, 6, 6, 0, 360, ArcType.ROUND);

            } else if (fragment.getType() == SnakeFragment.Type.ARC) {
                ArcSnakeFragment arc = (ArcSnakeFragment) fragment;
                mainCtx.strokeArc(arc.getCenter().x - arc.getRadius(), arc.getCenter().y - arc.getRadius(),
                        2 * arc.getRadius(), 2 * arc.getRadius(),
                        MathUtils.radToDeg(arc.getStartAngle()), MathUtils.radToDeg(arc.getAngle()), ArcType.OPEN);
            } else if (fragment.getType() == SnakeFragment.Type.HEAD) {
                HeadSnakeFragment head = (HeadSnakeFragment) fragment;
                headCtx.fillArc(head.getPosition().x - head.getWidth() / 2, head.getPosition().y - head.getWidth() / 2, head.getWidth(), head.getWidth(), 0, 360, ArcType.ROUND);
            }
        });
    }

    /**
     * Erase board
     */
    public synchronized void erase() {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.BLACK);
        mainCtx.fillRect(0, 0, mWidth, mHeight);
    }

    /**
     * Draws collision point in given color
     *
     * @param pCollisionPoint collision point
     * @param pKillerColor    color
     */
    public synchronized void drawCollision(Vec2 pCollisionPoint, SerializableColor pKillerColor) {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        mainCtx.setFill(pKillerColor.toFXColor());

        mainCtx.fillArc(pCollisionPoint.x - 5, pCollisionPoint.y - 5, 10, 10, 0, 360, ArcType.ROUND);
    }

    /**
     * Clear everything on board
     */
    public synchronized void clear() {
        GraphicsContext mainCtx = mMainCanvas.getGraphicsContext2D();
        GraphicsContext headCtx = mHeadCanvas.getGraphicsContext2D();
        GraphicsContext bonusCtx = mBonusCanvas.getGraphicsContext2D();
        mainCtx.setFill(Color.BLACK);
        mainCtx.fillRect(0, 0, mWidth, mHeight);
        headCtx.clearRect(0, 0, mWidth, mHeight);
        bonusCtx.clearRect(0, 0, mWidth, mHeight);
    }
}
