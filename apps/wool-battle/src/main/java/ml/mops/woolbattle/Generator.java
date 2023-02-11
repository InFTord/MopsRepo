package ml.mops.woolbattle;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

import java.util.List;

public class Generator {
    String status  = "woolbattle.generator.uncaptured";

    List<Block> blockList;
    List<Block> blockListLong;

    String percent = ChatColor.GRAY + " (0%)";
    String genLetter;

    boolean firstCapture = true;

    Generator(String genLetter) {
        this.genLetter = genLetter;
    }

    public void setPercent(String string) {
        percent = string;
    }
    public void setStatus(String string) {
        status = string;
    }

    public void setBlocks(List<Block> list) {
        blockList = list;
    }
    public void setLongBlocks(List<Block> list) {
        blockListLong = list;
    }

    public String getStatus() {
        return status;
    }
    public String getPercent() {
        return percent;
    }
    public String getGenLetter() {
        return genLetter;
    }

    public List<Block> getBlocks() {
        return blockList;
    }
    public List<Block> getLongBlocks() {
        return blockListLong;
    }
}
