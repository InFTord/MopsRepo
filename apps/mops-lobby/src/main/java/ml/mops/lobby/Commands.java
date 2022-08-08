package ml.mops.lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Обработчик комманд
 *
 * @author Kofiy
 */

public class Commands {
	// Хандлер команд
	public boolean commandsExecutor(CommandSender sender, Command command, String label, String[] args, Plugin plugin) {
		if (new AdminUtils().execCommands(sender, command, label, args, plugin)) {
			return true;
		} else {
			return new PlayerEssentials().commandsExecutor(sender, command, label, args, plugin);
		}
	}
}
