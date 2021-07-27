package com.customwrld.parkour.listeners;

import com.customwrld.parkour.Parkour;
import com.customwrld.parkour.util.Util;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParkourListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

//        FastBoard board = new FastBoard(player);

//        board.updateTitle(MC.Style.translateLegacy("&b&lCUSTOMWRLD &8| &7Parkour"));

//        Parkour.getParkour().getBoards().put(player.getUniqueId(), board);

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Parkour.getParkour().getSpawn());

        // TODO: Use gamemode storage and keep hasToggledVisibility as a constant boolean.
//        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
//            if(onlinePlayer != player && Util.toggledVisibility.contains(onlinePlayer.getUniqueId())) {
//                ServerUtil.hidePlayer(player, onlinePlayer);
//            }
//        });

        player.getInventory().clear();
        player.getActivePotionEffects().clear();

        Util.setItems(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

//        FastBoard board = Parkour.getParkour().getBoards().remove(player.getUniqueId());
//
//        if (board != null) {
//            board.delete();
//        }

        // TODO: Save their current time, course, and location.
    }

}
