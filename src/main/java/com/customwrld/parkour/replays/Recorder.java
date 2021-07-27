package com.customwrld.parkour.replays;

import com.customwrld.customlib.util.MetadataUtil;
import com.customwrld.parkour.Parkour;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Recorder {

    public static final String METADATA_KEY = "activeRecorder";
    @Getter private static final List<Recorder> activeRecorders = Collections.synchronizedList(new ArrayList<>());

    private final List<Step> recordedSteps = Collections.synchronizedList(new ArrayList<>());

    private final Player player;
    @Setter private boolean isRecording;

    private long startTime = Long.MIN_VALUE;
    private long endTime = Long.MIN_VALUE;

    private BukkitTask task;

    public Recorder(Player player) {
        this.player = player;
    }

    public static Recorder fromPlayer(Player player) {
        return MetadataUtil.getValue(player, METADATA_KEY, Parkour.getParkour(), null, Recorder.class, false);
    }

    public void start() {
        this.isRecording = true;
        this.startTime = System.currentTimeMillis();

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getParkour(), () -> {
            if(!this.isRecording()) { this.task.cancel(); }

            var currentLocation = this.player.getLocation();
            var currentVelocity = this.player.getVelocity();

            if(!this.recordedSteps.isEmpty()) {
                var lastStep = this.recordedSteps.get(this.recordedSteps.size() - 1);

                if(lastStep.getPx() == currentLocation.getX() &&
                        lastStep.getPy() == currentLocation.getY() &&
                        lastStep.getPz() == currentLocation.getZ()) {

                    return;
                }
            }

            this.record(new Step(Step.Type.NORMAL, currentLocation, currentVelocity));
        }, 0, 5);

        this.player.setMetadata(METADATA_KEY, new FixedMetadataValue(Parkour.getParkour(), this));
        activeRecorders.add(this);
    }

    public void record(Step step) {
        step.setIndex(recordedSteps.size());
        recordedSteps.add(step);
    }

    public void stop() {
        this.isRecording = false;
        this.endTime = System.currentTimeMillis();

        this.task.cancel();

        //this.player.removeMetadata(METADATA_KEY, Parkour.getParkour());
        //activeRecorders.remove(this);
    }

}
