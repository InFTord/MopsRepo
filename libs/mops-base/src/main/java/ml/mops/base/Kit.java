package ml.mops.base;

import ml.mops.utils.MopsColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public enum Kit {
    MAGE("Mage", MopsColor.PURPLE, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    BOMBER("Bomber", MopsColor.BLACK, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    PHOENIX("Phoenix", MopsColor.GOLD, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    DIVER("Diver", MopsColor.AQUA, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    ASSASSIN("Assassin", MopsColor.LIGHT_GRAY, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    WITHERLORD("Witherlord", MopsColor.RED, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    ILLUSIONER("Illusioner", MopsColor.BLUE, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    NECROMANCER("Necromancer", MopsColor.MAGENTA, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    PIG("Pig", MopsColor.LIME, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    HEALER("Healer", MopsColor.WHITE, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    STRAY("Stray", MopsColor.CYAN, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    PUG( "Pug", MopsColor.BROWN, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    ARCHER("Archer", MopsColor.GREEN, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    WEREWOLF("Werewolf", MopsColor.DARK_GRAY, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    PALADIN("Paladin", MopsColor.YELLOW, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    SAKURA("Sakura", MopsColor.PINK, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),

    PYRO("Pyro", MopsColor.RED, KitType.ADMIN_TEST_ONLY, KitManager.createKit(new String[] {"x 0 0 0 0 0 0 0 0", "y 0 0 0 a b c d 0", "z 0 0 0 0 0 0 0 0"}, KitManager.pyroMapping()));


    final String name;
    final Material shulker;
    final KitType type;
    final Inventory inventory;

    Kit (String name, MopsColor mopsColor, KitType type, Inventory inventory) {
        this.name = mopsColor.getColor() + name;
        this.shulker = mopsColor.getShulker();
        this.type = type;

        Inventory inv = Bukkit.createInventory(null, 27, this.name);
        inv.setContents(inventory.getContents());
        this.inventory = inv;
    }

    public String getName() {
        return name;
    }
    public Material getShulker() {
        return shulker;
    }
    public KitType getType() {
        return type;
    }
    public Inventory getInventory() {
        return inventory;
    }
}
