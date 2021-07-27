package com.customwrld.parkour;

import com.customwrld.customlib.util.ClassUtils;
import com.customwrld.parkour.listeners.CancelledListener;
import com.customwrld.parkour.listeners.ParkourListener;
import com.customwrld.parkour.listeners.RecorderListener;
import com.customwrld.parkour.util.Command;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Getter @Setter
public class Parkour extends JavaPlugin {

    @Getter private static Parkour parkour;
//    @Getter private final Map<UUID, FastBoard> boards = new HashMap<>();
    private Location spawn;
    private List<Command> registeredCommands;

    @Override
    public void onEnable() {
        parkour = this;

        Logger logger = Bukkit.getLogger();

        World world = Bukkit.getWorld("world");

        if(world != null) {
            this.spawn = new Location(world, 0.5, 64, 0.5, 180, 0);
        } else {
            logger.severe("Bukkit was unable load the default world.");
            Bukkit.shutdown();
        }

        Arrays.asList(
                new CancelledListener(),
                new RecorderListener(),
                new ParkourListener()).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        for (Class<?> aClass : ClassUtils.getClassesInPackage(Parkour.class, "com.customwrld.parkour.commands")) {
            registerCommand(this, aClass);
        }

//        getServer().getScheduler().runTaskTimer(this, () -> {
//            for (FastBoard board : this.boards.values()) {
//                updateBoard(board);
//            }
//        }, 0, 20);
    }

    @Override
    public void onDisable() {}

//    private void updateBoard(FastBoard board) {
//        board.updateLines(
//                MC.Style.translateLegacy("&7&m                                "),
//                MC.Style.translateLegacy("&fJumps: &b" + Util.abbreviateNumber(board.getPlayer().getStatistic(Statistic.JUMP))),
//                MC.Style.translateLegacy("&fMaps: &a0&7/&a0"),
//                MC.Style.translateLegacy("&fPlaytime: &3" + TimeUtil.millisToRoundedTime(board.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) * 60L * 1000L)),
//                MC.Style.translateLegacy("&7&m                                ")
//        );
//    }

    private static boolean registerCommand(Parkour parkour, Class<?> commandClass) {
        try {
            if (!Command.class.isAssignableFrom(commandClass)) {
                return false;
            }
            Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
            command.command().register();
            parkour.getRegisteredCommands().add(command);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

}
