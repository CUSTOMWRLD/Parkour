package com.customwrld.parkour.util;

import org.bukkit.util.Vector;

public class VectorUtil
{
    public static Vector getQuadraticBezierAt(final Vector p1, final Vector cp, final Vector p2, final double t) {
        final Vector a = getVectorBetween(p1, cp, t);
        final Vector b = getVectorBetween(cp, p2, t);
        return getVectorBetween(a, b, t);
    }

    public static Vector getVectorBetween(final Vector v1, final Vector v2, final double perc) {
        final Vector v1c = v1.clone();
        final Vector v2c = v2.clone();
        return v1c.add(v2c.subtract(v1).multiply(perc));
    }
}
