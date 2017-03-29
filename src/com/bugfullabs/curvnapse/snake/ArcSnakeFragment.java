package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;

import java.io.Serializable;

public class ArcSnakeFragment extends SnakeFragment implements Serializable{

    public ArcSnakeFragment(PlayerColor pColor, double pWidth) {
        super(Type.ARC, pColor, pWidth);
    }

    public void updateHead(double pAngle) {

    }
}
