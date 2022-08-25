package ml.mops.base;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum Kit {
    MAGE(ChatColor.DARK_PURPLE + "Mage"),
    BOMBER(ChatColor.BLACK + "Bomber"),
    PHOENIX(ChatColor.GOLD + "Phoenix"),
    DIVER(ChatColor.AQUA + "Diver"),
    ASSASSIN(ChatColor.GRAY + "Assassin"),
    WITHERLORD(ChatColor.RED + "Witherlord"),
    ILLUSIONER(ChatColor.DARK_BLUE + "Illusioner"),
    NECROMANCER(ChatColor.LIGHT_PURPLE + "Necromancer"),
    PIG(ChatColor.GREEN + "Pig"),
    HEALER(ChatColor.WHITE + "Healer"),
    STRAY(ChatColor.DARK_AQUA + "Stray"),
    GOLEM(net.md_5.bungee.api.ChatColor.of("#9e6841") + "Dirt Golem"),
    ARCHER(ChatColor.DARK_GREEN + "Archer"),
    WEREWOLF(ChatColor.DARK_GRAY + "Werewolf"),
    PALADIN(ChatColor.YELLOW + "Paladin"),
    BLOODY(net.md_5.bungee.api.ChatColor.of("#ffadc6") + "Bloody");

    final String name;

    Kit (String name) {
        this.name = name;
    }
}
