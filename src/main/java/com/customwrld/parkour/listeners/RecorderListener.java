package com.customwrld.parkour.listeners;

import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.replays.Step;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

}
