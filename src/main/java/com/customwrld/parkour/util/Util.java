package com.customwrld.parkour.util;

import com.customwrld.commonlib.util.MC;
import com.customwrld.customlib.util.ItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {

    public static ItemStack PARKOUR_SELECTOR = new ItemFactory(Material.COMPASS)
            .setName(Component.text()
                    .append(Component.text("Parkour Selector ")
                            .color(MC.CC.AQUA.getTextColor()))
                    .append(Component.text("(Right Click)")
                            .color(MC.CC.GRAY.getTextColor()))
                    .decoration(TextDecoration.ITALIC, false)
                    .build())
            .setLore(Component.text("Right Click to bring up the Parkour Menu!")
                    .color(MC.CC.GRAY.getTextColor())
                    .decoration(TextDecoration.ITALIC, false))
            .build();

    public static void setItems(Player player) {
        Inventory inventory = player.getInventory();
        inventory.setItem(0, Util.PARKOUR_SELECTOR);
        player.updateInventory();
    }

    public static String abbreviateNumber(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }

}
