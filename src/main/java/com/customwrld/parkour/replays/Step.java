package com.customwrld.parkour.replays;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@Getter @Setter
public class Step implements Comparable<Step>{

    private int index;
    private long timestamp;

    private final Type type;

    private final double px;
    private final double py;
    private final double pz;

    private final double vx;
    private final double vy;
    private final double vz;

    private final float yaw;
    private final float pitch;

    public Step(Type type, final Location location, final Vector velocity) {
        this.timestamp = System.currentTimeMillis();

        this.type = type;

        this.px = location.getX();
        this.py = location.getY();
        this.pz = location.getZ();

        this.vx = velocity.getX();
        this.vy = velocity.getY();
        this.vz = velocity.getZ();

        this.yaw = location.getPitch();
        this.pitch = location.getYaw();
    }

    @Override public boolean equals(Object o) {
        if(!(o instanceof Step other)) { return false; }
        return this.timestamp == other.timestamp;
    }

    @Override public int hashCode() {
        return Long.hashCode(this.timestamp);
    }

    @Override public int compareTo(@NotNull Step o) {
        return Long.compare(this.timestamp, o.timestamp);
    }

    public enum Type {
        NORMAL,

        JUMP,
        //LAND,

        SPRINT_START,
        SPRINT_STOP,

        SNEAK_START,
        SNEAK_STOP,

        SWING,

        SWIM_START,
        SWIM_STOP
    }

}
