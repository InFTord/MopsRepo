package ml.mops.base.kits;

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
    DRAGON("Dragon", MopsColor.RED, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    THUNDER("Thunder", MopsColor.BLUE, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    NECROMANCER("Necromancer", MopsColor.MAGENTA, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    SCIENTIST("Scientist", MopsColor.LIME, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    HEALER("Healer", MopsColor.WHITE, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    BEAR("Polar Bear", MopsColor.CYAN, KitType.BASE, KitManager.createKit(new String[] {"b b 0 0 0 0 0 b b", "b bo l 0 s 0 c h b", "b p 0 0 0 0 0 p b"}, KitManager.kitSpecificMapping("bear"))),
    PUG( "Pug", MopsColor.BROWN, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    ARCHER("Archer", MopsColor.GREEN, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    WOLF("Werewolf", MopsColor.DARK_GRAY, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    PALADIN("Paladin", MopsColor.YELLOW, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),
    SAKURA("Sakura", MopsColor.PINK, KitType.BASE, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping())),

    PLACEHOLDERSHITIDKJUSTIGNORETHIS("PLACEHOLDERSHITIDKJUSTIGNORETHIS", MopsColor.PLAIN, KitType.ADMIN_TEST_ONLY, KitManager.createKit(new String[] {"0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0"}, KitManager.baseMapping()));

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
