package kiul.kiulduelsv2;

import kiul.kiulduelsv2.duel.DuelManager;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// not used in my user, implement it yourself.
public class C {

    public static Plugin plugin = Kiulduelsv2.getPlugin(Kiulduelsv2.class);
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-fA-F])");
    public static int K = 30;


    public static String GOLD = "&#ebbc3d";
    public static String LIGHT_RED = "&#f5633b";
    public static String RED = "&#e33630";
    public static String DARK_RED = "&#a11813";
    public static String GREEN = "&#27a33a";
    public static String YELLOW = "&#b59a4e";
    public static String BLUE = "&#658bb5"; //old &#5f95ed
    public static String GRAY = "&#787878";
    public static String PURPLE = "&#6d2b94";
    public static String PINK = "&#c73e8b";
    public static String LIGHT_GREEN = "&#31e862";
    public static String DARK_GREEN = "&#218a3c";
    /*#c73e8b pink
#6d2b94 purple*/
    public static String failPrefix = C.t(DARK_RED+"❌ "+RED);
    public static String successPrefix = C.t(DARK_GREEN+"✔ "+GREEN);
    public static double safeDivide(double dividend, double divisor) {
        if(Double.compare(divisor, Double.NaN) == 0) return Double.NaN;
        if(Double.compare(dividend, Double.NaN) == 0) return Double.NaN;
        if(Double.compare(divisor, 0.0) == 0) {
            if(Double.compare(dividend, 0.0) == -1) {
                dividend = 1.0;
            }
            divisor = 1.0;
        }
        if(Double.compare(divisor, -0.0) == 0) {
            if(Double.compare(dividend, -0.0) == 1) {
                dividend = 1.0;
            }
            divisor = 1.0;
        }
        return dividend / divisor;
    }

    public static String t(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static PartyManager partyManager = new PartyManager();
    public static DuelManager duelManager = new DuelManager();

    public static int[] splitTimestampSince(long pastTimeStamp) {
        long millisecondsRemaining = System.currentTimeMillis() - pastTimeStamp;
        long hours = millisecondsRemaining / 3600000;
        long minutes = (millisecondsRemaining % 3600000) / 60000;
        long seconds = ((millisecondsRemaining % 3600000) % 60000) / 1000;
        return new int[]{(int)hours, (int)minutes, (int)seconds};
    }
    public static int[] splitTimestampUntil(long futureTimestamp) {
        long millisecondsRemaining = futureTimestamp - System.currentTimeMillis();
        long hours = millisecondsRemaining / 3600000;
        long minutes = (millisecondsRemaining % 3600000) / 60000;
        long seconds = ((millisecondsRemaining % 3600000) % 60000) / 1000;
        return new int[]{(int)hours, (int)minutes, (int)seconds};
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static ItemStack createItemStack (String itemName, Material material, int amount, String[] lore, Enchantment enchantment, Integer enchantLvl, String localizedName, URL URL) {
        ItemStack i = new ItemStack(material);
        if (material == Material.PLAYER_HEAD) {
            if (URL != null) {
                i = C.getHeadFromURL(URL);
            }
        }
        ItemMeta iM = i.getItemMeta();
        iM.setMaxStackSize(amount);
        List<String> adjustedLore = new ArrayList<>();
        for (String oldLore : lore) {
            adjustedLore.add(C.t(oldLore));
        }
        iM.setLore(adjustedLore);
        if (localizedName != null) {
            iM.getPersistentDataContainer().set(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING,localizedName);
        }

        i.setAmount(amount);
        iM.setItemName(C.t(itemName));
        if (enchantment != null) {
            iM.addEnchant(enchantment, enchantLvl, true);
        }

        i.setItemMeta(iM);
        return i;
    }

    public static ItemStack getHeadFromURL(URL value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        profile.getTextures().setSkin(value);
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);

        return head;
    }

    public static URL getURL (String URL) {
        URL url = null;
        try {
            url = new URL(URL);
        } catch (MalformedURLException err) {err.printStackTrace();}
        return url;
    }

}
