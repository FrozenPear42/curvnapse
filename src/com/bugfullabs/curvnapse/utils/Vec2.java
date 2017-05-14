package com.bugfullabs.curvnapse.utils;

import java.io.Serializable;

/**
 * Serializable  implementation of 2D vector
 */
public class Vec2 implements Serializable {
    /**
     * vector x
     */
    public double x;
    /**
     * vector y
     */
    public double y;

    /**
     * Zero vector constructor
     */
    public Vec2() {
        x = 0;
        y = 0;
    }

    /**
     * Vector from - to
     *
     * @param pV1 from
     * @param pV2 to
     */
    public Vec2(Vec2 pV1, Vec2 pV2) {
        x = pV2.x - pV1.x;
        y = pV2.y - pV1.y;
    }

    /**
     * Copy of Vector
     */
    public Vec2(Vec2 pVec) {
        x = pVec.x;
        y = pVec.y;
    }

    /**
     * Vector form coordinates
     *
     * @param pX x
     * @param pY y
     */
    public Vec2(double pX, double pY) {
        x = pX;
        y = pY;
    }

    /**
     * Length of vector
     *
     * @return length of vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalize vector to length 1
     *
     * @return normalized vector
     */
    public Vec2 normalize() {
        return new Vec2(x / length(), y / length());
    }

    /**
     * Returns orientation of vector
     *
     * @return angle in radians
     */
    public double angle() {
        double res = Math.atan(y / x);
        if (x < 0 && res > 0)
            return res - Math.PI;
        if (x < 0 && res < 0)
            return res + Math.PI;
        return res;

    }

    /**
     * Multipy pV by scalar
     *
     * @param pV vector
     * @param pS scalar
     * @return pV * pS
     */
    public static Vec2 times(Vec2 pV, double pS) {
        return new Vec2(pV.x * pS, pV.y * pS);
    }

    /**
     * Add two vectors
     *
     * @param pV1 v1
     * @param pV2 v2
     * @return v1 + v2
     */
    public static Vec2 add(Vec2 pV1, Vec2 pV2) {
        return new Vec2(pV1.x + pV2.x, pV1.y + pV2.y);
    }

    /**
     * Subtract two vectors
     *
     * @param pV1 v1
     * @param pV2 v2
     * @return v1 - v2
     */
    public static Vec2 sub(Vec2 pV1, Vec2 pV2) {
        return new Vec2(pV1.x - pV2.x, pV1.y - pV2.y);
    }

    /**
     * Create vector of given length directed in given angle
     *
     * @param pLength length
     * @param pAngle  angle
     * @return vector
     */
    public static Vec2 directed(double pLength, double pAngle) {
        return new Vec2(pLength * Math.cos(pAngle), pLength * Math.sin(pAngle));
    }

    /**
     * Calculate dot product of given vectors
     *
     * @param pV1 v1
     * @param pV2 v2
     * @return v1 dot v2
     */
    public static double dot(Vec2 pV1, Vec2 pV2) {
        return pV1.x * pV2.x + pV1.y * pV2.y;
    }

}
