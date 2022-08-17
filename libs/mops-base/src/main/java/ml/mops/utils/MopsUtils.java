package ml.mops.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import org.apache.commons.lang3.StringUtils;
import ml.mops.base.MopsPlugin;
import ml.mops.exception.UnsoportedYetFeature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class MopsUtils {
	MopsPlugin plugin;

	public MopsUtils(MopsPlugin plugin) {
		this.plugin = plugin;
	}

	public Duration ticks(int ticks) {
		return Duration.ofMillis(ticks * 50L);
	}

	public TextComponent combineComponents(TextComponent[] tcs, TextComponent separator) {
		TextComponent fc = Component.empty();
		for (TextComponent tc : tcs) {
			fc.append(tc).append(separator);
		}
		return fc;
	}
	

	public Title createTitle(@NotNull String lang, @Nullable String id, @Nullable String id2nd, int i, int j, int k) {
		TextComponent c1;

		if (id2nd != null && id2nd.isBlank()) {
			c1 = Component.empty();
		} else {
			try {
				c1 = plugin.getByLang(lang, id);

			} catch (Exception e) {
				c1 = Component.empty();
			}
		}

		TextComponent c2;
		if (id2nd != null && id2nd.isBlank()) {
			c2 = Component.empty();
		} else {
			try {
				c2 = plugin.getByLang(lang, id2nd);

			} catch (Exception e) {
				c2 = Component.empty();
			}
		}

		final Title.Times times = Title.Times.times(ticks(i), ticks(k), ticks(j));

		return Title.title(c1, c2, times);
	}

	static public String componentsToSingularString(List<Component> textComponents) {
		List<String> strings = null;
		for (Component component : textComponents) {
			strings.add(legacyAmpersand().serialize(component));
		}
		return strings.toString();
	}
	static public String textComponentsToSingularString(List<TextComponent> textComponents) {
		List<String> strings = null;
		for (TextComponent textComponent : textComponents) {
			strings.add(legacyAmpersand().serialize(textComponent));
		}
		return strings.toString();
	}

	static public List<String> componentsToStrings(List<Component> textComponents) {
		List<String> strings = null;
		for (Component component : textComponents) {
			strings.add(legacyAmpersand().serialize(component));
		}
		return strings;
	}
	static public List<String> textComponentsToStrings(List<TextComponent> textComponents) {
		List<String> strings = null;
		for (TextComponent textComponent : textComponents) {
			strings.add(legacyAmpersand().serialize(textComponent));
		}
		return strings;
	}

//	static public List<String> playerNames() {
//		List<String> playerNames = null;
//		for (Player player : Dependencies.getPlugin().getServer().getOnlinePlayers()) {
//			playerNames.add(player.getName());
//		}
//		return  playerNames;
//	}

	static public String combineStrings(String[] strings) {
		StringJoiner joiner = new StringJoiner("");
		for (String string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}
	static public String combineStrings(CharSequence[] strings) {
		StringJoiner joiner = new StringJoiner("");
		for (CharSequence string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}

	static public String combineStrings(String[] strings, String character) {
		StringJoiner joiner = new StringJoiner(character);
		for (String string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}
	static public String combineStrings(CharSequence[] strings, String character) {
		StringJoiner joiner = new StringJoiner(character);
		for (CharSequence string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}

	static public String combineStrings(String[] strings, String character, Integer[] excludes) {
		StringJoiner joiner = new StringJoiner(character);
		for (int i = 0; i<strings.length; i++) {
			final int j = i;
			if (Arrays.stream(excludes).noneMatch(x -> x == j)) {
				joiner.add(strings[j]);
			}
		}
		return joiner.toString();

	}

	static public void updateDisplayName(Player target) {
//		String rank = Dependencies.getMopsRank(target);
//		String name = Dependencies.getMopsName(target);
//
//		if (rank == null || rank.isBlank() || rank.isEmpty()) {
//			rank = ChatColor.GRAY + "";
//		}
//		if (name == null || name.isBlank() || name.isEmpty()) {
//			name = target.getName();
//		}
//
//		target.displayName(Component.empty().append(legacyAmpersand().deserialize(rank)).append(legacyAmpersand().deserialize(name)));
		target.sendMessage("временно не работает");
	}

	static public void addScore(Objective objective, Integer number, TextComponent displayContent) {
		objective.getScore(legacyAmpersand().serialize(displayContent)).setScore(number);
	}

	static public String convertColorCodes(String string) {
		string = string.replaceAll("&0", ChatColor.BLACK + "");
		string = string.replaceAll("&1", ChatColor.DARK_BLUE + "");
		string = string.replaceAll("&2", ChatColor.DARK_GREEN + "");
		string = string.replaceAll("&3", ChatColor.DARK_AQUA + "");
		string = string.replaceAll("&4", ChatColor.DARK_RED + "");
		string = string.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		string = string.replaceAll("&6", ChatColor.GOLD + "");
		string = string.replaceAll("&7", ChatColor.GRAY + "");
		string = string.replaceAll("&8", ChatColor.DARK_GRAY + "");
		string = string.replaceAll("&9", ChatColor.BLUE + "");
		string = string.replaceAll("&a", ChatColor.GREEN + "");
		string = string.replaceAll("&b", ChatColor.AQUA + "");
		string = string.replaceAll("&c", ChatColor.RED + "");
		string = string.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		string = string.replaceAll("&e", ChatColor.YELLOW + "");
		string = string.replaceAll("&f", ChatColor.WHITE + "");
		string = string.replaceAll("&k", ChatColor.MAGIC + "");
		string = string.replaceAll("&l", ChatColor.BOLD + "");
		string = string.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		string = string.replaceAll("&n", ChatColor.UNDERLINE + "");
		string = string.replaceAll("&o", ChatColor.ITALIC + "");
		string = string.replaceAll("&r", ChatColor.RESET + "");

		string = string.replaceAll("&s", " ");

		return string;
	}

	static public List<Material> leatherItems() {
		List<Material> list = new ArrayList<>();

		list.add(Material.LEATHER_HELMET);
		list.add(Material.LEATHER_CHESTPLATE);
		list.add(Material.LEATHER_LEGGINGS);
		list.add(Material.LEATHER_BOOTS);
		list.add(Material.LEATHER_HORSE_ARMOR);

		return list;
	}

	static public void fillInventory(Inventory inv, ItemStack item) {
		int i = 0;

		while (i < inv.getSize()) {
			ItemStack itemstack = inv.getItem(i);
			if(itemstack == null || itemstack.getType() == Material.AIR) {
				inv.setItem(i, item);
			}

			i++;
		}
	}

	static public ItemStack renameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	static public ItemStack addItemLore(ItemStack item, String lore1, String lore2, String lore3) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(lore1);
		lore.add(lore2);
		lore.add(lore3);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	static public ItemStack addItemLore(ItemStack item, String lore1, String lore2) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(lore1);
		lore.add(lore2);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}


	static public ItemStack addItemLore(ItemStack item, String lore1) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(lore1);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}


	static public EntityPlayer createNPC(Location location, String name) {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		EntityPlayer npcPlayer = new EntityPlayer(server, world, gameProfile, null);

		npcPlayer.b(location.getX(), location.getY(), location.getZ(), 180, 0);
		return npcPlayer;
	}
}
