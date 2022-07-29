package ml.mopsutils;

import ml.mopsbase.MopsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class Utilities {
	MopsPlugin plugin;

	public Utilities(MopsPlugin plugin) {
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
			if (!Arrays.stream(excludes).anyMatch(x -> x == j)) {
				joiner.add(strings[j]);
			}
		}
		return joiner.toString();

	}
	static public String combineStrings(CharSequence[] strings, CHARACTER character, Integer[] excludes) {
		StringJoiner joiner = new StringJoiner(character.getString());
		for (int i = 0; i<strings.length; i++) {
			final int j = i;
			if (!Arrays.stream(excludes).anyMatch(x -> x == j)) {
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
}
