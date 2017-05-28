package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.message.*;
import com.bugfullabs.curvnapse.game.Player;
import com.bugfullabs.curvnapse.network.message.control.ServerTextMessage;
import com.bugfullabs.curvnapse.network.message.game.*;
import com.bugfullabs.curvnapse.powerup.PowerUpFactory;
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
 * Main Game Logic class, actually that is not a {@link Thread}, but uses threading via {@link Timer}
 */
public class GameThread implements ClientThread.ClientMessageListener {
    private static final Logger LOG = Logger.getLogger(GameThread.class.getName());

    private Timer mTimer;
    private List<Player> mPlayers;
    private List<PowerUpEntity> mPowerUps;
    private Map<Player, Snake> mSnakes;
    private Game mGame;
    private long mLastTime;
    private List<ClientThread> mClients;

    private boolean mWalls;
    private int mNextPowerUpTime;
    private Random mRandom;
    private int mSnakesAlive;

    private int mRoundNumber;

    private BlockingQueue<Pair<Snake, MovementAction>> mMovementQueue;
    private BlockingQueue<Player> mPlayersToKick;

    private FinishListener mListener;

    /**
     * Create new Game with given options
     *
     * @param pGame    game options
     * @param pClients clients involved in game
     */
    public GameThread(Game pGame, List<ClientThread> pClients, FinishListener pListener) {
        mClients = pClients;
        mGame = new Game(pGame);
        mRandom = new Random();
        mSnakes = new HashMap<>();
        mPowerUps = new LinkedList<>();
        mPlayers = new ArrayList<>();
        mPlayers.addAll(mGame.getPlayers());
        mMovementQueue = new ArrayBlockingQueue<>(100);
        mPlayersToKick = new ArrayBlockingQueue<>(pGame.getPlayers().size());
        mRoundNumber = 1;

        mListener = pListener;

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
        mPlayers.forEach(player -> mSnakes.put(player, createNewSnake(player)));
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
        mLastTime = System.nanoTime();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // calculate delta time
                double delta = (System.nanoTime() - mLastTime) / 1000000;
                mLastTime = System.nanoTime();

                LinkedList<SnakeFragment> fragments = new LinkedList<>();

                // Count rounds
                if (mSnakesAlive <= 1) {
                    endRound();
                    return;
                }

                // Spawn PowerUps
                if (mGame.anyPowerUps()) {
                    mNextPowerUpTime -= delta;
                    if (mNextPowerUpTime < 0) {
                        mNextPowerUpTime = mRandom.nextInt(4000) + 3000;
                        nextPowerUp();
                    }
                }
                // Process movement queue
                ArrayList<Pair<Snake, MovementAction>> movement = new ArrayList<>();
                mMovementQueue.drainTo(movement);
                movement.forEach(p -> {
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

                //collect powerup
                mSnakes.forEach((player, snake) -> {
                    mPowerUps.stream()
                            .filter(powerUp -> powerUp.isCollision(snake.getPosition()))
                            .forEach(powerUp -> collectPowerUp(powerUp, snake));

                    if (mPowerUps.removeIf(p -> p.isCollision(snake.getPosition())))
                        mClients.forEach(client -> client.sendMessage(new UpdatePowerUpMessage(mPowerUps)));
                });

                //Propagate changes
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
        PowerUp p = PowerUpFactory.fromType(pPowerUp.getType());
        PowerUp.Target t = p.getTarget();

        if (pPowerUp.getType() == PowerUp.PowerType.GLOBAL_ERASE) {
            mSnakes.forEach((__, s) -> s.erase());
            mClients.forEach(client -> client.sendMessage(new EraseMessage()));
        } else if (pPowerUp.getType() == PowerUp.PowerType.GLOBAL_RANDOM_DEATH) {
            killSnake((Snake) mSnakes.values().toArray()[new Random().nextInt(mSnakes.size())], pSnake.getColor());
        } else if (t == PowerUp.Target.SELF)
            pSnake.addPowerUp(p);
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

        List<Player> kickList = new ArrayList<>();
        mPlayersToKick.drainTo(kickList);
        mPlayers.removeAll(kickList);

        if (mPlayers.size() <= 1) {
            mListener.onFinish();
            mClients.forEach(client -> client.sendMessage(new GameOverMessage()));
            mClients.forEach(client -> client.removeListener(GameThread.this));
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mRoundNumber <= mGame.getRounds()) {
                    prepareRound();
                    startRoundCounter();
                } else {
                    mListener.onFinish();
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
        double angle = rnd.nextDouble() * Math.PI;
        return new Snake(pPlayer.getID(), randomPosition(), angle, pPlayer.getColor());
    }

    /**
     * Listens on client message
     *
     * @param pClientThread source
     * @param pMessage      message
     */
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
                mMovementQueue.add(new Pair<>(snake, MovementAction.STOP));
            else if (msg.getDirection() == ControlUpdateMessage.Direction.LEFT)
                mMovementQueue.add(new Pair<>(snake, MovementAction.LEFT));
            else if (msg.getDirection() == ControlUpdateMessage.Direction.RIGHT)
                mMovementQueue.add(new Pair<>(snake, MovementAction.RIGHT));
        }
        if (pMessage.getType() == Message.Type.DISCONNECT) {
            mGame.getPlayers()
                    .stream()
                    .filter(p -> p.getOwner() == pClientThread.getID())
                    .forEach(p -> mPlayersToKick.add(p));

        }
    }

    /**
     * Action determined by {@link ControlUpdateMessage} for movement management
     */
    public enum MovementAction {
        LEFT, RIGHT, STOP
    }

    /**
     * Listener on game end
     */
    public interface FinishListener {
        /**
         * Invoked on game finished
         */
        void onFinish();
    }

}
