package ml.mops.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum MopsColor {
    PURPLE(ChatColor.DARK_PURPLE + "", Material.PURPLE_SHULKER_BOX),
    BLACK(ChatColor.BLACK + "", Material.BLACK_SHULKER_BOX),
    GOLD(ChatColor.GOLD + "", Material.ORANGE_SHULKER_BOX),
    AQUA(ChatColor.AQUA + "", Material.LIGHT_BLUE_SHULKER_BOX),
    LIGHT_GRAY(ChatColor.GRAY + "", Material.LIGHT_GRAY_SHULKER_BOX),
    RED(ChatColor.RED + "", Material.RED_SHULKER_BOX),
    BLUE(ChatColor.BLUE + "", Material.BLUE_SHULKER_BOX),
    MAGENTA(ChatColor.LIGHT_PURPLE + "", Material.MAGENTA_SHULKER_BOX),
    LIME(ChatColor.GREEN + "", Material.LIME_SHULKER_BOX),
    WHITE(ChatColor.WHITE + "", Material.WHITE_SHULKER_BOX),
    CYAN(ChatColor.DARK_AQUA + "", Material.CYAN_SHULKER_BOX),
    BROWN(net.md_5.bungee.api.ChatColor.of("#9e6841") + "", Material.BROWN_SHULKER_BOX),
    GREEN(ChatColor.DARK_GREEN + "", Material.GREEN_SHULKER_BOX),
    DARK_GRAY(ChatColor.DARK_GRAY + "", Material.GRAY_SHULKER_BOX),
    YELLOW(ChatColor.YELLOW + "", Material.YELLOW_SHULKER_BOX),
    PINK(net.md_5.bungee.api.ChatColor.of("#f79cb8") + "", Material.PINK_SHULKER_BOX),

    SOFT_LIME(net.md_5.bungee.api.ChatColor.of("#83f783") + "", Material.LIME_SHULKER_BOX),
    SOFT_GREEN(net.md_5.bungee.api.ChatColor.of("#50bf50") + "", Material.GREEN_SHULKER_BOX),
    SOFT_YELLOW(net.md_5.bungee.api.ChatColor.of("#f2ec6f") + "", Material.YELLOW_SHULKER_BOX),
    SOFT_MAGENTA(net.md_5.bungee.api.ChatColor.of("#f573f5") + "", Material.MAGENTA_SHULKER_BOX),
    SOFT_PURPLE(net.md_5.bungee.api.ChatColor.of("#c851cf") + "", Material.PURPLE_SHULKER_BOX),
    SOFT_DARKBLUE(net.md_5.bungee.api.ChatColor.of("#3434ad") + "", Material.BLUE_SHULKER_BOX),

    PLAIN(ChatColor.RESET + "", Material.SHULKER_BOX);

    final String color;
    final Material shulker;

    MopsColor (String color, Material shulker) {
        this.color = color;
        this.shulker = shulker;
    }

    public Material getShulker() {
        return shulker;
    }
    public String getColor() {
        return color;
    }
}
