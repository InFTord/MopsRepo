package ml.mops.pvps;

import ml.mops.base.kits.Kit;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitGUI implements Listener {
    private final Inventory inv;
    Player player;

    List<Kit> availableKits = new ArrayList<>(Arrays.asList(Kit.values()));

    public KitGUI(Player player) {
        inv = Bukkit.createInventory(null, 36, "Select Your Class");

        this.player = player;

        setItems();
    }

    public void setItems() {

        int i = 0;

        while(i < availableKits.size()) {
            Kit kit = availableKits.get(i);

            ItemStack kitItem = new ItemStack(kit.getShulker());
            MopsUtils.renameItem(kitItem, kit.getName());

            BlockStateMeta bsm = (BlockStateMeta) kitItem.getItemMeta();
            ShulkerBox box = (ShulkerBox) bsm.getBlockState();
            box.getInventory().setContents(kit.getInventory().getContents());
            bsm.setBlockState(box);
            box.update();
            kitItem.setItemMeta(bsm);

            inv.setItem(i, kitItem);
            i++;
        }
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
