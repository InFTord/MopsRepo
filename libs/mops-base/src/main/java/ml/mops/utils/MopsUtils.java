package ml.mops.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
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
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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


	static public EntityPlayer createNPC(Location location, String name, String skin) {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		EntityPlayer npcPlayer = new EntityPlayer(server, world, gameProfile, null);

		npcPlayer.b(location.getX(), location.getY(), location.getZ(), 180, 0);

		String[] skinKey = getSkin(skin);
		gameProfile.getProperties().put("textures", new Property("textures", skinKey[0], skinKey[1]));

		return npcPlayer;
	}



	static public String[] getSkin(String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();

			return new String[] {texture, signature};
		} catch (Exception e) {
			return new String[] {"ewogICJ0aW1lc3RhbXAiIDogMTY0MzE3MDI3Mjg4NywKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5MWUyZDMwNzFmNmYxNGQ5MTY3OGU4YTRjZWE2ZGIyMzUxMDI4MTVjNmZmM2QxOWIwYmI5ZTE2ZjlhYjUyZCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "GAzwBm8gcNYhxIadPeraOb+5r8InZ64T5NjUdv8WCHyXTmYjYkY8MycQTAWwg2TVUL+sMYvYcb2nYK6AMYRRwwSjpSS/w6xPZn8XxdlnO3qeouBH86A/pstHuRHtEtVnbpBfibsYmhsiVgz7P6SD2dMY42DN34SRc7+R/zEYcd7CyZoOtRx8Fc4kMmI/G+w4QxawJBwsStP/Eig1JLaYW8Ux4muLwRp9KkPepQV75HE8jRp7Y9D3+qOGBdC6yjprB2Mhm2/cCgtvVfrPPu1d7NGf15+tcdkLHoY9h6GHg55PBIaP5QwDJC8aAcKDYc5FvbKVD+x/FQms5Z7S29JZIaAKZjdyscYKUUoQwCNjNlVMZZPJpFaYKp83SeEsqbsIZwl6JMb7qlubuWiDzbEyAeDt3aAxrH5pMueyo1bGV/UIIXsUL4N5isB5VLgQ5t7/Mypuy8vJvr+Q/BtB/YW+nLH1UIKgwFQv+AX3CgbdIgCAsXDFhLNL9aXAKRvN3nUk9JbWStqfaS6gj8Noxf7ndoV/oBC0NXdTJTBaAt1UQGT3Lh6JKjzckM2blxb9XOlQJx3Gn0naPo5Q9hLVBY6H+DT8RRv/dvHcAc0sIKXu9/7rhGNJUSFxEBdzd7viLQHQdS+3P+t3qP6u2ZufxokVZA6g+C5dWwm1n8D0xRWZbi8="};
		}
	}

	static public ArmorStand createNewNameStand(String string, Location location) {
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		stand.setVisible(false);
		stand.setCustomName(string);
		stand.setMarker(true);
		stand.setCustomNameVisible(true);
		stand.addScoreboardTag("killOnDisable");
		return stand;
	}


}
