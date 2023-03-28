package ml.mops.lobby;

import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Change {
    public int version = 0;
    public String change = "nothing new";
    public int type = 0;

    // 0 - added
    // 1 - changed
    // 2 - removed

    Change(int version, String change, int type) {
        this.version = version;
        this.change = change;
        this.type = type;
    }

    public String getText(int version) {
        String string = "";

        if(this.version == version) {
            string += ChatColor.GREEN + "" + ChatColor.BOLD + "NEW! ";
        }

        switch (type) {
            case 0 -> {
                string += ChatColor.DARK_GREEN + "+ ";
            }
            default -> {
                string += ChatColor.GRAY + "- ";
            }
            case 2 -> {
                string += ChatColor.DARK_RED + "- ";
            }
        }

        String additionalText = "";
        List<String> lines = new ArrayList<>();

        int lineCount = 0;

        int i = 0;
        while (i < change.length()) {
            additionalText += change.charAt(i);

            if(additionalText.length() * 4 >= 105) {
                if(lineCount >= 1) {
                    lines.add("   " + additionalText);
                } else {
                    lines.add(additionalText);
                }
                additionalText = "";

                lineCount++;
            }

            if(lineCount == change.length() - 1) {
                lines.add(additionalText);

                lineCount++;
            }

            i++;
        }

        string += ChatColor.BLACK + MopsUtils.combineStrings(lines, "\n") + ChatColor.RESET;

        return string;
    }
}
