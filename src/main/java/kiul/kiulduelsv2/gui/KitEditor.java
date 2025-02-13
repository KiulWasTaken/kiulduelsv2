package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.gui.layout.LayoutMenuInventory;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import org.pattychips.pattyeventv2.Commands.Practice;
import org.pattychips.pattyeventv2.Events.InventoryClick;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class KitEditor implements Listener {

    public static HashMap<Player,String> inEditor = new HashMap<>();
    public static HashMap<Player,Boolean> editorMode = new HashMap<>();
    public static HashMap<Player,ItemStack[]> editorInventory = new HashMap<>();
    public static HashMap<Player,ItemStack[]> editorEnderchest = new HashMap<>();

    public static Map<String,HashMap<Material,Integer>> limitedItems = new HashMap() {{
       put("smp", new HashMap<Material,Integer>() {{
           put(Material.EXPERIENCE_BOTTLE,192);
           put(Material.ENCHANTED_GOLDEN_APPLE,3);
           put(Material.END_CRYSTAL,0);
           put(Material.RESPAWN_ANCHOR,0);
           put(Material.TOTEM_OF_UNDYING,12);
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

    public static Map<String,HashMap<Material,Integer>> penaltyItems = new HashMap<>() {{
        put("smp", new HashMap<Material,Integer>() {{
            put(Material.NETHERITE_HELMET,29);
            put(Material.NETHERITE_CHESTPLATE,42);
            put(Material.NETHERITE_LEGGINGS,40);
            put(Material.NETHERITE_BOOTS,34);
        }});
    }};
    public static Map<String,ArrayList<PotionType>> limitedEffects = new HashMap() {{
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
        editorMode.put(p,false);
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),onlinePlayers);
        }
        p.getInventory().clear();

        p.closeInventory();
        p.sendMessage(C.t("&#4DBA4B&m                                                                "));
        p.sendMessage(C.t("&#4DBA4B→ &fSNEAK&7 to re-open the &fEXPLORER GUI"));
        p.sendMessage(C.t("&#4DBA4B→ &fRIGHT-CLICK&7 on items in your inventory to &dENCHANT&7"));
        p.sendMessage(C.t("&#4DBA4B→ &fSHIFT-RIGHT-CLICK&7 on an empty slot to &eADD ITEMS"));
        p.sendMessage(C.t("&#4DBA4B&m                                                                "));
        if (CustomKitData.get().get(p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type)) != null) {
            try {
                KitMethods.loadSelectedKitSlot(p,type);
                if (CustomKitData.get().get(p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type)+".enderchest") != null) {
                    editorEnderchest.put(p, InventoryToBase64.itemStackArrayFromBase64((String)CustomKitData.get().get(p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type)+".enderchest")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LayoutMenuInventory.open(p);
    }

    public static int getAmountOfItemInInventory (Player p, Material itemStack) {
        ItemStack[] enderchestItems = editorMode.get(p) ? p.getInventory().getContents() : KitEditor.editorEnderchest.get(p);
        ItemStack[] inventoryItems = editorMode.get(p) ? KitEditor.editorInventory.get(p) : p.getInventory().getContents();
        ItemStack[] items = inventoryItems.clone();
        if (enderchestItems != null) {
            items = ArrayUtils.addAll(enderchestItems.clone(), inventoryItems.clone());
        }
        int totalItems = 0;
        for (ItemStack item : items) {
            if (item != null && item.getType() == itemStack)
                totalItems += item.getAmount();
        }
        return totalItems;
    }

    public static int getAdjustedXPLimit (Player p,String type) {
        ItemStack[] enderchestItems = editorMode.get(p) ? p.getInventory().getContents() : KitEditor.editorEnderchest.get(p);
        ItemStack[] inventoryItems = editorMode.get(p) ? KitEditor.editorInventory.get(p) : p.getInventory().getContents();
        ItemStack[] items = inventoryItems.clone();
        if (enderchestItems != null) {
            items = ArrayUtils.addAll(enderchestItems.clone(), inventoryItems.clone());
        }
        HashMap<Material,Integer> penalizedItemSightings = new HashMap<>();
        int xpPenalty = 0;
        for (Material penalizedItem : penaltyItems.get(type).keySet()) {
            penalizedItemSightings.put(penalizedItem,0);
        }
        for (ItemStack item : items) {
            if (item == null) {continue;}
            if (penaltyItems.get(type).keySet().contains(item.getType())) {
                penalizedItemSightings.put(item.getType(),penalizedItemSightings.get(item.getType())+1);
            }
        }
        for (Material penalizedItem : penalizedItemSightings.keySet()) {
            if (penalizedItemSightings.get(penalizedItem) > 0) {
                xpPenalty += (penalizedItemSightings.get(penalizedItem)-1) * penaltyItems.get(type).get(penalizedItem);
            }
        }
        return xpPenalty;
    }

    public static void updateXPAmount (Player p,int limit) {
        ItemStack[] enderchestItems = editorMode.get(p) ? p.getInventory().getContents() : KitEditor.editorEnderchest.get(p);
        ItemStack[] inventoryItems = editorMode.get(p) ? KitEditor.editorInventory.get(p) : p.getInventory().getContents();
        ItemStack[] items = inventoryItems.clone();
        if (enderchestItems != null) {
            items = ArrayUtils.addAll(enderchestItems.clone(), inventoryItems.clone());
        }
        int xpAmountSighted = 0;
        for (ItemStack item : items) {
            if (item != null && item.getType() == Material.EXPERIENCE_BOTTLE)
                xpAmountSighted += item.getAmount();
        }
        if (xpAmountSighted < limit) {return;}
        int amountToDelete = xpAmountSighted-limit;
        for (ItemStack item : items) {
            if (amountToDelete <= 0) {break;}
            if (item != null && item.getType() == Material.EXPERIENCE_BOTTLE) {
                if (amountToDelete >= item.getAmount()) {
                    amountToDelete -= item.getAmount();
                    item.setAmount(0);
                } else {
                    item.setAmount(item.getAmount()-amountToDelete);
                    amountToDelete = 0;
                }
            }
        }
    }

    public static boolean itemAmountIsWithinLimit (Player p, ItemStack itemStack, String type) {
        ItemStack[] enderchestItems = editorMode.get(p) ? p.getInventory().getContents() : KitEditor.editorEnderchest.get(p);
        ItemStack[] inventoryItems = editorMode.get(p) ? KitEditor.editorInventory.get(p) : p.getInventory().getContents();
        ItemStack[] items = inventoryItems.clone();
        if (enderchestItems != null) {
            items = ArrayUtils.addAll(enderchestItems.clone(), inventoryItems.clone());
        }


        int adjustedLimit = getAdjustedXPLimit(p,type);

        int itemLimit = limitedItems.get(type).get(Material.EXPERIENCE_BOTTLE)-adjustedLimit;

        if (penaltyItems.get(type).containsKey(itemStack.getType())) {
            if (getAmountOfItemInInventory(p,itemStack.getType()) > 0) {
                if (itemLimit - penaltyItems.get(type).get(itemStack.getType()) < 0) {
                    p.sendMessage(C.failPrefix + C.t("you cannot fit any more of these items: your maximum amount of xp is &e&o" + itemLimit + "&r" + C.RED + " and the cost of this item is &e&o" + penaltyItems.get(type).get(itemStack.getType())));
                    return false;
                } else {
                    updateXPAmount(p, (itemLimit - penaltyItems.get(type).get(itemStack.getType())));
                    p.sendMessage(C.t(C.YELLOW + "⚠" + C.GOLD + " your maximum amount of xp bottles has been reduced to &o" + C.RED + (itemLimit - penaltyItems.get(type).get(itemStack.getType()))));
                }
            }
        }

        int totalItems = 0;
        for (ItemStack item : items) {
            if (item != null && item.getType() == itemStack.getType())
                totalItems += item.getAmount();
        }

        if (limitedItems.get(type).containsKey(itemStack.getType())) {
            if (itemStack.getType() != Material.EXPERIENCE_BOTTLE) {
                itemLimit = limitedItems.get(type).get(itemStack.getType());
            }
            if (totalItems + itemStack.getAmount() > itemLimit) {
                if ((itemLimit - totalItems) > 1) {
                    itemStack.setAmount(itemLimit - totalItems);
                    if (p != null) {p.sendMessage(C.failPrefix + "Amount received adjusted to meet the limit");}
                    return true;
                }
                    if (p != null) {p.sendMessage(C.failPrefix + "The limit for this item is " + ChatColor.YELLOW + ChatColor.ITALIC + itemLimit);}
                return false;
            }
        }
        if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.TIPPED_ARROW) {
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                if (limitedEffects.get(type).contains(potionMeta.getBasePotionType())) {
                    if (p != null) {p.sendMessage(C.failPrefix + "This potion is disabled");}
                    return false;
                }
        }
    return true;}

    public static HashMap<Player,Long> editModeSwapCooldown = new HashMap<>();

    public static void editMode (Player p, boolean openEnderchest) {
        if (editModeSwapCooldown.get(p) != null && System.currentTimeMillis()-editModeSwapCooldown.get(p) < 500) {return;}
        editorMode.put(p,openEnderchest);
        editModeSwapCooldown.put(p,System.currentTimeMillis());
        if (openEnderchest) {
            p.playSound(p,Sound.BLOCK_ENDER_CHEST_OPEN,0.5f,1f);
            editorInventory.put(p,p.getInventory().getContents());
            if (editorEnderchest.get(p) != null) {
                ItemStack[] enderChestItems = editorEnderchest.get(p);
                for (int i = 0; i < 27; i++) {
                    p.getInventory().setItem(i+9,enderChestItems[i]);
                }

            } else {
                for (int i = 0; i < 27; i++) {
                    p.getInventory().setItem(i+9,null);
                }

            }
            for (int i = 0; i < 9; i++) {
                p.getInventory().setItem(i,C.createItemStack(" ",Material.GRAY_STAINED_GLASS_PANE,1,new String[]{},null,null,"null",null));
            }
            p.getInventory().setItem(4,C.createItemStack(ChatColor.GOLD+"Inventory",Material.CHEST,1,new String[]{C.t("&6⏵ &7Click to return to inventory editor")},null,null,"return",null));
        } else {
            p.playSound(p,Sound.BLOCK_ENDER_CHEST_CLOSE,0.5f,1f);
            List<ItemStack> enderChestItems = new ArrayList<>();
            for (int i = 0; i < 27; i++) {
                enderChestItems.add(p.getInventory().getItem(i+9));
            }
            editorEnderchest.put(p,enderChestItems.toArray(ItemStack[]::new));
            p.getInventory().setContents(editorInventory.get(p));

        }
    }

    @EventHandler
    public void clickEnderChestEditFooter (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                String localName = e.getCurrentItem().getPersistentDataContainer().get(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING);
                switch (localName) {
                    case "null":
                        e.setCancelled(true);
                        break;
                    case "return":
                        e.setCancelled(true);
                        editMode(p, false);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    @EventHandler
    public void makeNewJoinsInvisible (PlayerJoinEvent e) {
        UtilMethods.teleportLobby(e.getPlayer());
        Player newJoin = e.getPlayer();
        for (Player p : inEditor.keySet()) {
            p.hidePlayer(C.plugin,newJoin);
        }
    }
}
