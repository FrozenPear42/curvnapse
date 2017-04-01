package com.bugfullabs.curvnapse.network.server;


import com.bugfullabs.curvnapse.FlowManager;
import com.bugfullabs.curvnapse.game.Game;
import com.bugfullabs.curvnapse.network.client.ServerConnector;
import com.bugfullabs.curvnapse.network.message.ControlUpdateMessage;
import com.bugfullabs.curvnapse.network.message.Message;
import com.bugfullabs.curvnapse.network.message.ServerTextMessage;
import com.bugfullabs.curvnapse.network.message.SnakeFragmentsMessage;
import com.bugfullabs.curvnapse.player.Player;
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

    public GameThread(Game pGame, List<ClientThread> pClients) {
        mClients = pClients;
        mGame = pGame;
        mTimer = new Timer();
        mSnakes = new HashMap<>();
        mGame.getPlayers().forEach(player -> mSnakes.put(player, createNewSnake(player)));

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double delta = 1000 / 60; //FIXME: WCALE NIE
                LinkedList<SnakeFragment> fragments = new LinkedList<>();

                mSnakes.forEach((player, snake) -> {
                    snake.step(delta);
                    if (snake.isAlive())
                        fragments.add(snake.getLastFragment());

                    if (snake.getPosition().x < 0 || snake.getPosition().x > mGame.getBoardWidth())
                        snake.kill();
                    if (snake.getPosition().y < 0 || snake.getPosition().y > mGame.getBoardHeight())
                        snake.kill();
                });

                mClients.forEach(client -> client.sendMessage(new SnakeFragmentsMessage(fragments)));

            }
        }, 0, 1000 / 60);
    }

    public void stop() {
        mTimer.cancel();
    }

    private Snake createNewSnake(Player pPlayer) {
        Random rnd = new Random();
        //TODO: add margin, avoid conflict
        int x = rnd.nextInt(mGame.getBoardWidth());
        int y = rnd.nextInt(mGame.getBoardHeight());
        double angle = rnd.nextDouble() * Math.PI;
        //TODO: TEMPORAL WORKAROUND
        x = 250;
        y = 250;
        angle = 0;

        return new Snake(pPlayer.getID(), new Vec2(x, y), angle, pPlayer.getColor());
    }

    @Override
    public void onClientMessage(ClientThread pClientThread, Message pMessage) {
        if (pMessage.getType() == Message.Type.CONTROL_UPDATE) {
            ControlUpdateMessage msg = (ControlUpdateMessage) pMessage;
            Snake snake = mSnakes.entrySet().stream().filter(e -> e.getKey().getID() == msg.getPlayerID()).findFirst().get().getValue();
            if (msg.getAction() == ControlUpdateMessage.Action.UP)
                snake.turnEnd();
            else if (msg.getDirection() == ControlUpdateMessage.Direction.LEFT)
                snake.turnLeft();
            else if (msg.getDirection() == ControlUpdateMessage.Direction.RIGHT)
                snake.turnRight();
        }
    }
}
