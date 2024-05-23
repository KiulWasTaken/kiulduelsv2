package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.inventory.GlobalKits;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.*;

public class ClickMethods {

    public static ArrayList<Player> inEditor = new ArrayList<>();

    public static Map<Material,Integer> limitedItems = new HashMap() {{
       put(Material.ENCHANTED_GOLDEN_APPLE,2);
       put(Material.END_CRYSTAL,0);
       put(Material.RESPAWN_ANCHOR,0);
    }};
    public static Set<PotionEffectType> limitedEffects = new HashSet<>() {{
        add(PotionEffectType.SLOW_FALLING);
    }};




    public static void enterKitEditor (Player p) {
        inEditor.add(p);
        ItemInventory.itemInventory(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200000,1,false,false));
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),p);
        }
        p.getInventory().clear();
        if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + KitMethods.kitSlot.get(p)) != null) {
            try {
                KitMethods.loadSelectedKitSlot(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        p.sendMessage(ChatColor.GRAY + " " + ChatColor.ITALIC + "Left-Click to open the item menu / Right-Click to open the enchant menu");
    }

    public static boolean itemAmountIsWithinLimit (Player p, ItemStack itemStack) {
        if (limitedItems.containsKey(itemStack.getType())) {
            if (p.getInventory().containsAtLeast(itemStack,limitedItems.get(itemStack.getType())) || itemStack.getAmount() > limitedItems.get(itemStack.getType())) {
//                if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.TIPPED_ARROW) {
//                    PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
//                    for (PotionEffect potionEffect : potionMeta.getCustomEffects()) {
//                        if (limitedEffects.contains(potionEffect.getType())) {
//                            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "This potion is disabled");
//                            return false;
//                        }
//                    }
//                }
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The limit for this item is " + ChatColor.YELLOW + ChatColor.ITALIC + limitedItems.get(itemStack.getType()));
                return false;
            }
        }
    return true;}
}
