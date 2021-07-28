package com.customwrld.parkour.listeners;

import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.replays.Step;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class RecorderListener implements Listener {

    @EventHandler
    public void onJumped(PlayerJumpEvent event) {
        var player = event.getPlayer();

        Recorder recorder = Recorder.fromPlayer(player);
        if(recorder == null || !recorder.isRecording()) { return; }

        //TODO Check if the velocity is correct
        recorder.record(new Step(Step.Type.JUMP, event.getFrom(), player.getVelocity()));
    }

    @EventHandler
    public void onSprintToggled(PlayerToggleSprintEvent event) {
        var player = event.getPlayer();

        Recorder recorder = Recorder.fromPlayer(player);
        if(recorder == null || !recorder.isRecording()) { return; }

        //TODO Check if the velocity is correct
        recorder.record(new Step(event.isSprinting() ? Step.Type.SPRINT_START : Step.Type.SPRINT_STOP, player.getLocation(), player.getVelocity()));
    }

    @EventHandler
    public void onSneakToggled(PlayerToggleSneakEvent event) {
        var player = event.getPlayer();

        Recorder recorder = Recorder.fromPlayer(player);
        if(recorder == null || !recorder.isRecording()) { return; }

        //TODO Check if the velocity is correct
        recorder.record(new Step(event.isSneaking() ? Step.Type.SNEAK_START : Step.Type.SNEAK_STOP, player.getLocation(), player.getVelocity()));
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent event) {
        var player = event.getPlayer();

        Recorder recorder = Recorder.fromPlayer(player);
        if(recorder == null || !recorder.isRecording()) { return; }

        //TODO Check if the velocity is correct
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            recorder.record(new Step(Step.Type.SWING, player.getLocation(), player.getVelocity()));
        }
    }

    @EventHandler
    public void onSwim(EntityToggleSwimEvent event) {
        if (event.getEntity() instanceof Player player) {

            Recorder recorder = Recorder.fromPlayer(player);
            if (recorder == null || !recorder.isRecording()) {
                return;
            }

            //TODO Check if the velocity is correct
            recorder.record(new Step(event.isSwimming() ? Step.Type.SWIM_START : Step.Type.SWIM_STOP, player.getLocation(), player.getVelocity()));
        }
    }

}
