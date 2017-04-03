package com.bugfullabs.curvnapse.snake;

import com.bugfullabs.curvnapse.player.PlayerColor;
import com.bugfullabs.curvnapse.utils.Vec2;

import java.io.Serializable;

public class LineSnakeFragment extends SnakeFragment implements Serializable {
    private Vec2 mBegin;
    private Vec2 mEnd;

    private Vec2 mConstEdge;
    private Vec2 mEdge;


    public LineSnakeFragment(Vec2 pBegin, Vec2 pEnd, double pAngle, PlayerColor pColor, double pWidth) {
        super(Type.LINE, pColor, pWidth);
        mBegin = pBegin;
        mEnd = pEnd;
        mConstEdge = Vec2.directed(pWidth, pAngle + Math.PI / 2);
        mEdge = Vec2.sub(mEnd, mBegin);
    }

    public void updateHead(Vec2 pEnd) {
        mEnd = pEnd;
        mEdge = Vec2.sub(mEnd, mBegin);
    }

    @Override
    public boolean isCollision(Vec2 pPoint) {
        //FIXME: Change beginning point to rect origin
        Vec2 point = new Vec2(mBegin, pPoint);
        double dotA = Vec2.dot(mConstEdge, point);
        double dotB = Vec2.dot(mEdge, point);

        return 0 <= dotA && dotA <= Vec2.dot(mConstEdge, mConstEdge) &&
                0 <= dotB && dotB <= Vec2.dot(mEdge, mEdge);
    }

    public Vec2 getBegin() {
        return mBegin;
    }

    public Vec2 getEnd() {
        return mEnd;
    }

}
