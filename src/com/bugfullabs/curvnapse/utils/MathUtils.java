package com.bugfullabs.curvnapse.utils;

/**
 * Several static math functions
 */
public class MathUtils {
    /**
     * Converts radians to degrees
     * @param pRad radians
     * @return degrees
     */
    public static double radToDeg(double pRad) {
        return pRad * 180 / Math.PI;
    }

    /**
     * Narmalize angle to match [0, 2pi]
     * @param x angle
     * @return normalized angle
     */
    public static double normalizeAngle(double x) {
        while (x >= 2 * Math.PI) x -= 2 * Math.PI;
        while (x < 0) x += 2 * Math.PI;
        return x;
    }
}
