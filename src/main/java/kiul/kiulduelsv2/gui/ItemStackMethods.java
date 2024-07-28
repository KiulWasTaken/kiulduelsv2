package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class ItemStackMethods {

    public static ItemStack createItemStack (String itemname, Material material, int amount, List<String> lore, Enchantment enchantment, Integer enchantLvl,String localizedName) {
            ItemStack i = new ItemStack(material);
            ItemMeta iM = i.getItemMeta();
            List<String> adjustedLore = new ArrayList<>();
            for (String oldLore : lore) {
                adjustedLore.add(C.t(oldLore));
            }
            iM.setLore(adjustedLore);
            if (localizedName != null) {
                iM.getPersistentDataContainer().set(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING,localizedName);
            }
            i.setAmount(amount);
            iM.setDisplayName(C.t(itemname));
            if (enchantment != null) {
                iM.addEnchant(enchantment, enchantLvl, true);
            }
            i.setItemMeta(iM);
            return i;
    }

    public static ItemStack createSkullItem(String displayName,Player player,ArrayList<String> lore,String localizedName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        if (localizedName != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING, localizedName);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPotion(String displayName, Material mat, int amount, PotionType potionType) {
        ItemStack itemStack = new ItemStack(mat);
        itemStack.setAmount(amount);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setBasePotionType(potionType);
        potionMeta.setItemName(displayName);
        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    public static String translateHexColorCodes(String startTag, String endTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
