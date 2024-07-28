package kiul.kiulduelsv2.gui.clickevents;

import it.unimi.dsi.fastutil.Hash;
import kiul.kiulduelsv2.C;
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

    public static HashMap<Player,String> inEditor = new HashMap<>();

    static Map<String,HashMap<Material,Integer>> limitedItems = new HashMap() {{
       put("smp", new HashMap<Material,Integer>() {{
           put(Material.EXPERIENCE_BOTTLE,128);
           put(Material.ENCHANTED_GOLDEN_APPLE,3);
           put(Material.END_CRYSTAL,0);
           put(Material.RESPAWN_ANCHOR,0);
       }});
        put("crystal", new HashMap<Material,Integer>() {{
            put(Material.ENCHANTED_GOLDEN_APPLE,5);
        }});
        put("shield", new HashMap<Material,Integer>() {{
            put(Material.EXPERIENCE_BOTTLE,128);
            put(Material.ENCHANTED_GOLDEN_APPLE,3);
            put(Material.END_CRYSTAL,0);
            put(Material.RESPAWN_ANCHOR,0);
            put(Material.MACE,0);
            put(Material.TNT_MINECART,0);
            put(Material.TNT,0);

        }});
    }};
    static Set<PotionEffectType> limitedEffects = new HashSet<>() {{
        add(PotionEffectType.SLOW_FALLING);
    }};




    public static void enterKitEditor (Player p,String type) {
        inEditor.put(p,type);

        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200000,1,false,false));
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),p);
        }
        p.getInventory().clear();

        p.closeInventory();
        p.sendMessage(C.t("&#4DBA4B&m                                                                "));
        p.sendMessage(C.t("&7Welcome to the Kit Editor!"));
        p.sendMessage(C.t("&7To create/edit your kit, first open your inventory."));
        p.sendMessage(C.t("&7You can add items to your kit by &#4DBA4BSHIFT-LEFT-CLICKING"));
        p.sendMessage(C.t("&7anywhere in your inventory."));
        p.sendMessage(C.t("&7likewise, you can enchant & customize items in your kit"));
        p.sendMessage(C.t("&7by &#4DBA4BSHIFT-RIGHT-CLICKING &7on the item that you want to edit."));
        p.sendMessage(C.t(""));
        p.sendMessage(C.t("&#4DBA4B&m                                                                "));
        if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type)) != null) {
            try {
                KitMethods.loadSelectedKitSlot(p,type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean itemAmountIsWithinLimit (Player p, ItemStack itemStack,String type) {
        if (limitedItems.get(type).containsKey(itemStack.getType())) {

            if (p.getInventory().containsAtLeast(itemStack,limitedItems.get(type).get(itemStack.getType())) || itemStack.getAmount() > limitedItems.get(type).get(itemStack.getType())) {
//                if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.TIPPED_ARROW) {
//                    PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
//                    for (PotionEffect potionEffect : potionMeta.getCustomEffects()) {
//                        if (limitedEffects.contains(potionEffect.getType())) {
//                            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "This potion is disabled");
//                            return false;
//                        }
//                    }
//                }
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The limit for this item is " + ChatColor.YELLOW + ChatColor.ITALIC + limitedItems.get(type).get(itemStack.getType()));
                return false;
            }
        }
    return true;}
}
