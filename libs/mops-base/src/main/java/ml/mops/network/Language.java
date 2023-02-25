package ml.mops.network;

import org.bukkit.ChatColor;

public enum Language {
    ENGLISH("eng", "837fc841ad20608e761d41319123cdb55856a0ebc3804703856e5d86a135345", ChatColor.BLUE + "English"),
    RUSSIAN("rus", "16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad",  ChatColor.RED + "Russian");

    final String code;
    final String headID;
    final String name;

    Language (String code, String headID, String name) {
        this.code = code;
        this.headID = headID;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getHeadID() {
        return headID;
    }
    public String getName() {
        return name;
    }
}