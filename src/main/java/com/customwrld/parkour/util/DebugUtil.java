package com.customwrld.parkour.util;

import com.customwrld.parkour.Parkour;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class DebugUtil {

    public static BukkitTask drawPoint(Location location, Color color) {
        return Bukkit.getScheduler().runTaskTimer(Parkour.getParkour(),
                () -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, new Particle.DustOptions(color, 1)),
                0, 2);
    }

    public static BukkitTask drawPoints(Map<Location, Color> locations) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getParkour(), () -> {
            for(Map.Entry<Location, Color> ent : locations.entrySet()) {
                var location = ent.getKey();
                var color = ent.getValue();

                location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, new Particle.DustOptions(color, 1));
            } }, 0, 2);
    }

    public static BukkitTask drawLines(List<Pair<Location, Color>> points) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getParkour(), () -> {
            Pair<Location, Color> prev = null;
            for(Pair<Location, Color> pair : points) {
                var location = pair.getLeft();
                var color = pair.getRight();

                location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, new Particle.DustOptions(color, 1));

                if(prev != null) {
                    var prevLocation = prev.getLeft();
                    var prevColor = prev.getRight();

                    var stepVector = location.toVector().subtract(prevLocation.toVector()).divide(new Vector(10, 10, 10));

                    var origin = prevLocation.toVector();
                    for(var i = 0; i < 9; i++) {
                        var midVector = origin.add(stepVector);
                        location.getWorld().spawnParticle(Particle.REDSTONE, midVector.toLocation(prevLocation.getWorld()),
                                0, new Particle.DustOptions(prevColor, 1));
                    }
                }

                prev = pair;
            }


            }, 0, 5);
    }

    //TODO Find a better name
    public static BukkitTask drawBezierSplineQuadraticEnds(List<Pair<Location, Color>> cps) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getParkour(), () -> {
            for(final var it = cps.listIterator(); it.nextIndex() + 2 < cps.size();) {
                var first = it.next();

                var firstLoc = first.getLeft();
                var color = first.getRight();

                var cpLoc = it.next().getLeft();
                var nextLoc = it.next().getLeft();

                var world = firstLoc.getWorld();

//                world.spawnParticle(Particle.REDSTONE, firstLoc, 0, new Particle.DustOptions(color, 1));

//                var stepVector = location.toVector().subtract(prevLocation.toVector()).divide(new Vector(10, 10, 10));

//                var origin = prevLocation.toVector();
                for(var i = 0; i < 10; i++) {
                    var midVector = VectorUtil.getQuadraticBezierAt(firstLoc.toVector(), cpLoc.toVector(), nextLoc.toVector(), i / 10d);
                    world.spawnParticle(Particle.REDSTONE, midVector.toLocation(world),
                            0, new Particle.DustOptions(color, 1));
                }

                it.previous(); //Move back the cursor
            }

        }, 0, 5);
    }

}
