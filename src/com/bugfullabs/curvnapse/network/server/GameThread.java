package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;
import com.bugfullabs.curvnapse.snake.Snake;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.util.*;
import java.util.logging.Logger;

public class GameThread implements ClientThread.ClientListener {
    private static final Logger LOG = Logger.getLogger(GameThread.class.getName());

    private Timer mTimer;
    private Map<Player, Snake> mSnakes;
    private LinkedList<PowerUpEntity> mPowerUps;
    private Game mGame;
    private long mLastTime;
    private List<ClientThread> mClients;

    private boolean mWalls;
    private int mNextPowerUpTime;
    private Random mRandom;
    private int mSnakesAlive;

    private int mRoundNumber;


    public GameThread(Game pGame, List<ClientThread> pClients) {
        mClients = pClients;
        mGame = pGame;
        mRandom = new Random();
        mSnakes = new HashMap<>();
        mPowerUps = new LinkedList<>();

        mRoundNumber = 1;

        prepareRound();
        startRoundCounter();
    }

    private void prepareRound() {
        mTimer = new Timer();
        mSnakes.clear();
        mPowerUps.clear();
        mWalls = false;

        mNextPowerUpTime = mRandom.nextInt(4000) + 500;
        mGame.getPlayers().forEach(player -> mSnakes.put(player, createNewSnake(player)));
        mSnakesAlive = mSnakes.size();
        mClients.forEach(client -> client.sendMessage(new NextRoundMessage(mRoundNumber)));

        LinkedList<SnakeFragment> fragments = new LinkedList<>();
        mSnakes.forEach((player, snake) -> fragments.add(snake.getHead()));
        mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));
    }

    private void startRoundCounter() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int times = 3;

            @Override
            public void run() {
                if (times == 0) {
                    mClients.forEach(client -> client.sendMessage(new ServerTextMessage(String.format("Round starts now...", times))));
                    this.cancel();
                    runRound();
                } else
                    mClients.forEach(client -> client.sendMessage(new ServerTextMessage(String.format("Round will start in %d seconds...", times))));
                times--;
            }
        }, 0, 1000);
    }

    private void runRound() {
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mSnakesAlive <= 1) {
                    endRound();
                    return;
                }

                double delta = 1000 / 60; //FIXME: WCALE NIE
                LinkedList<SnakeFragment> fragments = new LinkedList<>();

                //Spawn Powerups
                mNextPowerUpTime -= delta;
                if (mNextPowerUpTime < 0) {
                    mNextPowerUpTime = mRandom.nextInt(4000) + 3000;
                    nextPowerUp();
                }


                mSnakes.forEach((player, snake) -> {
                    //move snake
                    snake.step(delta);

                    Vec2 orgPosition = snake.getPosition();
                    //collect powerup
                    mPowerUps.forEach(powerUp -> {
                        if (powerUp.isCollision(snake.getPosition())) {
                            PowerUp.Target t = PowerUp.getTarget(powerUp.getType());

                            if (t == PowerUp.Target.SELF)
                                snake.addPowerUp(PowerUp.fromType(powerUp.getType()));
                            else if (t == PowerUp.Target.OTHERS)
                                mSnakes.forEach((__, s) -> {
                                    if (s != snake) s.addPowerUp(PowerUp.fromType(powerUp.getType()));
                                });
                            else if (t == PowerUp.Target.ALL)
                                mSnakes.forEach((__, s) -> s.addPowerUp(PowerUp.fromType(powerUp.getType())));
                            else {
                                if (powerUp.getType() == PowerUp.PowerType.GLOBAL_ERASE) {
                                    mSnakes.forEach((__, s) -> s.erase());
                                    mClients.forEach(client -> client.sendMessage(new EraseMessage()));
                                }
                            }
                        }
                    });

                    if (mPowerUps.removeIf(powerUp -> powerUp.isCollision(orgPosition))) {
                        mClients.forEach(client -> client.sendMessage(new UpdatePowerUpMessage(mPowerUps)));
                    }

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

                    if (!snake.isDead())
                        fragments.add(snake.getLastFragment());
                });

                mSnakes.forEach(((player, snake) -> {
                    if (snake.isInvisible() || snake.isDead())
                        return;

                    for (Snake otherSnake : mSnakes.values()) {
                        if (otherSnake == snake) {
                            if (snake.checkSelfCollision()) {
                                killSnake(snake, true);
                                LOG.info("SELF KILL " + snake);
                                break;
                            }
                        } else {
                            if (otherSnake.isCollisionAtPoint(snake.getPosition())) {
                                killSnake(snake, false);
                                break;
                            }
                        }
                    }
                }));
                mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));

            }
        }, 0, 1000 / 60);

    }


    private void endRound() {
        mTimer.cancel();
        mRoundNumber += 1;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mRoundNumber <= mGame.getRounds()) {
                    prepareRound();
                    startRoundCounter();
                } else {
                    mClients.forEach(client -> client.sendMessage(new GameOverMessage()));
                    mClients.forEach(client -> client.removeListener(GameThread.this));
                }
            }
        }, 1000);
    }

    public void stop() {
        mTimer.cancel();
    }


    private void killSnake(Snake pSnake, boolean pSelfKill) {
        if (pSnake.isDead())
            return;

        pSnake.kill();
        mClients.forEach(client -> client.sendMessage(new SnakeKilledMessage(pSnake.getPosition(), pSelfKill)));
        mSnakesAlive -= 1;
        mSnakes.forEach((player, snake) -> {
            if (snake != pSnake && !snake.isDead())
                player.setPoints(player.getPoints() + 1);
        });
        mGame.getPlayers().sort(Comparator.comparingInt(Player::getPoints).reversed());
        mClients.forEach((client) -> client.sendMessage(new GameUpdateMessage(mGame)));
    }


    private void nextPowerUp() {
        int id;
        do
            id = mRandom.nextInt(mGame.getPowerUps().length);
        while (!mGame.getPowerUps()[id]);

        PowerUp.PowerType type = PowerUp.PowerType.values()[id];
        Vec2 pos = randomPosition();
        PowerUpEntity entity = new PowerUpEntity(pos, type);
        mPowerUps.add(entity);
        mClients.forEach(client -> client.sendMessage(new UpdatePowerUpMessage(mPowerUps)));
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
    public synchronized void onClientMessage(ClientThread pClientThread, Message pMessage) {
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
