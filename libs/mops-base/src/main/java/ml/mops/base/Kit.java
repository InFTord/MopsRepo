package ml.mops.base;

import ml.mops.utils.MopsColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum Kit {
    MAGE("Mage", MopsColor.PURPLE),
    BOMBER("Bomber", MopsColor.BLACK),
    PHOENIX("Phoenix", MopsColor.GOLD),
    DIVER("Diver", MopsColor.AQUA),
    ASSASSIN("Assassin", MopsColor.LIGHT_GRAY),
    WITHERLORD("Witherlord", MopsColor.RED),
    ILLUSIONER("Illusioner", MopsColor.BLUE),
    NECROMANCER("Necromancer", MopsColor.MAGENTA),
    PIG("Pig", MopsColor.LIME),
    HEALER("Healer", MopsColor.WHITE),
    STRAY("Stray", MopsColor.CYAN),
    PUG( "Pug", MopsColor.BROWN),
    ARCHER("Archer", MopsColor.GREEN),
    WEREWOLF("Werewolf", MopsColor.DARK_GRAY),
    PALADIN("Paladin", MopsColor.YELLOW),
    SAKURA("Sakura", MopsColor.PINK),

    PLACEHOLDER("Placeholder Kit Name!", MopsColor.PLAIN);

    final String name;
    final Material shulker;

    Kit (String name, MopsColor mopsColor) {
        this.name = mopsColor.getColor() + name;
        this.shulker = mopsColor.getShulker();
    }
}
