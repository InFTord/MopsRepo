package ml.mops.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.ChatMessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import ml.mops.base.MopsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.Duration;
import java.util.*;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class MopsUtils <OBJ extends Object, COMPONENT_COLLECTION extends Collection<Component>, COLLECTION extends Collection<OBJ>> {
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

	String CombineComponentsToString(COMPONENT_COLLECTION components) {
		List<String> strings = null;
		for (Component component : components) {
			strings.add(legacyAmpersand().serialize(component));
		}
		return strings.toString();
	}

//	static public List<String> playerNames() {
//		List<String> playerNames = null;
//		for (Player player : Dependencies.getPlugin().getServer().getOnlinePlayers()) {
//			playerNames.add(player.getName());
//		}
//		return  playerNames;
//	}

	static public void sendTextComponentMessage(Player player, TextComponent component) {
		player.sendMessage(component);
	}

	static public void sendToServer(Plugin plugin, Player player, String server) {
		player.sendMessage(ChatColor.GRAY + "Sending you to " + server +"...");

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	static public void restartServer(Plugin plugin) {
		String path = plugin.getServer().getPluginsFolder().getAbsolutePath();
		path = path.replace("\\plugins", "\\start.bat");

		for(World world : plugin.getServer().getWorlds()) {
			world.save();
		}
		plugin.getServer().savePlayers();

		String finalPath = path;

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			plugin.getServer().shutdown();

			try {
				Runtime.getRuntime().exec("cmd /c start " + finalPath);
			} catch (Exception ignored) { }
		}, 20L);
	}

	static public Map<String, String> emoteMap() {
		Map<String, String> emotes = new HashMap<>();
		emotes.put("skull", ChatColor.GRAY + "☠");
		emotes.put("fire", ChatColor.GOLD + "🔥");
		emotes.put("bang", ChatColor.RED + "" + ChatColor.BOLD + "!!");
		emotes.put("bangbang", ChatColor.RED + "" + ChatColor.BOLD + "!!");
		emotes.put("exclamation", ChatColor.RED + "" + ChatColor.BOLD + "!!");
		emotes.put("warning", ChatColor.YELLOW + "⚠");
		emotes.put("sob", ChatColor.AQUA + "(T-T)");
		emotes.put("cry", ChatColor.AQUA + "(T-T)");
		emotes.put("plead", ChatColor.YELLOW + "(*-*)☞☜");
		emotes.put("pleading", ChatColor.YELLOW + "(*-*)☞☜");
		emotes.put("pleading_face", ChatColor.YELLOW + "(*-*)☞☜");
		emotes.put("bow", MopsColor.BROWN.getColor() + "🏹");

		return emotes;
	}

	static public String emojify(String input) {
		Map<String, String> emotes = emoteMap();

		for(String key : emotes.keySet()) {
			input = input.replaceAll(":" + key + ":", emotes.get(key) + ChatColor.RESET);
			input = input.replaceAll(":" + key.toUpperCase(Locale.ROOT) + ":", emotes.get(key) + ChatColor.RESET);
		}

		return input;
	}

	static public String getPath(Plugin plugin) {
		return plugin.getServer().getPluginsFolder().getAbsolutePath();
	}

	public static String readFile(String path) {
		StringBuilder string = new StringBuilder();
		try {
			File file = new File(path);
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				string.append(line).append("\n");
			}
			reader.close();

		} catch (IOException ignored) { }

		return string.toString();
	}

	public static void writeFile(String path, String text) throws IOException {
		FileWriter writer = new FileWriter(path, false);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);

		bufferedWriter.write(text);

		bufferedWriter.close();
	}

	static public String combineStrings(String[] strings) {
		StringJoiner joiner = new StringJoiner("");
		for (String string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}

	static public String combineStrings(List<String> strings) {
		StringJoiner joiner = new StringJoiner("");
		for (String string : strings) {
			joiner.add(string);
		}
		return joiner.toString();
	}

	static public String combineStrings(List<String> strings, String separator) {
		StringJoiner joiner = new StringJoiner("");
		for (String string : strings) {
			joiner.add(string).add(separator);
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
		for (int i = 0; i < strings.length; i++) {
			final int j = i;
			if (Arrays.stream(excludes).noneMatch(x -> x == j)) {
				joiner.add(strings[j]);
			}
		}
		return joiner.toString();

	}

	static public @NotNull String textComponentToString(TextComponent component) {
		return legacyAmpersand().serialize(component);
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

		string = string.replaceAll("&g", MopsColor.BROWN.color + "");
		string = string.replaceAll("&h", MopsColor.PINK.color + "");

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
			if (itemstack == null || itemstack.getType() == Material.AIR) {
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

	static public ItemStack addLore(ItemStack item, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);

		return item;
	}

	static public ItemStack amount(ItemStack item, int amount) {
		item.setAmount(amount);

		return item;
	}

	static public ItemStack colorItem(ItemStack item, String hex) {
		if(leatherItems().contains(item.getType())) {
			ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
			String rgbBase16 = hex.trim().replace("#", "");
			int rgbBase10 = Integer.parseInt(rgbBase16, 16);
			leatherArmorMeta.setColor(Color.fromRGB(rgbBase10));

			item.setItemMeta(leatherArmorMeta);
		}
		return item;
	}

	static public ItemStack unbreak(ItemStack item) {
		item.setUnbreakable(true);
		return item;
	}

	static public ItemStack unbreak(ItemStack item, boolean bool) {
		item.setUnbreakable(bool);
		return item;
	}

	static public ItemStack createItem(Material mat, String name) {
		ItemStack item = new ItemStack(mat, 1);
		renameItem(item, name);
		return item;
	}

	static public ItemStack createItem(Material mat, String name, int count) {
		ItemStack item = new ItemStack(mat, count);
		renameItem(item, name);
		return item;
	}

	static public ItemStack createItem(Material mat, String name, int count, boolean unbreakable) {
		ItemStack item = new ItemStack(mat, count);
		renameItem(item, name);
		item.setUnbreakable(unbreakable);
		return item;
	}

	static public ItemStack createItem(Material mat, String name, int count, String[] lore) {
		ItemStack item = new ItemStack(mat, count);
		renameItem(item, name);
		addLore(item, lore);
		return item;
	}

	static public ItemStack createItem(Material mat, String name, int count, String[] lore, boolean unbreakable) {
		ItemStack item = new ItemStack(mat, count);
		renameItem(item, name);
		addLore(item, lore);
		item.setUnbreakable(unbreakable);
		return item;
	}

	static public String upperSquares(int repeat) {
		int i = 0;
		StringBuilder string = new StringBuilder();
		while (i < repeat) {
			string.append("▄");
			i++;
		}

		return string.toString();
	}

	static public String bottomSquares(int repeat) {
		int i = 0;
		StringBuilder string = new StringBuilder();
		while (i < repeat) {
			string.append("▀");
			i++;
		}

		return string.toString();
	}

	public static ItemStack createCustomHead(String texture) {
		texture = "http://textures.minecraft.net/texture/" + texture;

		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);

		SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
		skullMeta.setDisplayName(ChatColor.RESET + "custom player head wow omg real");

		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = skullMeta.getClass().getDeclaredField("profile");
		}
		catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		assert profileField != null;
		profileField.setAccessible(true);
		try {
			profileField.set(skullMeta, profile);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		skull.setItemMeta(skullMeta);
		return skull;
	}

	public static String getHeadID(ItemStack skull) {
		SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
		return skullMeta.getAsString();
	}

	static public ItemStack createPotion(PotionEffectType type, int ticks, int level, Color color) {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = ((PotionMeta) item.getItemMeta());
		meta.setColor(color);
		meta.addCustomEffect(new PotionEffect(type, ticks, level), true);
		item.setItemMeta(meta);

		return item;
	}

	static public ItemStack createSplashPotion(PotionEffectType type, int ticks, int level, Color color) {
		ItemStack item = new ItemStack(Material.SPLASH_POTION);
		PotionMeta meta = ((PotionMeta) item.getItemMeta());
		meta.setColor(color);
		meta.addCustomEffect(new PotionEffect(type, ticks, level), true);
		item.setItemMeta(meta);

		return item;
	}

	static public ItemStack createLingeringPotion(PotionEffectType type, int ticks, int level, Color color) {
		ItemStack item = new ItemStack(Material.LINGERING_POTION);
		PotionMeta meta = ((PotionMeta) item.getItemMeta());
		meta.setColor(color);
		meta.addCustomEffect(new PotionEffect(type, ticks, level), true);
		item.setItemMeta(meta);

		return item;
	}

	static public void sendDialogueMessage(String dialogue, Player player, Entity entity) {
		player.sendMessage(ChatColor.YELLOW + "[NPC] " + entity.getCustomName() + ChatColor.WHITE + ": " + dialogue);
	}

	static public boolean isAutomodded(String string) {
		String noSpace = string.toLowerCase(Locale.ROOT).replaceAll(" ", "");
		List<String> strings = Arrays.asList(string.toLowerCase(Locale.ROOT).split(" "));
		String lowercase = string.toLowerCase(Locale.ROOT);

		if(strings.contains("kys") || strings.contains("kуs")) {
			return true;
		}
		if(noSpace.contains("killyoursel") || noSpace.contains("卐") || noSpace.contains("nigg") || noSpace.contains("хох") || noSpace.contains("canibeadmin")) {
			return true;
		}
		if(lowercase.contains("k y s")) {
			return true;
		}
		return false;
	}

	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
		Set<T> keys = new HashSet<T>();
		for (Map.Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	static public String sendRandomDialogueMessage(List<String> dialogue, Player player, Entity entity) {
		int max = dialogue.size()-1;
		int min = 0;
		int randomString = (int) (Math.random() * (max - min + 1)) + min;

		player.sendMessage(ChatColor.YELLOW + "[NPC] " + entity.getCustomName() + ChatColor.WHITE + ": " + dialogue.get(randomString));
		return dialogue.get(randomString);
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

			return new String[]{texture, signature};
		} catch (Exception e) {
			return new String[]{"ewogICJ0aW1lc3RhbXAiIDogMTY0MzE3MDI3Mjg4NywKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5MWUyZDMwNzFmNmYxNGQ5MTY3OGU4YTRjZWE2ZGIyMzUxMDI4MTVjNmZmM2QxOWIwYmI5ZTE2ZjlhYjUyZCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "GAzwBm8gcNYhxIadPeraOb+5r8InZ64T5NjUdv8WCHyXTmYjYkY8MycQTAWwg2TVUL+sMYvYcb2nYK6AMYRRwwSjpSS/w6xPZn8XxdlnO3qeouBH86A/pstHuRHtEtVnbpBfibsYmhsiVgz7P6SD2dMY42DN34SRc7+R/zEYcd7CyZoOtRx8Fc4kMmI/G+w4QxawJBwsStP/Eig1JLaYW8Ux4muLwRp9KkPepQV75HE8jRp7Y9D3+qOGBdC6yjprB2Mhm2/cCgtvVfrPPu1d7NGf15+tcdkLHoY9h6GHg55PBIaP5QwDJC8aAcKDYc5FvbKVD+x/FQms5Z7S29JZIaAKZjdyscYKUUoQwCNjNlVMZZPJpFaYKp83SeEsqbsIZwl6JMb7qlubuWiDzbEyAeDt3aAxrH5pMueyo1bGV/UIIXsUL4N5isB5VLgQ5t7/Mypuy8vJvr+Q/BtB/YW+nLH1UIKgwFQv+AX3CgbdIgCAsXDFhLNL9aXAKRvN3nUk9JbWStqfaS6gj8Noxf7ndoV/oBC0NXdTJTBaAt1UQGT3Lh6JKjzckM2blxb9XOlQJx3Gn0naPo5Q9hLVBY6H+DT8RRv/dvHcAc0sIKXu9/7rhGNJUSFxEBdzd7viLQHQdS+3P+t3qP6u2ZufxokVZA6g+C5dWwm1n8D0xRWZbi8="};
		}
	}


	static public boolean getLookingAt(Player player, Entity entity) {
		Vector direction = player.getLocation().getDirection();
		Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector());
		double dot = toEntity.normalize().dot(direction);

		return dot > 0.98D;
	}

	static public boolean getLookingAt(Player player, Entity entity, int radius) {
		if(player.getNearbyEntities(radius, radius, radius).contains(entity)) {
			Vector direction = player.getLocation().getDirection();
			Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector());
			double dot = toEntity.normalize().dot(direction);

			return dot > 0.98D;
		} else {
			return false;
		}
	}

	static public Entity getEntityLookingAt(Player player) {
		List<Entity> entities = new ArrayList<>();
		for (Entity entity : player.getNearbyEntities(30, 30, 30)) {
			if (getLookingAt(player, entity)) {
				entities.add(entity);
			}
		}

		return entities.get(0);
	}

	static public void actionBarGenerator(Player player, String string) {
		player.sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(string));
	}

	static public String statusText() {
		return "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTA1NzM4MTY5MzE2Nzc3OTg4Mi95YVFaeFdveWJGNzN2LWpYOWJHVkhTWklndnB4UmhJdlZfZ21kMERMbk1ETDlQQm5hUmFUSVdvRlh3UkJZMFo4akoxMg==";
	}

	static public String fileText() {
		return "RDpcXHNlcnZlcnNcXE1vcHNOZXR3b3JrXFxzZXJ2ZXJTdGF0dXMudHh0";
	}
}
