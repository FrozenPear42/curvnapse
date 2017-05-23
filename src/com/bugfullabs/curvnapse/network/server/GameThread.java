package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.player.Player;
import com.bugfullabs.curvnapse.utils.SerializableColor;
import com.bugfullabs.curvnapse.powerup.PowerUp;
import com.bugfullabs.curvnapse.powerup.PowerUpEntity;
import com.bugfullabs.curvnapse.snake.Snake;
import com.bugfullabs.curvnapse.snake.SnakeFragment;
import com.bugfullabs.curvnapse.utils.Vec2;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;


/**
 * Main Game Logic class, actually that is not a {@link Thread}, but uses them via {@link Timer}
 */
public class GameThread implements ClientThread.ClientMessageListener {
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

    private BlockingQueue<Pair<Snake, MovementAction>> mMovementQueue;

    /**
     * Create new Game with given options
     *
     * @param pGame    game options
     * @param pClients clients involved in game
     */
    public GameThread(Game pGame, List<ClientThread> pClients) {
        mClients = pClients;
        mGame = pGame;
        mRandom = new Random();
        mSnakes = new HashMap<>();
        mPowerUps = new LinkedList<>();
        mMovementQueue = new ArrayBlockingQueue<>(20);

        mRoundNumber = 1;

        prepareRound();
        startRoundCounter();
    }

