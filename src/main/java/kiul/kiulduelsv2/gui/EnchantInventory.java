package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static kiul.kiulduelsv2.gui.clickevents.ClickMethods.inEditor;

public class EnchantInventory implements Listener {


    HashMap<Player,ItemStack> currentItem = new HashMap<>();
    final static HashMap<Enchantment,Integer> ENCHANTMENT_PRIORITY = new HashMap<>() {{
        put(Enchantment.SHARPNESS,1);
        put(Enchantment.FIRE_ASPECT,2);
        put(Enchantment.KNOCKBACK,3);
        put(Enchantment.UNBREAKING,4);
        put(Enchantment.MENDING,5);
        put(Enchantment.SWEEPING_EDGE,6);
        put(Enchantment.LOOTING,7);
        put(Enchantment.BANE_OF_ARTHROPODS,8);
        put(Enchantment.SMITE,9);
        put(Enchantment.VANISHING_CURSE,10);
        put(Enchantment.BINDING_CURSE,10);

        put(Enchantment.EFFICIENCY,1);
        put(Enchantment.SILK_TOUCH,2);
        put(Enchantment.FORTUNE,3);

        put(Enchantment.PROTECTION,1);
        put(Enchantment.FEATHER_FALLING,6);
        put(Enchantment.SWIFT_SNEAK,6);
        put(Enchantment.AQUA_AFFINITY,6);
        put(Enchantment.RESPIRATION,6);
        put(Enchantment.DEPTH_STRIDER,6);
        put(Enchantment.FROST_WALKER,6);
        put(Enchantment.THORNS,7);
        put(Enchantment.FIRE_PROTECTION,8);
        put(Enchantment.BLAST_PROTECTION,8);
        put(Enchantment.PROJECTILE_PROTECTION,8);
        put(Enchantment.SOUL_SPEED,6);

        put(Enchantment.POWER,1);
        put(Enchantment.PUNCH,2);
        put(Enchantment.FLAME,3);
        put(Enchantment.INFINITY,3);

        put(Enchantment.PIERCING,1);
        put(Enchantment.MULTISHOT,2);
        put(Enchantment.QUICK_CHARGE,3);

        put(Enchantment.IMPALING,1);
        put(Enchantment.RIPTIDE,2);
        put(Enchantment.CHANNELING,3);
        put(Enchantment.LOYALTY,3);
    }};

