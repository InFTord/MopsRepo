package ml.mopsutils;

//import ml.mopspvps.Dependencies;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class Utils {

	protected Plugin plugin;

	public Utils(Plugin plugin) {
		this.plugin = plugin;
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
		StringJoiner joiner = new StringJoiner(character.getSymbol());
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
