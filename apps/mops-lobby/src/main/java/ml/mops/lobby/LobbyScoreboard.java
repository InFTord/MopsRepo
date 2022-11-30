package ml.mops.lobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class LobbyScoreboard {
    public Scoreboard generateLobbyScoreboard(Player player, Long time) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();

        String timeChar = ChatColor.GOLD + "☀";

        boolean dayCondition = (time >= 0 && time <= 12000) || (time >= 23500 && time <= 24000);
        if(!dayCondition) {
            timeChar = ChatColor.BLUE + "☽";
        }

        String score1 = ChatColor.GRAY + "Welcome! " + timeChar;
        String score2 = ChatColor.RED + " ";
        String score3 = ChatColor.WHITE + "Your coins: " + ChatColor.GOLD + "" + ChatColor.BOLD + "420";
        String score4 = ChatColor.WHITE + "Your rank: " + ChatColor.GREEN + "[DEV]";
        String score5 = ChatColor.GOLD + " ";
        String score6 = ChatColor.WHITE + "You are in: " + ChatColor.YELLOW + "Lobby";

        Objective obj = board.registerNewObjective("MopsPVPs", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "MopsPVPs");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore(score1).setScore(10);
        obj.getScore(score2).setScore(9);
        obj.getScore(score3).setScore(8);
        obj.getScore(score4).setScore(7);
        obj.getScore(score5).setScore(6);
        obj.getScore(score6).setScore(5);

        return board;
    }
}
