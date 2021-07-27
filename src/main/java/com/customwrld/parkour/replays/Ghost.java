package com.customwrld.parkour.replays;

import com.customwrld.customlib.CustomLib;
import com.customwrld.customlib.hologram.Hologram;
import com.customwrld.customlib.npc.NPC;
import com.customwrld.customlib.util.LocationUtil;
import com.customwrld.parkour.Parkour;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;

@Getter
public class Ghost {

    private OfflinePlayer player;
    private NPC npc;

    private boolean isSwimming;
    private boolean isSprinting;

    public Ghost(OfflinePlayer of, Player viewer, Location location) {
        this.player = of;
        Bukkit.getScheduler().runTaskAsynchronously(Parkour.getParkour(), () -> {
            try {
            //https://api.ashcon.app/mojang/v2/user/Notch
                URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + of.getUniqueId());
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonObject textureProperty = CustomLib.getCustomLib()
                        .getGson().fromJson(reader, JsonObject.class).getAsJsonObject("textures").getAsJsonObject("raw");

                String texture = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();

                this.npc = new NPC(Collections.emptyMap(), location, texture, signature, new Hologram(LocationUtil.zero(location.getWorld()), false));
                spawn(viewer);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void spawn(Player viewer) {
        this.npc.spawn(viewer);
    }
}