    final static HashMap<Enchantment, Material> ENCHANTMENT_ICON = new HashMap<>() {{
       put(Enchantment.PROTECTION,Material.IRON_INGOT);
       put(Enchantment.FEATHER_FALLING,Material.FEATHER);
       put(Enchantment.PROJECTILE_PROTECTION,Material.ARROW);
       put(Enchantment.BLAST_PROTECTION,Material.TNT_MINECART);
       put(Enchantment.FIRE_PROTECTION,Material.FIRE_CHARGE);

       put(Enchantment.SOUL_SPEED,Material.SOUL_SAND);
       put(Enchantment.DEPTH_STRIDER,Material.WATER_BUCKET);
       put(Enchantment.FROST_WALKER,Material.BLUE_ICE);
       put(Enchantment.SWIFT_SNEAK,Material.SCULK);
       put(Enchantment.THORNS,Material.CACTUS);
       put(Enchantment.AQUA_AFFINITY,Material.SPONGE);
       put(Enchantment.RESPIRATION,Material.TURTLE_HELMET);

       put(Enchantment.UNBREAKING,Material.ANVIL);
       put(Enchantment.MENDING,Material.EXPERIENCE_BOTTLE);

       put(Enchantment.SHARPNESS,Material.NETHERITE_SWORD);
       put(Enchantment.SMITE,Material.ROTTEN_FLESH);
       put(Enchantment.BANE_OF_ARTHROPODS,Material.SPIDER_EYE);
       put(Enchantment.LOOTING,Material.POTATO);
       put(Enchantment.SWEEPING_EDGE,Material.EMERALD);
       put(Enchantment.FIRE_ASPECT,Material.FIRE_CHARGE);
       put(Enchantment.BINDING_CURSE,Material.LEATHER_CHESTPLATE);

       put(Enchantment.POWER,Material.TIPPED_ARROW);
       put(Enchantment.FLAME,Material.FIRE_CHARGE);
       put(Enchantment.PUNCH,Material.SLIME_BALL);
       put(Enchantment.INFINITY,Material.ARROW);

       put(Enchantment.PIERCING,Material.SHIELD);
       put(Enchantment.MULTISHOT,Material.TIPPED_ARROW);
       put(Enchantment.QUICK_CHARGE,Material.SUGAR);

       put(Enchantment.EFFICIENCY,Material.NETHERITE_PICKAXE);
       put(Enchantment.SILK_TOUCH,Material.GRASS_BLOCK);
       put(Enchantment.FORTUNE,Material.DEEPSLATE_GOLD_ORE);

        put(Enchantment.IMPALING,Material.PRISMARINE_CRYSTALS);
        put(Enchantment.RIPTIDE,Material.ELYTRA);
        put(Enchantment.CHANNELING,Material.LIGHTNING_ROD);
        put(Enchantment.LOYALTY,Material.TRIDENT);
    }};
    public List<Enchantment> getApplicableEnchantments(ItemStack item) {
        List<Enchantment> applicableEnchantments = new ArrayList<>();

        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(item)) {
                applicableEnchantments.add(enchantment);
            }
        }

        return applicableEnchantments;
    }

    public static String itemEnchantInvTitle = C.t("&#681dc8&lE&#7719cf&ln&#8615d6&lc&#9511dc&lh&#a40ee3&la&#b30aea&ln&#c206f1&lt &#d102f8&lI&#d102f8&ln&#c206f1&lv&#b30aea&le&#a40ee3&ln&#9511dc&lt&#8615d6&lo&#7719cf&lr&#681dc8&ly");

    public void open(Player p, ItemStack itemStack) {
        currentItem.put(p, itemStack);
        List<Enchantment> applicableEnchantments = getApplicableEnchantments(itemStack);

        if (!applicableEnchantments.isEmpty()) {
            // Sort enchantments by their priority
            applicableEnchantments.sort(Comparator.comparingInt(enchantment -> ENCHANTMENT_PRIORITY.getOrDefault(enchantment, Integer.MAX_VALUE)));

            int invSize = 54;
            Inventory inventory = Bukkit.createInventory(p, invSize, itemEnchantInvTitle);

            // Add the bottom row of black stained glass panes
            for (int i = 1; i <= 9; i++) {
                inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(""), null, null, null));
            }

            int row = 0;
            int column = 0;

            for (Enchantment enchantment : applicableEnchantments) {
                if ((enchantment == Enchantment.THORNS || enchantment == Enchantment.PROJECTILE_PROTECTION) && currentItem.get(p).getType().name().contains("BOOTS")) {
                    continue;
                }
                int levels = enchantment.getMaxLevel() - enchantment.getStartLevel() + 1;
                // Check if the current column can fit the enchantment levels
                if (row + levels > 5) {
                    row = 0;
                    column++;
                }

                for (int i = enchantment.getStartLevel(); i <= enchantment.getMaxLevel(); i++) {
                    List<String> lore = new ArrayList<>();
                    lore.add(C.t("&6⏵ &7Click to enchant your item with " + enchantment.getKey()).replaceFirst("minecraft:", ""));
                    String displayName = C.t(enchantment.getKey().toString() + " " + i).toLowerCase().replaceFirst("minecraft:", "").replaceAll("_", " ");
                    displayName = "&r&l" + displayName.substring(0, 1).toUpperCase() + displayName.substring(1);

                    int slot = row * 9 + column;
                    if (slot < invSize - 9) { // Ensure we don't place items in the bottom row
                        inventory.setItem(slot, ItemStackMethods.createItemStack(displayName, Material.ENCHANTED_BOOK, 1, lore, enchantment, i, null));
                    }

                    row++;
                    if (row >= 5) {
                        row = 0;
                        column++;
                    }
                }

                // Get the separator material for the current enchantment
                Material separatorMaterial = Material.GRAY_STAINED_GLASS_PANE;

                // Add a separator after each enchantment type, if not at the top of a column
                if (row != 0 || column == 0) {
                    int separatorSlot = row * 9 + column;
                    inventory.setItem(separatorSlot, ItemStackMethods.createItemStack(" ", separatorMaterial, 1, List.of(""), null, null, null));


                    row++;
                    if (row >= 5) {
                        row = 0;
                        column++;
                    }
                }
            }
            inventory.setItem(invSize-5,C.createItemStack(C.t(HeadEnum.CLEAR_ENCHANTS.getDisplayName()),HeadEnum.CLEAR_ENCHANTS.getMaterial(),1,HeadEnum.CLEAR_ENCHANTS.getLore(),null,null,HeadEnum.CLEAR_ENCHANTS.getLocalName(), HeadEnum.CLEAR_ENCHANTS.getURL()));
            inventory.setItem(invSize-4,C.createItemStack(C.t(HeadEnum.CUSTOMIZE_ITEM.getDisplayName()),HeadEnum.CUSTOMIZE_ITEM.getMaterial(),1,HeadEnum.CUSTOMIZE_ITEM.getLore(),null,null,HeadEnum.CUSTOMIZE_ITEM.getLocalName(), HeadEnum.CUSTOMIZE_ITEM.getURL()));
            p.openInventory(inventory);
        }
    }

    public static void itemEnchantInventory(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 45, itemEnchantInvTitle);

        //for (int i = 0; i < 45; i++) {
        //    inventory.setItem(i, ItemStackMethods.createItemStack(" ", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, List.of(""), null, null));
        //}

        for (EnchantEnum item : EnchantEnum.values()) {
            List<String> lore = new ArrayList<>();
            if (item.getEnchantment() != null) {
                lore.add(C.t("&6⏵ &7Click to enchant your item with " + item.getEnchantment().getKey()).replaceFirst("minecraft:", ""));
            }
            String displayName = item.getDisplayName();
            if (item.getEnchantment() != null) {
                displayName = C.t(item.getEnchantment().getKey().toString() + " " + item.getEnchantLvl()).toLowerCase().replaceFirst("minecraft:", "").replaceAll("_", " ");
                displayName = "&r&l" + displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
            }
            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(displayName, item.getMaterial(), 1, lore, item.getEnchantment(), item.getEnchantLvl(),null));
        }

        p.openInventory(inventory);

    }


    @EventHandler
    public void enchantInventoryClickEvents (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(EnchantInventory.itemEnchantInvTitle)) {
            if (e.getClickedInventory() != null && e.getClickedInventory() == p.getOpenInventory().getTopInventory() || e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) {return;}
                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                    e.getCursor().setAmount(0);
                    return;
                }
                if (e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(EnchantEnum.iteminventory.getDisplayName()))) {
                    ItemInventory.itemInventory(p);
                    return;
                }
                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR && e.getCurrentItem().getType() != Material.AIR) {
                    e.getCursor().setAmount(0);
                    return;
                }
                if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"))) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING).equals(HeadEnum.CLEAR_ENCHANTS.getLocalName())) {
                        if (currentItem.get(p).getType() != Material.AIR) {
                            if (!currentItem.get(p).getEnchantments().isEmpty()) {
                                for (Map.Entry<Enchantment, Integer> a : p.getInventory().getItemInMainHand().getEnchantments().entrySet()) {
                                    currentItem.get(p).removeEnchantment(a.getKey());
                                }
                                p.playSound(p, Sound.BLOCK_GRINDSTONE_USE, 0.8f, 1.0f);
                            } else {
                                p.sendMessage(C.t("&7[&4⏩&7] &cItem already has no enchantments"));
                            }
                        } else {
                            p.sendMessage(C.t("&7[&4⏩&7] &cHold an item to enchant"));
                        }
                    }
                }
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
                    if (p.getInventory().getItemInMainHand() != null && currentItem.get(p).getType() != Material.AIR) {
                        for (Map.Entry<Enchantment, Integer> entry : e.getCurrentItem().getEnchantments().entrySet()) {
                            if (entry.getKey().canEnchantItem(currentItem.get(p))) {
                                for (Map.Entry<Enchantment, Integer> entry1 : currentItem.get(p).getEnchantments().entrySet()) {
                                    if (entry.getKey().conflictsWith(entry1.getKey())) {
                                        p.sendMessage(C.t("&7[&4⏩&7] &cItem has a conflicting enchantment"));
                                        return;
                                    }
                                }
                                currentItem.get(p).addEnchantment(entry.getKey(), entry.getValue());
                                p.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE,0.3f,1.2f);
                            } else {
                                p.sendMessage(C.t("&7[&4⏩&7] &cYou cannot add this enchant to this item"));
                            }
                            break;
                        }
                    } else {
                        p.sendMessage(C.t("&7[&4⏩&7] &cHold an item to enchant"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void clickPlayerInventory (InventoryClickEvent e) {
        Inventory clickedInv = e.getClickedInventory();
        Player p =(Player) e.getWhoClicked();
        Inventory playerInv = (e.getWhoClicked()).getInventory();
        if (playerInv.equals(clickedInv) && inEditor.containsKey(p)) {
            // do stuff
            switch (e.getClick()) {
                case SHIFT_LEFT:
                    e.setCancelled(true);
                    ItemInventory.itemInventory(p);
                    break;
                case SHIFT_RIGHT:
                    e.setCancelled(true);
                    open(p,e.getCurrentItem());
                    break;
                case DROP:
                    e.setCancelled(true);
                    e.getCurrentItem().setAmount(e.getCurrentItem().getAmount()-1);
                    break;
                case CONTROL_DROP:
                    e.setCancelled(true);
                    e.getCurrentItem().setAmount(0);
                    break;
            }
        }
    }
}
