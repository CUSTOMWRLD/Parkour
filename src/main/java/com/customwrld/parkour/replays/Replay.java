package com.customwrld.parkour.replays;

import com.customwrld.commonlib.util.MC;
import com.customwrld.parkour.Parkour;
import com.customwrld.parkour.util.VectorUtil;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class Replay {

    private Player viewer;
    private Ghost ghost;

    private List<Step> steps;

    private int currentStepIndex = 0;
    private Step currentStep;

    private int nextStepIndex = 1;
    private Step nextStep;

    private long originTime;
    private long length;

    private long currentTime = 0;

    private boolean isPlaying;

    @Setter private boolean isForwards = true;
    @Setter private float speed = 1f;

    private BukkitTask tickTask;
    private long lastTickAt;

    public Replay(Player viewer, List<Step> steps) {
        this.viewer = viewer;
        this.steps = steps;
    }

    public void start() {
        this.currentStep = this.steps.get(0);
        this.nextStep = this.steps.get(1);

        this.originTime = this.currentStep.getTimestamp();
        this.length = this.steps.get(this.steps.size() - 1).getTimestamp() - this.currentStep.getTimestamp();

        this.ghost = new Ghost(this.viewer, this.viewer, new Location(this.viewer.getWorld(), this.currentStep.getPx(), this.currentStep.getPy(), this.currentStep.getPz()));
    }

    public void moveBy(long millis) {
        this.currentTime += millis;
    }

    /**
     * Moves to the specific point in time.
     * @throws IllegalArgumentException If the time specified is less than 0 or larger than the replay's length
     */
    public void moveTo(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Time cannot be less than 0.");
        } else if(millis > this.length) {
            throw new IllegalArgumentException("Time cannot be larger than replay's length.");
        }

        this.currentTime = millis;
    }

    public void play() {
        this.isPlaying = true;

        this.lastTickAt = System.currentTimeMillis();
        this.tickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getParkour(), this::tick, 0, 1);

        this.ghost.setSleeping(true);
    }

    public void pause() {
        this.isPlaying = false;
        this.tickTask.cancel();
    }

    public void stop() {
        pause();
        this.ghost.getNpc().delete(viewer);
    }

    private void tick() {
        if(!this.isPlaying) { return; }
        long time = System.currentTimeMillis();
        long delta = time - this.lastTickAt;

        this.currentTime += delta * speed;

        //Get the next step (in a loop because you can skip through one or more steps, especially at high speeds)
        boolean newStep = false;
        for(int i = this.currentStepIndex + 1; i < this.steps.size(); i++) {
            var nextStep = this.steps.get(i);
            if(nextStep.getTimestamp() - originTime <= this.currentTime) {
                this.currentStepIndex = i;
                newStep = true;
            } else {
                break;
            }
        }

        if(newStep) {
            this.currentStep = this.steps.get(this.currentStepIndex);

            this.nextStepIndex = this.currentStepIndex + 1;
            if(this.nextStepIndex >= this.steps.size()) {
                this.viewer.sendMessage(Component.text("Finished!", MC.CC.GREEN.getTextColor()));
                stop();
                return;
            }

            this.nextStep = this.steps.get(this.nextStepIndex);
        }

        double t = (this.currentTime - (this.currentStep.getTimestamp() - this.originTime)) /
                (double) (this.nextStep.getTimestamp() - this.currentStep.getTimestamp());

        var v1 = new Vector(this.currentStep.getPx(), this.currentStep.getPy(), this.currentStep.getPz());
        var cp = new Vector(this.currentStep.getVx(), this.currentStep.getVy(), this.currentStep.getVz()).multiply(2.5).add(v1);
        var v2 = new Vector(this.nextStep.getPx(), this.nextStep.getPy(), this.nextStep.getPz());

        var location = VectorUtil.getQuadraticBezierAt(v1, cp, v2, t).toLocation(viewer.getWorld());
        location.setYaw((float) (this.currentStep.getYaw() + ((this.nextStep.getYaw() - this.currentStep.getYaw()) * t)));
        location.setPitch((float) (this.currentStep.getPitch() + ((this.nextStep.getPitch() - this.currentStep.getPitch()) * t)));

        this.ghost.moveTo(location);

        if (this.currentStep.getType() == Step.Type.SPRINT_START) {
            this.ghost.setSprinting(true);
        } else if (this.currentStep.getType() == Step.Type.SPRINT_STOP) {
            this.ghost.setSprinting(false);
        }

        if (this.currentStep.getType() == Step.Type.SNEAK_START) {
            this.ghost.setSneaking(true);
        } else if (this.currentStep.getType() == Step.Type.SNEAK_STOP) {
            this.ghost.setSneaking(false);
        }

        if (this.currentStep.getType() == Step.Type.SWING) {
            this.ghost.swing();
        }

        if (this.currentStep.getType() == Step.Type.SWIM_START) {
            this.ghost.setSwimming(true);
        } else if (this.currentStep.getType() == Step.Type.SWIM_STOP) {
            this.ghost.setSwimming(false);
        }

        this.lastTickAt = time;
    }

}
