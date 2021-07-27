package com.customwrld.parkour.commands;

import com.customwrld.commonlib.util.MC;
import com.customwrld.commonlib.util.TimeUtil;
import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.util.Command;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class StopCommand extends Command {

    public StopCommand() {
        this.command = "pstop";
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

                    recorder.stop();
                    System.out.println("Started at: " + TimeUtil.millisToTimer(recorder.getStartTime()));
                    System.out.println("# of Recorded Steps: " + recorder.getRecordedSteps().size());
                    System.out.println("Ended at: " + TimeUtil.millisToTimer(recorder.getEndTime()));
                    sender.sendMessage(Component.text("Stopped recording!", MC.CC.GREEN.getTextColor()));
                });
    }

}