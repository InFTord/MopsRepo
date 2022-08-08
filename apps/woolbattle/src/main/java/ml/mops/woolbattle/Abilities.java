package ml.mops.woolbattle;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Abilities {
	final Plugin plugin;

	public Abilities(Plugin pl) {
	this.plugin = pl;
	}

	public void startGame(String lang, Teams team, Player player1) {
		ItemStack item1 = new ItemStack(Material.SHEARS);
		ItemMeta meta1 = item1.getItemMeta();
		meta1.displayName(plugin.getByLang(lang, "shears.name"));
		meta1.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta1.addEnchant(Enchantment.KNOCKBACK, 2, true);
		meta1.setUnbreakable(true);

		//List<Component> lore1 = new ArrayList<>();
		//lore1.add(plugin.getByLang(lang, "shears.lore"));
		//meta1.lore(lore1);

		List<String> lore1 = new ArrayList<>();
		lore1.add(ChatColor.GRAY + "Ножницы для копания шерсти и");
		lore1.add(ChatColor.GRAY + "скидывания игроков в бездну.");
		lore1.add("");
		lore1.add(ChatColor.AQUA + "Способность: " + ChatColor.GRAY + "Убирает блоки");
		lore1.add(ChatColor.GRAY + "своего цвета на генераторах." + ChatColor.YELLOW + "" + ChatColor.BOLD + " ПКМ");
		lore1.add(ChatColor.AQUA + "Стоимость: 6 шерсти");
		meta1.setLore(lore1);

		item1.setItemMeta(meta1);
		player1.getInventory().setItem(0, item1);

		plugin.shears.add(item1);

		ItemStack item2 = new ItemStack(Material.STICK);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.displayName(plugin.getByLang(lang, "explosionStaff.name"));
		List<String> lore2 = new ArrayList<>();
		lore2.add(ChatColor.GRAY + "Палка откидывающая тебя назад." + ChatColor.YELLOW + "" + ChatColor.BOLD + " ПКМ" + ChatColor.DARK_GRAY + " (Нужно наводится на блок)");
		lore2.add(ChatColor.AQUA + "Стоимость: 28 шерсти");
		meta2.setLore(lore2);
		item2.setItemMeta(meta2);
		player1.getInventory().setItem(1, item2);

		plugin.explosiveSticks.add(item2);

		ItemStack item3 = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta3 = item3.getItemMeta();
		meta3.setDisplayName(ChatColor.GREEN + "Надувной Батут");
		List<String> lore3 = new ArrayList<>();
		lore3.add(ChatColor.GRAY + "Спасёт тебя при падении." + ChatColor.YELLOW + "" + ChatColor.BOLD + " ПКМ" + ChatColor.DARK_GRAY + " (Шифт чтобы прыгнуть высоко)");
		lore3.add(ChatColor.AQUA + "Стоимость: 208 шерсти");
		meta3.addEnchant(Enchantment.DURABILITY, 1, true);
		meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta3.setLore(lore3);
		item3.setItemMeta(meta3);
		player1.getInventory().setItem(2, item3);

		plugin.slimeballs.add(item3);

		ItemStack item4 = new ItemStack(Material.BOW);
		ItemMeta meta4 = item4.getItemMeta();
		meta4.setDisplayName(ChatColor.AQUA + "Лук");
		List<String> lore4 = new ArrayList<>();
		lore4.add(ChatColor.GRAY + "Лук, может ломать шерсть при попадании.");
		meta4.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
		meta4.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta4.setUnbreakable(true);
		meta4.setLore(lore4);
		item4.setItemMeta(meta4);
		player1.getInventory().setItem(8, item4);

		ItemStack item5 = new ItemStack(Material.ARROW);
		ItemMeta meta5 = item5.getItemMeta();
		meta5.setDisplayName(ChatColor.GRAY + "Стрела");
		item5.setItemMeta(meta5);
		player1.getInventory().setItem(17, item5);

		String colorString = team.getColorString;
		Color color = team.getLeatherColor;


		ItemStack bootsItem = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bootsMeta = (LeatherArmorMeta) bootsItem.getItemMeta();

		bootsMeta.displayName(plugin.getByLang(lang, "doubleJumpBoots.name", Map.of("TCC", colorString)));
		List<String> bootsLore = new ArrayList<>();
		bootsLore.add(ChatColor.GRAY + "Позволяют прыгать в воздухе. ");
		bootsLore.add(ChatColor.DARK_GRAY + "(Нужно нажать пробел дважды в падении)");
		bootsLore.add(ChatColor.AQUA + "Стоимость: 16 шерсти");
		bootsMeta.setUnbreakable(true);
		bootsMeta.setColor(color);
		bootsMeta.setLore(bootsLore);
		bootsItem.setItemMeta(bootsMeta);
		player1.getInventory().setItem(36, bootsItem);

		plugin.doubleJumpBoots.add(bootsItem);

	}
}
