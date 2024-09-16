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
import org.bukkit.potion.PotionType;

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
        put("cart", new HashMap<Material,Integer>() {{
            put(Material.EXPERIENCE_BOTTLE,128);
            put(Material.ENCHANTED_GOLDEN_APPLE,3);
            put(Material.END_CRYSTAL,0);
            put(Material.RESPAWN_ANCHOR,0);
            put(Material.MACE,0);
        }});
    }};
    static Map<String,ArrayList<PotionType>> limitedEffects = new HashMap() {{
        put("shield",new ArrayList<PotionType>() {{
            add(PotionType.SLOW_FALLING);
            add(PotionType.LONG_SLOW_FALLING);
        }});
        put("cart",new ArrayList<PotionType>() {{
            add(PotionType.SLOW_FALLING);
            add(PotionType.LONG_SLOW_FALLING);
            add(PotionType.POISON);
            add(PotionType.STRONG_POISON);
            add(PotionType.LONG_POISON);
            add(PotionType.STRONG_HARMING);
            add(PotionType.HARMING);
            add(PotionType.WEAKNESS);
            add(PotionType.LONG_WEAKNESS);
            add(PotionType.SLOWNESS);
            add(PotionType.STRONG_SLOWNESS);
            add(PotionType.LONG_SLOWNESS);
        }});
        put("smp",new ArrayList<PotionType>() {{
            add(PotionType.SLOW_FALLING);
            add(PotionType.LONG_SLOW_FALLING);
        }});
        put("crystal",new ArrayList<PotionType>() {{

        }});
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
        p.sendMessage(C.t("&#4DBA4B→ &fRIGHT-CLICK&7 on items in your inventory to &dENCHANT&7"));
        p.sendMessage(C.t("&#4DBA4B→ &fSHIFT-RIGHT-CLICK&7 on an empty slot to &eADD ITEMS"));
        p.sendMessage(C.t("&#4DBA4B→ &f/save &7to &fSAVE &7your kit"));
        p.sendMessage(C.t("&#4DBA4B→ &f/cancel &7to &cCANCEL &7editing."));
        p.sendMessage(C.t("&#4DBA4B&m                                                                "));
        if (Userdata.get().get("kits." + p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type)) != null) {
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
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "The limit for this item is " + ChatColor.YELLOW + ChatColor.ITALIC + limitedItems.get(type).get(itemStack.getType()));
                return false;
            }
        }
        if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.TIPPED_ARROW) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                if (limitedEffects.get(type).contains(potionMeta.getBasePotionType())) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "This potion is disabled");
                    return false;
                }
        }
    return true;}
}
