package com.customwrld.parkour.replays;

import com.customwrld.customlib.CustomLib;
import com.customwrld.customlib.hologram.Hologram;
import com.customwrld.customlib.npc.NPC;
import com.customwrld.customlib.util.LocationUtil;
import com.customwrld.customlib.util.NMSUtil;
import com.customwrld.parkour.Parkour;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.*;
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
    private Player viewer;

    private boolean isSwimming;
    private boolean isSprinting;

    public Ghost(OfflinePlayer of, Player viewer, Location location) {
        this.player = of;
        this.viewer = viewer;
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
                //spawn(viewer);
                Bukkit.getOnlinePlayers().forEach(this::spawn);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void spawn(Player viewer) {
        this.npc.spawn(viewer);
    }

    public void moveTo(Location location) {
        double lastX = this.npc.entityNPC.lastX;
        double lastY = this.npc.entityNPC.lastY;
        double lastZ = this.npc.entityNPC.lastZ;

        float lastYaw = this.npc.entityNPC.getBukkitYaw();
        float lastPitch = this.npc.entityNPC.lastPitch;

        this.npc.entityNPC.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.npc.entityNPC.setHeadRotation(location.getYaw());
        //NMSUtil.sendPacket(viewer, new PacketPlayOutEntityTeleport(this.npc.entityNPC));
        NMSUtil.sendPacket(viewer,
                new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.npc.entityId,
                        (short) PacketPlayOutEntity.a(location.getX() - lastX),
                        (short) PacketPlayOutEntity.a(location.getY() - lastY),
                        (short) PacketPlayOutEntity.a(location.getZ() - lastZ),
                        (byte) (location.getYaw() * 256F / 360F),
                        (byte) (location.getPitch() * 256F / 360F),
                        this.npc.entityNPC.isOnGround())
        );
        NMSUtil.sendPacket(viewer, new PacketPlayOutEntityHeadRotation(this.npc.entityNPC, (byte) (location.getYaw() * 256F / 360F)));
    }

    public void setSprinting(boolean sprinting) {
        this.npc.entityNPC.setSprinting(sprinting);
    }

    public void swing() {
        NMSUtil.sendPacket(viewer, new PacketPlayOutAnimation(this.npc.entityNPC, 0));
    }

    public void setSleeping(boolean sleeping) {
        if (sleeping) {
            this.sendPose(viewer, EntityPose.SLEEPING);
        } else {
            this.sendPose(viewer, EntityPose.STANDING);
        }
    }

    public void setSneaking(boolean sneaking) {
//        if (sneaking) {
//            this.sendPose(viewer, EntityPose.CROUCHING);
//        } else {
//            this.sendPose(viewer, EntityPose.STANDING);
//        }
    }

    public void setSwimming(boolean swiming) {
//        if (swiming) {
//            this.sendPose(viewer, EntityPose.SWIMMING);
//        } else {
//            this.sendPose(viewer, EntityPose.STANDING);
//        }
    }

    private void sendPose(Player player, EntityPose pose) {
        DataWatcher watcher = this.npc.entityNPC.getDataWatcher();
        watcher.set(new DataWatcherObject<>(6, DataWatcherRegistry.s), pose);
        NMSUtil.sendPacket(player, new PacketPlayOutEntityMetadata(this.npc.entityId, watcher, true));
    }
}
