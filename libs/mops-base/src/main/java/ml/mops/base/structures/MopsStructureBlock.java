package ml.mops.base.structures;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class MopsStructureBlock {
    public boolean enchanted = false;
    public Material block = Material.AIR;
    public EulerAngle angle = new EulerAngle(0, 0, 0);

    public ItemStack getBlock() {
        ItemStack item = new ItemStack(block);
        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

        item.setItemMeta(meta);

        return item;
    }
}
