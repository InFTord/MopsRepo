package ml.mops.woolbattle;

import ml.mops.utils.MopsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.TextComponentTagVisitor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Items {

    final Plugin plugin;

    Items(Plugin plugin) {
        this.plugin = plugin;
    }


    public ItemStack shears(String lang) {
        ItemStack item = new ItemStack(Material.SHEARS);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "shears.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "shears.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack boomstickMK1(String lang) {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "boomstick.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "boomstick.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack slimeball(String lang) {
        ItemStack item = new ItemStack(Material.SLIME_BALL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "slimeball.name"));

        List<Component> lore = new ArrayList<>();
        lore.add(plugin.getByLang(lang, "slimeball.lore1"));
        lore.add(plugin.getByLang(lang, "slimeball.lore2"));
        lore.add(plugin.getByLang(lang, "slimeball.lore3"));
        meta.lore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack stockBow(String lang) {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "bow.name"));

        meta.setLore(new ArrayList<>(Collections.singleton(plugin.getStringByLang(lang, "bow.lore"))));

        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack arrow(String lang) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "arrow.name"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack leaveButton(String lang) {
        ItemStack item = new ItemStack(Material.MANGROVE_DOOR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "leaveButton.name"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack doubleJumpBoots(String lang, Teams team) {
        String colorString = team.getColorString;
        Color color = team.getLeatherColor;

        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.displayName(plugin.getByLang(lang, "doubleJumpBoots.name", Map.of("TCC", colorString)));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "doubleJumpBoots.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.setUnbreakable(true);
        meta.setColor(color);
        item.setItemMeta(meta);

        return item;
    }










    public ItemStack shield(String lang) {
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "shield.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "shield.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        item.setItemMeta(meta);
        item.setDurability((short) 320);

        return item;
    }

    public ItemStack leaves(String lang) {
        ItemStack item = new ItemStack(Material.OAK_LEAVES);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "leaves.name"));
        meta.setLore(new ArrayList<>(Collections.singleton(plugin.getStringByLang(lang, "leaves.lore"))));
        item.setItemMeta(meta);
        item.setAmount(64);

        return item;
    }

    public ItemStack axe(String lang) {
        ItemStack item = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "axe.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "axe.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        item.setItemMeta(meta);
        item.setDurability((short) 115);

        return item;
    }

    public ItemStack potion(String lang) {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "jumpboost.name"));

        meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 1800, 1), true);
        meta.setColor(Color.LIME);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack platform(String lang) {
        ItemStack item = new ItemStack(Material.BRICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "platform.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "platform.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

//        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound nbtTag = new NBTTagCompound();
//        nbtTag.a("itemId", "platform");
//        nmsItem.b(nbtTag);
//        item = CraftItemStack.asCraftMirror(nmsItem);

        return item;
    }

    public ItemStack enderpearl(String lang) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "enderpearl.name"));

        meta.setLore(new ArrayList<>(Collections.singleton(plugin.getStringByLang(lang, "enderpearl.lore"))));

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        item.setAmount(16);

        return item;
    }

    public ItemStack boomstickMK2(String lang) {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "boomstickMK2.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "boomstickMK2.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        item.setAmount(1);

        return item;
    }

    public ItemStack flareGun(String lang) {
        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "flareGun.name"));

        List<String> lore = new ArrayList<>();
        lore.add(plugin.getStringByLang(lang, "flareGun.lore"));
        lore.add(plugin.getStringByLang(lang, "flareGun.lore2"));
        lore.add(" ");
        lore.add(plugin.getStringByLang(lang, "flareGun.flareCount") + ChatColor.RED + "3/3");

        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.setColor(Color.fromRGB(204, 47, 47));
        item.setItemMeta(meta);

        item.setAmount(1);

        return item;
    }







    public ItemStack sniperRifle(String lang) {
        ItemStack item = new ItemStack(Material.SPYGLASS);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "sniperRifle.name"));

        List<String> lore = new ArrayList<>();
        lore.add(plugin.getStringByLang(lang, "sniperRifle.lore"));
        lore.add(plugin.getStringByLang(lang, "sniperRifle.lore2"));

        item.setItemMeta(meta);

        item.setAmount(1);

        return item;
    }

    public ItemStack platform3d(String lang) {
        ItemStack item = new ItemStack(Material.BRICKS);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getByLang(lang, "platform3d.name"));

        List<String> lore = new ArrayList<>(Arrays.asList(MopsUtils.textComponentToString(plugin.getByLang(lang, "platform3d.lore")).split("\n")));
        int i = 0;
        while(i < lore.size()) {
            lore.set(i, MopsUtils.convertColorCodes(ChatColor.RESET + "" + ChatColor.GRAY + lore.get(i)));
            i++;
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }
}
