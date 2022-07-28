package ml.mopslobby;

import ml.mopslobby.Commands;
//import ml.mopspvps.Dependencies;
//import ml.mopspvps.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Locale;

/**
 * Обработчик комманд обычных игроков
 *
 * @author Kofiy
 */

public class PlayerEssentials extends Commands {
	@Override
	public boolean commandsExecutor(CommandSender sender, Command command, String label, String[] args, Plugin plugin) {
		Player player = (Player) sender;
		String commandName = command.getName().toLowerCase(Locale.ROOT);

		if (commandName.equals("hub") || commandName.equals("lobby") || commandName.equals("l")) {
			player.sendMessage("тп в хаб");
			return true;
		}
		if (commandName.equals("kit")) {
			//player.openInventory(Dependencies.getKits());
			player.sendMessage("киты не работают");
			return true;
		}
		if (commandName.equals("calc")) {
			double number1 = Double.parseDouble(args[0]);
			double number2 = Double.parseDouble(args[2]);
			String usage = args[1];
			double result = 0;
			if (usage.equals("+")) {
				result = number1 + number2;
			}
			if (usage.equals("-")) {
				result = number1 - number2;
			}
			if (usage.equals("*")) {
				result = number1 * number2;
			}
			if (usage.equals("/")) {
				result = number1 / number2;
			}
			player.sendMessage("Результат: " + result + "");
			return true;
		}
		return false;
	}
}
