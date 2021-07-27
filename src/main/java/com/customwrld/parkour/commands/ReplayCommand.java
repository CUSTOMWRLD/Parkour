package com.customwrld.parkour.commands;

import com.customwrld.commonlib.util.MC;
import com.customwrld.customlib.util.MetadataUtil;
import com.customwrld.parkour.Parkour;
import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.replays.Replay;
import com.customwrld.parkour.util.Command;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ReplayCommand extends Command {

    public ReplayCommand() {
        this.command = "preplay";
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand(this.command)
                .executesPlayer((Player sender, Object[] args) -> {
                    if(sender.hasMetadata("woah")) {
                        Replay replay = MetadataUtil.getValue(sender, "woah", Parkour.getParkour(), null, Replay.class, false);
                        if(replay != null) { replay.stop(); }

                        sender.removeMetadata("woah", Parkour.getParkour());
                        sender.sendMessage(Component.text("Stopped!", MC.CC.GREEN.getTextColor()));
                        return;
                    }

                    Recorder recorder = Recorder.fromPlayer(sender);
                    if(recorder == null) {
                        sender.sendMessage(Component.text("You don't have a recorder!", MC.CC.RED.getTextColor()));
                        return;
                    }

                    Replay replay = new Replay(sender, recorder.getRecordedSteps());
                    sender.setMetadata("woah", new FixedMetadataValue(Parkour.getParkour(), replay));

                    sender.sendMessage(Component.text("Setting up the ghost.", MC.CC.GREEN.getTextColor()));
                    replay.start();

                    Bukkit.getScheduler().runTaskLater(Parkour.getParkour(), () -> {
                        sender.sendMessage(Component.text("Starting the ghost.", MC.CC.GREEN.getTextColor()));
                        replay.play();
                    }, 100);
                });
    }

}