    /**
     * Prepare new round - reset all the collections, timers, generate new start positions
     */
    private void prepareRound() {
        mTimer = new Timer();
        mSnakes.clear();
        mPowerUps.clear();
        mMovementQueue.clear();
        mWalls = false;

        mNextPowerUpTime = mRandom.nextInt(4000) + 500;
        mGame.getPlayers().forEach(player -> mSnakes.put(player, createNewSnake(player)));
        mSnakesAlive = mSnakes.size();
        mClients.forEach(client -> client.sendMessage(new NextRoundMessage(mRoundNumber)));

        LinkedList<SnakeFragment> fragments = new LinkedList<>();
        mSnakes.forEach((player, snake) -> fragments.add(snake.getHead()));
        mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));
    }

    /**
     * Start new round countdown
     */
    private void startRoundCounter() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int times = 3;

            @Override
            public void run() {
                if (times == 0) {
                    mClients.forEach(client -> client.sendMessage(new ServerTextMessage("Round starts now...")));
                    this.cancel();
                    runRound();
                } else
                    mClients.forEach(client -> client.sendMessage(new ServerTextMessage(String.format("Round will start in %d seconds...", times))));
                times--;
            }
        }, 0, 1000);
    }

    /**
     * Start actual round
     */
    private void runRound() {
        mMovementQueue.clear();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // calculate delta time
                double delta = 1000 / 60; //FIXME: WCALE NIE
                LinkedList<SnakeFragment> fragments = new LinkedList<>();

                // Count rounds
                if (mSnakesAlive <= 1) {
                    endRound();
                    return;
                }

                // Spawn PowerUps
                mNextPowerUpTime -= delta;
                if (mNextPowerUpTime < 0) {
                    mNextPowerUpTime = mRandom.nextInt(4000) + 3000;
                    nextPowerUp();
                }

                // Process movement queue
                ArrayList<Pair<Snake, MovementAction>> movement = new ArrayList<>();
                mMovementQueue.drainTo(movement);
                movement.forEach(p -> {
                    System.out.println(p.getKey());
                    switch (p.getValue()) {
                        case LEFT:
                            p.getKey().turnLeft();
                            break;
                        case RIGHT:
                            p.getKey().turnRight();
                            break;
                        case STOP:
                            p.getKey().turnEnd();
                            break;
                    }
                });

                // Update each snake
                mSnakes.forEach((player, snake) -> {

                    if (snake.isDead())
                        return;

                    //move snake
                    snake.step(delta);

                    // traverse through walls
                    if (!mWalls) {
                        if (snake.getPosition().x < 0)
                            snake.teleport(new Vec2(mGame.getBoardWidth(), snake.getPosition().y));
                        else if (snake.getPosition().x > mGame.getBoardWidth())
                            snake.teleport(new Vec2(0, snake.getPosition().y));

                        if (snake.getPosition().y < 0)
                            snake.teleport(new Vec2(snake.getPosition().x, mGame.getBoardHeight()));
                        else if (snake.getPosition().y > mGame.getBoardHeight())
                            snake.teleport(new Vec2(snake.getPosition().x, 0));
                    } else {
                        if (snake.getPosition().x < 0 || snake.getPosition().x > mGame.getBoardWidth() ||
                                snake.getPosition().y < 0 || snake.getPosition().y > mGame.getBoardHeight())
                            snake.kill();
                    }

                    fragments.add(snake.getLastFragment());
                });

                ///Check collisions
                mSnakes.forEach(((player, snake) -> {
                    if (snake.isInvisible() || snake.isDead())
                        return;

                    for (Snake otherSnake : mSnakes.values()) {
                        if (otherSnake == snake) {
                            if (snake.checkSelfCollision()) {
                                killSnake(snake, otherSnake.getColor());
                                break;
                            }
                        } else {
                            if (otherSnake.isCollisionAtPoint(snake.getPosition())) {
                                killSnake(snake, otherSnake.getColor());
                                break;
                            }
                        }
                    }
                }));

                mSnakes.forEach((player, snake) -> {
                    //collect powerup
                    mPowerUps.stream()
                            .filter(powerUp -> powerUp.isCollision(snake.getPosition()))
                            .forEach(powerUp -> collectPowerUp(powerUp, snake));

                    if (mPowerUps.removeIf(p -> p.isCollision(snake.getPosition())))
                        mClients.forEach(client -> client.sendMessage(new UpdatePowerUpMessage(mPowerUps)));
                });

                //Propagate changes
                fragments.clear();
                mSnakes.forEach(((pPlayer, pSnake) -> fragments.addAll(pSnake.getFragments())));
                mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));

            }
        }, 0, 1000 / 60);

    }

    /**
     * Collect PowerUp entity
     *
     * @param pPowerUp PowerUp Entity to be collected
     * @param pSnake   Snake which collected that PowerUp
     */
    private void collectPowerUp(PowerUpEntity pPowerUp, Snake pSnake) {
        PowerUp.Target t = PowerUp.getTarget(pPowerUp.getType());
        PowerUp p = PowerUp.fromType(pPowerUp.getType());

        if (pPowerUp.getType() == PowerUp.PowerType.GLOBAL_ERASE) {
            mSnakes.forEach((__, s) -> s.erase());
            mClients.forEach(client -> client.sendMessage(new EraseMessage()));
        } else if (t == PowerUp.Target.SELF)
            pSnake.addPowerUp(PowerUp.fromType(pPowerUp.getType()));
        else if (t == PowerUp.Target.ALL)
            mSnakes.forEach((__, s) -> s.addPowerUp(p));
        else if (t == PowerUp.Target.OTHERS)
            mSnakes.forEach((__, s) -> {
                if (s != pSnake) s.addPowerUp(p);
            });
    }

    /**
     * End round - keep board for several seconds before wiping it out, than check if all rounds are done
     */
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

    /**
     * Kill given snake with given color(for killer indication)
     *
     * @param pSnake       Snake to be killed
     * @param pKillerColor kill indicator color
     */
    private void killSnake(Snake pSnake, SerializableColor pKillerColor) {
        if (pSnake.isDead())
            return;

        LOG.info("Snake lines " + pSnake.getLinesCount() + " abd arcs: " + pSnake.getArcsCount());

        pSnake.kill();
        mClients.forEach(client -> client.sendMessage(new SnakeKilledMessage(pSnake.getPosition(), pKillerColor)));
        mSnakesAlive -= 1;
        mSnakes.forEach((player, snake) -> {
            if (snake != pSnake && !snake.isDead())
                player.setPoints(player.getPoints() + 1);
        });
        mGame.getPlayers().sort(Comparator.comparingInt(Player::getPoints).reversed());
        mClients.forEach((client) -> client.sendMessage(new GameUpdateMessage(mGame)));
    }

    /**
     * Spawn {@link PowerUpEntity}
     */
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

    /**
     * Generate random position
     *
     * @return position
     */
    private Vec2 randomPosition() {
        int margin = 50;
        int x = mRandom.nextInt(mGame.getBoardWidth() - margin * 2) + margin;
        int y = mRandom.nextInt(mGame.getBoardHeight() - margin * 2) + margin;
        return new Vec2(x, y);
    }

    /**
     * Create new Snake for given Player
     *
     * @param pPlayer Player
     * @return Snake
     */
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

            LOG.info(String.format("Move %d to %s %s", msg.getPlayerID(), msg.getDirection().name(), msg.getAction().name()));

            if (msg.getAction() == ControlUpdateMessage.Action.UP)
                mMovementQueue.add(new Pair<>(snake, MovementAction.STOP));
            else if (msg.getDirection() == ControlUpdateMessage.Direction.LEFT)
                mMovementQueue.add(new Pair<>(snake, MovementAction.LEFT));
            else if (msg.getDirection() == ControlUpdateMessage.Direction.RIGHT)
                mMovementQueue.add(new Pair<>(snake, MovementAction.RIGHT));
        }
        if (pMessage.getType() == Message.Type.DISCONNECT) {
            //TODO: fix game when player leaves
            LOG.warning("Player left");
        }
    }

    public enum MovementAction {
        LEFT, RIGHT, STOP
    }

}
