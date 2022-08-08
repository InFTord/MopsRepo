package ml.mops.lobby;

//import ml.mopspvps.Dependencies;
//import ml.mopspvps.Plugin;
import ml.mops.network.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

		switch (commandName) {
			case "hub", "lobby", "l" -> {
				player.sendMessage("тп в хаб");
				return true;
			}
			case "kit" -> {
				player.sendMessage("киты не работают");
				return true;
			}
			case "calc" -> {
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
			case "party" -> {
				player.sendMessage("штуки пати, я хз как они будут работать :)");

				switch (args[0]) {
					case "invite" -> {
						player.sendMessage("инвайт в пати, я хз как он будет работать :)");
					}
					case "disband" -> {
						player.sendMessage("роспуск пати, я хз как он будет работать :)");
					}
				}
			}
		}
		return false;
	}
}
