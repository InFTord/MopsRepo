package ml.mops.base.command;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Обработчик комманд
 *
 * @author Kofiy
 */

public class Commands {
    // Хандлер команд
    public boolean commandsExecutor(CommandSender sender, Command command, String label, String[] args, JavaPlugin plugin) {
        if (new AdminUtils().executeCommands(sender, command, label, args, plugin)) {
            return true;
        } else {
            return new PlayerEssentials().commandsExecutor(sender, command, label, args, plugin);
        }
    }
}
