package com.bugfullabs.curvnapse.utils;

public class MathUtils {
    public static double radToDeg(double pRad) {
        return pRad * 180 / Math.PI;
    }

    public static double normalizeAngle(double x) {
        while (x >= 2 * Math.PI) x -= 2 * Math.PI;
        while (x < 0) x += 2 * Math.PI;
        return x;
    }
}
