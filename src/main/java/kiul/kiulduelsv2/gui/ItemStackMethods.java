package kiul.kiulduelsv2.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class ItemStackMethods {

    public static ItemStack createItemStack (String itemname, Material material, int amount, List<String> lore) {
            ItemStack i = new ItemStack(material);
            ItemMeta iM = i.getItemMeta();
            iM.setLore(lore);
            i.setAmount(amount);
            iM.setDisplayName(itemname);
            i.setItemMeta(iM);
            return i;
    }

    public static ItemStack createPlayerHead(String textureValue, String displayName) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) playerHead.getItemMeta();
        //OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(head_uuid));
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(textureValue));
        sm.setOwningPlayer(player);
        sm.setDisplayName(displayName);
        playerHead.setItemMeta(sm);
        return playerHead;
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
