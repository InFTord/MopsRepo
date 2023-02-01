package ml.mops.base;

import org.bukkit.ChatColor;

public class Value {
    private String[] borderChar = new String[] {"[", "]"};
    private ChatColor borderColor = ChatColor.GOLD;
    private String defaultChar = "-";
    private ChatColor defaultColor = ChatColor.YELLOW;
    private String usedChar = "X";
    private ChatColor usedColor = ChatColor.GRAY;

    // if you need to squish the amount into X bars use this
    // isnt implemented yet actually i would need to do that later
    private int squishAmount = 0;

    private int maxAmount = 5;
    private int currentAmount = 4;

    public void setValues(String leftBorder, String rightBorder, ChatColor borderColor, ChatColor defaultColor, ChatColor usedColor, String usedChar, String defaultChar) {
        this.defaultColor = defaultColor;
        this.usedColor = usedColor;
        this.borderColor = borderColor;
        this.usedChar = usedChar;
        this.defaultChar = defaultChar;
        borderChar = new String[] {leftBorder, rightBorder};
    }

    public String getIndicator() {
        String firstBorder = borderColor + borderChar[0];
        String secondBorder = borderColor + borderChar[1];

        String bar = "";

        if(squishAmount == 0) {
            bar = (usedColor + usedChar).repeat(currentAmount) + (defaultChar + defaultColor).repeat(maxAmount-currentAmount);
        }

        return firstBorder + bar + secondBorder;
    }



    public void setCurrentAmount(int value) {
        currentAmount = value;
    }
    public void setMaxAmount(int value) {
        maxAmount = value;
    }
    public void setSquishAmount(int value) {
        squishAmount = value;
    }
}
