package com.customwrld.parkour.commands;

import com.customwrld.commonlib.util.MC;
import com.customwrld.commonlib.util.TimeUtil;
import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.util.Command;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class DeleteCommand extends Command {

    public DeleteCommand() {
        this.command = "pdelete";
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand(this.command)
                .executesPlayer((Player sender, Object[] args) -> {
                    Recorder recorder = Recorder.fromPlayer(sender);
                    if(recorder == null) {
                        sender.sendMessage(Component.text("You're not recording!", MC.CC.RED.getTextColor()));
                        return;
                    }

                    if (recorder.isRecording()) {
                        recorder.stop();
                    }
                    recorder.destroy();
                    sender.sendMessage(Component.text("Deleted recording!", MC.CC.GREEN.getTextColor()));
                });
    }

}
