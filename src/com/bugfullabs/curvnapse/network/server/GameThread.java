package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.ControlUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.SnakeFragmentsMessage;
import com.bugfullabs.curvnapse.network.message.SpawnPowerUpMessage;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.snake.Snake;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.util.*;

public class GameThread implements ClientThread.ClientListener {
    private Timer mTimer;
    private Map<Player, Snake> mSnakes;
    private Game mGame;
    private long mLastTime;
    private List<ClientThread> mClients;

    private boolean mWalls;
    private int mNextPowerUpTime;
    private Random mRandom;


    public GameThread(Game pGame, List<ClientThread> pClients) {
        mClients = pClients;
        mGame = pGame;
        mRandom = new Random();
        mTimer = new Timer();
        mSnakes = new HashMap<>();

        mWalls = false;

        mNextPowerUpTime = mRandom.nextInt(4000) + 500;
        mGame.getPlayers().forEach(player -> mSnakes.put(player, createNewSnake(player)));

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double delta = 1000 / 60; //FIXME: WCALE NIE
                LinkedList<SnakeFragment> fragments = new LinkedList<>();

                mNextPowerUpTime -= delta;
                if (mNextPowerUpTime < 0) {
                    mNextPowerUpTime = mRandom.nextInt(4000) + 3000;
                    nextPowerUp();
                }

                mSnakes.forEach((player, snake) -> {
                    snake.step(delta);
                    if (snake.isAlive())
                        fragments.add(snake.getLastFragment());

                    if (!mWalls) {
                        if (snake.getPosition().x < 0)
                            snake.teleport(new Vec2(mGame.getBoardWidth(), snake.getPosition().y));
                        else if (snake.getPosition().x > mGame.getBoardWidth())
                            snake.teleport(new Vec2(0, snake.getPosition().y));

                        if (snake.getPosition().y < 0)
                            snake.teleport(new Vec2(snake.getPosition().x, mGame.getBoardHeight()));
                        else if (snake.getPosition().y > mGame.getBoardHeight())
                            snake.teleport(new Vec2(snake.getPosition().x, 0));

                    }
                });

                mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));

            }
        }, 0, 1000 / 60);
    }

    public void stop() {
        mTimer.cancel();
    }

    private void nextPowerUp() {
        int id;

        do
            id = mRandom.nextInt(mGame.getPowerUps().length);
        while (!mGame.getPowerUps()[id]);

        PowerUp.PowerType type = PowerUp.PowerType.values()[id];
        Vec2 pos = randomPosition();
        mClients.forEach(client -> client.sendMessage(new SpawnPowerUpMessage(type, pos)));
    }

    private Vec2 randomPosition() {
        int margin = 50;
        int x = mRandom.nextInt(mGame.getBoardWidth() - margin * 2) + margin;
        int y = mRandom.nextInt(mGame.getBoardHeight() - margin * 2) + margin;
        return new Vec2(x, y);
    }

    private Snake createNewSnake(Player pPlayer) {
        Random rnd = new Random();
        //TODO: avoid conflict
        double angle = rnd.nextDouble() * Math.PI;

        return new Snake(pPlayer.getID(), randomPosition(), angle, pPlayer.getColor());
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        if (pMessage.getType() == Message.Type.CONTROL_UPDATE) {
            ControlUpdateMessage msg = (ControlUpdateMessage) pMessage;

            Snake snake = mSnakes.entrySet()
                    .stream()
                    .filter(e -> e.getKey().getID() == msg.getPlayerID())
                    .findFirst()
                    .get()
                    .getValue();

            if (msg.getAction() == ControlUpdateMessage.Action.UP)
                snake.turnEnd();
            else if (msg.getDirection() == ControlUpdateMessage.Direction.LEFT)
                snake.turnLeft();
            else if (msg.getDirection() == ControlUpdateMessage.Direction.RIGHT)
                snake.turnRight();
        }
    }
}
