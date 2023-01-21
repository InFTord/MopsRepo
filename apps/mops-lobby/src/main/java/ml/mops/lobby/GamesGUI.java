package ml.mops.lobby;

import ml.mops.base.maps.Map;
import ml.mops.base.maps.MapManager;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class GamesGUI implements Listener {
    private final Inventory inv;

    public GamesGUI() {
        inv = Bukkit.createInventory(null, 18, "Select Your Destination");

        setItems();
    }

    public void setItems() {
        inv.setItem(0, MopsUtils.createItem(Material.GOLD_BLOCK, ChatColor.GOLD + "Spawn"));
        inv.setItem(1, MopsUtils.createItem(Material.CALCITE, ChatColor.WHITE + "MopsPVP"));
        inv.setItem(2, MopsUtils.createItem(Material.RED_WOOL, ChatColor.RED + "WoolBattle"));
        inv.setItem(3, MopsUtils.createItem(Material.LIGHT_BLUE_SHULKER_BOX, ChatColor.AQUA + "Kits"));
        inv.setItem(4, MopsUtils.createItem(Material.MELON, ChatColor.GREEN + "Market"));
        inv.setItem(5, MopsUtils.createItem(Material.GREEN_TERRACOTTA, ChatColor.DARK_GREEN + "Missions"));
        inv.setItem(6, MopsUtils.createItem(Material.LAPIS_BLOCK, ChatColor.BLUE + "MopsLobby Section 2"));
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
