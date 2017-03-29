package com.bugfullabs.curvnapse.utils;

import java.io.Serializable;

public class Vec2 implements Serializable {
    public double x;
    public double y;

    public Vec2() {
        x = 0;
        y = 0;
    }

    public Vec2(Vec2 pVec) {
        x = pVec.x;
        y = pVec.y;
    }

    public Vec2(double pX, double pY) {
        x = pX;
        y = pY;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public static Vec2 times(Vec2 pV, double pS) {
        return new Vec2(pV.x * pS, pV.y * pS);
    }

    public static Vec2 add(Vec2 pV1, Vec2 pV2) {
        return new Vec2(pV1.x + pV2.x, pV1.y + pV2.y);
    }

    public static Vec2 sub(Vec2 pV1, Vec2 pV2) {
        return new Vec2(pV1.x - pV2.x, pV1.y - pV2.y);
    }

    public static Vec2 directed(double pLength, double pAngle) {
        return new Vec2(pLength * Math.cos(pAngle), pLength * Math.sin(pAngle));
    }

}
