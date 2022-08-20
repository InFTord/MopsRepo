package ml.mops.base;

import org.bukkit.ChatColor;

public class Value {
    private String fillerChar = "-";
    private int fillerCount = 5;
    private String[] borderChar = new String[] {"[", "]"};
    private ChatColor borderColor = ChatColor.GOLD;
    private ChatColor fillerColor = ChatColor.YELLOW;
    private ChatColor usedColor = ChatColor.GRAY;

    private int maxAmount = 5;
    private int currentAmount = 4;


    public void setValues(String leftBorder, String rightBorder, String fill, int count, ChatColor color, ChatColor usedcolor) {
        fillerChar = fill;
        borderChar = new String[] {leftBorder, rightBorder};
        fillerCount = count;
        fillerColor = color;
        borderColor = color;
        usedColor = usedcolor;
    }

    public String getIndicator() {
        ChatColor realBorderColor = borderColor;
        ChatColor realFillerColor = fillerColor;

        if(realBorderColor == null) {
            realBorderColor = fillerColor;
        } else if(realFillerColor == null) {
            realFillerColor = borderColor;
        }

        String firstBorder = realBorderColor + borderChar[0];
        String secondBorder = realBorderColor + borderChar[1];

        int currentIndicatorCells = Math.round(currentAmount/maxAmount*fillerCount);
        int margin = fillerCount - currentIndicatorCells;

        String filler = (realFillerColor + fillerChar).repeat(Math.max(0, currentIndicatorCells)) +
                        (usedColor + fillerChar).repeat(Math.max(0, margin));

        return firstBorder + filler + secondBorder;
    }



    public void setCurrentAmount(int value) {
        currentAmount = value;
    }
    public void setMaxAmount(int value) {
        maxAmount = value;
    }
    public void setFillerCount(int value) {
        fillerCount = value;
    }


    public void setFillerChar(String character) {
        fillerChar = character;
    }
    public void setBorderChar(String[] array) {
        borderChar = array;
    }


    public void setBorderColor(ChatColor color) {
        borderColor = color;
    }
    public void setFillerColor(ChatColor color) {
        fillerColor = color;
    }
    public void setUsedColor(ChatColor color) {
        usedColor = color;
    }
}
