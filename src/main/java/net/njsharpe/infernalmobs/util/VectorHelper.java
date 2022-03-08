package net.njsharpe.infernalmobs.util;

import org.bukkit.util.Vector;

public class VectorHelper {

    public static double horizontalDistance(Vector vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
    }

}
