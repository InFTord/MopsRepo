package ml.mops.woolbattle;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import java.util.List;

public class Generator {
    String status  = "woolbattle.generator.uncaptured";

    List<Block> blockList;
    List<Block> longBlockList;

    String percent = ChatColor.GRAY + " (0%)";
    String genLetter = "";

    Generator(List<Block> blockList, List<Block> longBlockList, String genLetter) {
        this.blockList = blockList;
        this.longBlockList = longBlockList;
        this.genLetter = genLetter;
    }

    public void setPercent(String string) {
        percent = string;
    }

    public void setStatus(String string) {
        status = string;
    }
}
