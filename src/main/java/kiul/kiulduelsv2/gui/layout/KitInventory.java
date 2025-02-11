package kiul.kiulduelsv2.gui.layout;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.gui.KitEditor;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class KitInventory implements Listener {


    public static void selectKitToEdit(Player p) {
        Inventory inventory = Bukkit.createInventory(p, InventoryType.HOPPER,"Kit Selector");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
        }

        inventory.setItem(1,C.createItemStack(KitEnum.SMP.getDisplayName(),KitEnum.SMP.getMaterial(),1,KitEnum.SMP.getLore(),null,null,KitEnum.SMP.getlocalName(),null));
        inventory.setItem(2,C.createItemStack(KitEnum.SHIELD.getDisplayName(),KitEnum.SHIELD.getMaterial(),1,KitEnum.SHIELD.getLore(),null,null,KitEnum.SHIELD.getlocalName(),null));
        //inventory.setItem(2,C.createItemStack(KitEnum.CART.getDisplayName(),KitEnum.CART.getMaterial(),1,KitEnum.CART.getLore(),null,null,KitEnum.CART.getlocalName(),null));
        inventory.setItem(3,C.createItemStack(KitEnum.CRYSTAL.getDisplayName(),KitEnum.CRYSTAL.getMaterial(),1,KitEnum.CRYSTAL.getLore(),null,null,KitEnum.CRYSTAL.getlocalName(),null));
        p.openInventory(inventory);
    }

    public static void selectSlotToEdit(Player p,String type) {
        Inventory inventory = Bukkit.createInventory(p, InventoryType.HOPPER,"Select Slot | " + type);
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
        }
        List<String> lore = new ArrayList<>();

        if (p.hasPermission("kiulduels.extraslots")) {
            lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Select");
            for (int i = 0; i < 3; i++) {
                inventory.setItem(i, ItemStackMethods.createItemStack(C.t("&2"+(i+1)),Material.GREEN_TERRACOTTA,1,lore,null,null,"SLOT-"+(i+1)+"-"+type));
            }
        } else {
            lore.add(C.t("&6[GOLD]&7 or higher is required to unlock this kit slot"));
            for (int i = 0; i < 3; i++) {
                inventory.setItem(i, ItemStackMethods.createItemStack(C.t("&7"+(i+1)),Material.CYAN_TERRACOTTA,1,lore,null,null,"null"));
            }
        }
        //Slot3Cart("&#8d3a2d3", "&#a65151",null, Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 32,"SLOT-3-CART"),
        inventory.setItem(4,C.createItemStack(KitEnum.EXPORT.getDisplayName(),KitEnum.EXPORT.getMaterial(),1,KitEnum.EXPORT.getLore(),null,null,KitEnum.EXPORT.getlocalName(),null));


        int selectedSlot = KitMethods.kitSlot.get(p).get(type);
        lore.clear();
        lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Edit");
        lore.add(ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase");
        inventory.setItem(selectedSlot-1,ItemStackMethods.createItemStack(C.t("&a" + selectedSlot),Material.LIME_TERRACOTTA,1,lore,null,null,"SLOT-"+selectedSlot+"-"+type));
        p.openInventory(inventory);
    }

    /*public static void kitInventory(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 54, "Kit Selector");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
        }


        for (KitEnum item : KitEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                String itemName = ItemStackMethods.translateHexColorCodes("&#","",item.getDisplayName());

                if (item.getMaterial().name().contains("TERRACOTTA")) {
                    lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Select");
                    inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                    List<String> types = Queue.queueTypesLowercase();
                    for (String type : types) {
                        String[] strings = item.getlocalName().split("-");
                        String matchStr = strings[2].toLowerCase();
                        int matchInt = Integer.parseInt(strings[1]);
                        if (type.equalsIgnoreCase(matchStr) && matchInt == KitMethods.kitSlot.get(p).get(type)) {
                            lore.clear();
                            lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Edit");
                            lore.add(ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase");
                            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(C.t(item.getActiveHex()+KitMethods.kitSlot.get(p).get(type).toString()), item.getActiveMaterial(), 1, lore, null, null, item.getlocalName()));
                        }
                    }


                } else {


                    if (item.getRequiredPermission() == null) {
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                    } else {
                     if (p.hasPermission(item.getRequiredPermission())) {
                         inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                     } else {
                         inventory.setItem(item.getInventorySlot(),ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
                     }
                    }
                }
        }

        p.openInventory(inventory);

    }*/


    @EventHandler
    public void onGUIClick (InventoryClickEvent e) {
        Player p = (Player)e.getView().getPlayer();
        if (e.getView().getTitle().contains("Select Slot | ")) {
            if (e.getCurrentItem() == null) {return;}
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getItemName());
                int num = Integer.parseInt(name);
                String[] strings = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING).split("-");
                String type = strings[strings.length-1].toLowerCase();
                if (e.getClick() != ClickType.RIGHT) {
                    boolean doReturn = true;
                    for (String queueTypes : Queue.queueTypesLowercase()) {
                        if (type.equalsIgnoreCase(queueTypes)) {
                            doReturn = false;
                            break;
                        }
                    }
                    if (doReturn) {return;}
                    KitMethods.kitSlot.get(p).put(type,num);
                    p.playSound(p,Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,1f,1f);
                    KitInventory.selectSlotToEdit(p,type);
                }
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"))) {
            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getItemName());
            String lastColours = ChatColor.getLastColors(e.getCurrentItem().getItemMeta().getDisplayName());
            String[] strings = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING).split("-");
            String type = strings[strings.length-1].toLowerCase();

            if (e.getClick() == ClickType.LEFT) {
                if (e.getCurrentItem().getItemMeta().getLore().get(0).trim().equals(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Edit")) {
                    KitEditor.enterKitEditor(p, type);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1.5f);
                } else if (e.getCurrentItem().getItemMeta().getLore().get(0).trim().contains("Cancel Erasure")) {
                    List<String> lore = e.getCurrentItem().getLore();
                    lore.clear();
                    lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Edit");
                    lore.add(ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase");
                    e.getCurrentItem().setLore(lore);
                    e.getCurrentItem().getItemMeta().setItemName(lastColours+name);
                }
            } else if (e.getClick() == ClickType.RIGHT) {
                if (e.getCurrentItem().getItemMeta().getLore().get(1).trim().equals(ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase")) {
                    e.getCurrentItem().setType(Material.BARRIER);
                    List<String> lore = e.getCurrentItem().getLore();
                    lore.clear();
                    e.getCurrentItem().getItemMeta().setItemName(lastColours+"Are You Sure You Want To Erase This Slot?");
                    lore.add(C.t("&7Left Click " + C.DARK_GREEN + "⏵" + C.GREEN + " Cancel Erasure"));
                    lore.add(C.t("&7Right Click " + C.DARK_RED + "⏵" + C.RED + " Confirm Erasure"));
                    e.getCurrentItem().setLore(lore);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1.8f);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 1.8f);
                        }
                    }.runTaskLater(C.plugin, 5);
                } else if (e.getCurrentItem().getItemMeta().getLore().get(1).trim().contains("Confirm Erasure"))  {
                    List<String> lore = e.getCurrentItem().getLore();
                    lore.clear();
                    lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵ " + ChatColor.WHITE + " Edit");
                    lore.add(ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵ " + ChatColor.WHITE + " Erase");
                    e.getCurrentItem().getItemMeta().setItemName(lastColours+name);
                    CustomKitData.get().set(p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type) + ".inventory",null);
                    CustomKitData.get().set(p.getUniqueId() + "." + type + ".kit-slot-" + KitMethods.kitSlot.get(p).get(type) + ".armour",null);
                    CustomKitData.save();
                    p.playSound(p, Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 1f, 1f);
                    selectSlotToEdit(p,type);
                }
            }
        }

        } else if (e.getView().getTitle().equalsIgnoreCase("Kit Selector")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {return;}
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                String localName =  e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING);
                boolean doReturn = true;
                for (String queueTypes : Queue.queueTypesLowercase()) {
                    if (localName.equalsIgnoreCase(queueTypes)) {
                        doReturn = false;
                        break;
                    }
                }
                if (doReturn) {return;}
                p.playSound(p,Sound.ENTITY_VILLAGER_WORK_LIBRARIAN,0.5f,1.2f);
                selectSlotToEdit(p,localName);
            }
        }
    }
}
