package ml.mops.pvps;

import ml.mops.base.commands.AdminUtils;
import ml.mops.pvps.commands.PlayerEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Обработчик комманд
 *
 * @author Kofiy
 */
 
public class Commands {
	// Хандлер команд
	public boolean commandsExecutor(CommandSender sender, Command command, String label, Plugin plugin, String[] args) {
		if (new AdminUtils().executeCommands(sender, command, label, args, plugin)) {
			return true;
		} else {
			return new PlayerEssentials().commandsExecutor(sender, command, label, plugin, args);
		}
	}
}
