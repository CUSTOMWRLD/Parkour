package com.customwrld.parkour.commands;

import com.customwrld.commonlib.util.MC;
import com.customwrld.customlib.util.MetadataUtil;
import com.customwrld.parkour.Parkour;
import com.customwrld.parkour.replays.Recorder;
import com.customwrld.parkour.replays.Step;
import com.customwrld.parkour.util.Command;
import com.customwrld.parkour.util.DebugUtil;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class DisplayCommand extends Command {

    public DisplayCommand() {
        this.command = "pdisplay";
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand(this.command)
                .executesPlayer((Player sender, Object[] args) -> {
                    if(sender.hasMetadata("testing")) {
                        BukkitTask task = MetadataUtil.getValue(sender, "testing", Parkour.getParkour(), null, BukkitTask.class, false);
                        if(task != null) { task.cancel(); }

                        sender.removeMetadata("testing", Parkour.getParkour());
                        sender.sendMessage(Component.text("Stopped!", MC.CC.GREEN.getTextColor()));
                        return;
                    }

                    Recorder recorder = Recorder.fromPlayer(sender);
                    if(recorder == null) {
                        sender.sendMessage(Component.text("You don't have a recorder!", MC.CC.RED.getTextColor()));
                        return;
                    }

                    List<Pair<Location, Color>> points = new ArrayList<>();
                    for(Step step : recorder.getRecordedSteps()) {
                        var location = new Location(sender.getWorld(), step.getPx(), step.getPy(), step.getPz());
                        points.add(Pair.of(location, switch(step.getType()) {
                            case NORMAL -> Color.RED;

                            case JUMP -> Color.FUCHSIA;
                            //LAND,

                            case SPRINT_START -> Color.AQUA;
                            case SPRINT_STOP -> Color.BLUE;

                            case SNEAK_START -> Color.GRAY;
                            case SNEAK_STOP -> Color.WHITE;

                            case SWING -> Color.YELLOW;

                            case SWIM_START -> Color.TEAL;
                            case SWIM_STOP -> Color.NAVY;
                        }));

                        points.add(Pair.of(new Location(sender.getWorld(), step.getVx(), step.getVy(), step.getVz()).multiply(2.5).add(location),
                                null));
                    }

                    sender.setMetadata("testing", new FixedMetadataValue(Parkour.getParkour(), DebugUtil.drawBezierSplineQuadraticEnds(points)));

                    sender.sendMessage(Component.text("Started displaying recorded steps.", MC.CC.GREEN.getTextColor()));
                });
    }

}