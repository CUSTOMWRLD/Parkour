package com.customwrld.parkour.commands;

import com.customwrld.commonlib.util.MC;
import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.util.Command;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class StartCommand extends Command {

    public StartCommand() {
        this.command = "pstart";
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand(this.command)
                .executesPlayer((Player sender, Object[] args) -> {
                    Recorder existingRecorder = Recorder.fromPlayer(sender);
                    if(existingRecorder != null) {
                        sender.sendMessage(Component.text("You're already recording!", MC.CC.RED.getTextColor()));
                        return;
                    }

                    Recorder recorder = new Recorder(sender);
                    recorder.start();

                    sender.sendMessage(Component.text("Started recording!", MC.CC.GREEN.getTextColor()));
                });
    }

}