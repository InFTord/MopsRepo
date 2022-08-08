package ml.mops.utils;

import org.apache.commons.lang3.StringUtils;
import ml.mops.base.MopsPlugin;
import ml.mops.exception.UnsoportedYetFeature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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

	@Deprecated
	static public String legacyAmpersandStringToDeprecatedBukkitChatColor(String string, Map<String, String> customMapping, MAP_BOOLEAN_MODE customMappingOption) throws UnsoportedYetFeature {
		Map<String, String> defaultMapping = new HashMap<String, String>();
		defaultMapping.put("&0", ChatColor.BLACK + "");
		defaultMapping.put("&1", ChatColor.DARK_BLUE + "");
		defaultMapping.put("&2", ChatColor.DARK_GREEN + "");
		defaultMapping.put("&3", ChatColor.DARK_AQUA + "");
		defaultMapping.put("&4", ChatColor.DARK_RED + "");
		defaultMapping.put("&5", ChatColor.DARK_PURPLE + "");
		defaultMapping.put("&6", ChatColor.GOLD + "");
		defaultMapping.put("&7", ChatColor.GRAY + "");
		defaultMapping.put("&8", ChatColor.DARK_GRAY + "");
		defaultMapping.put("&9", ChatColor.BLUE + "");
		defaultMapping.put("&a", ChatColor.GREEN + "");
		defaultMapping.put("&b", ChatColor.AQUA + "");
		defaultMapping.put("&c", ChatColor.RED + "");
		defaultMapping.put("&d", ChatColor.LIGHT_PURPLE + "");
		defaultMapping.put("&e", ChatColor.YELLOW + "");
		defaultMapping.put("&f", ChatColor.WHITE + "");
		defaultMapping.put("&k", ChatColor.MAGIC + "");
		defaultMapping.put("&l", ChatColor.BOLD + "");
		defaultMapping.put("&m", ChatColor.STRIKETHROUGH + "");
		defaultMapping.put("&n", ChatColor.UNDERLINE + "");
		defaultMapping.put("&o", ChatColor.ITALIC + "");
		defaultMapping.put("&r", ChatColor.RESET + "" + ChatColor.WHITE);

		Map<String, String> mapping = new LinkedHashMap<String, String>();

		switch (customMappingOption) {
			case IGNORE -> {
				mapping.putAll(defaultMapping);
			}
			case UNION -> {
				mapping.putAll(defaultMapping);
				mapping.putAll(customMapping);
			}
			case DIFFERENCE,SUBTRACTION,INTERSECTION -> {
				mapping.putAll(defaultMapping);
				throw new UnsoportedYetFeature("DIFFERENCE, SUBSRACTION and INTERSECTION in legacyAmpersandStringToDeprecatedBukkitChatColor()");
			}

		}
		int size = mapping.size();
		String[] keys = mapping.keySet().toArray(new String[size]);
		String[] values = mapping.values().toArray(new String[size]);
		return StringUtils.replaceEach(string, keys, values);
	}
}
