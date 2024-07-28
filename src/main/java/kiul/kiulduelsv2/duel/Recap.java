package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recap implements Listener {


    public static void open (Player open,Player recapping,boolean post,String kitType) {
        int invSize = 54;
        String name = post ? "Post-Game Inventory | " + kitType.toUpperCase() : "Initial Kit Used | " + kitType.toUpperCase();
        Inventory inventory = Bukkit.createInventory(null,invSize,name);
        String kitContentsBase64 = post ? DuelMethods.inventoryPreview.get(recapping) : (String) Userdata.get().get("kits." + recapping.getUniqueId() + "." + kitType + ".kit-slot-" + KitMethods.kitSlot.get(recapping).get(kitType) + ".inventory");
        try{inventory.setContents(InventoryToBase64.itemStackArrayFromBase64(kitContentsBase64));}catch(IOException err){err.printStackTrace();}
        ArrayList<String> lore = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.GRAY_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }
        lore.add(ChatColor.GRAY + "Hits: " + (int)DuelListeners.duelStatistics.get(recapping.getUniqueId()).get("hits_dealt"));
        lore.add(ChatColor.GRAY + "Hits Taken: " +(int)DuelListeners.duelStatistics.get(recapping.getUniqueId()).get("hits_taken"));
        lore.add(ChatColor.GRAY + "Damage Dealt: " +(int)DuelListeners.duelStatistics.get(recapping.getUniqueId()).get("damage_dealt"));
        lore.add(ChatColor.GRAY + "Longest Combo: " + (int)DuelListeners.duelStatistics.get(recapping.getUniqueId()).get("longest_combo"));
        lore.add("");
        lore.add(ChatColor.GRAY+"Click To View Initial Kit Used");
        inventory.setItem(invSize-5,ItemStackMethods.createSkullItem(recapping.getDisplayName(),recapping,lore,"swap"));



        open.openInventory(inventory);
    }


    @EventHandler
    public void recapClickEvent (InventoryClickEvent e) {
        Player p = (Player) e.getView().getPlayer();
        if (e.getCurrentItem() == null) {return;}
        if (e.getView().getTitle().contains("Post-Game Inventory") || e.getView().getTitle().contains("Initial Kit Used")) {
            e.setCancelled(true);
        if (!e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {return;}
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING) == "swap") {
                    Player viewing = ((SkullMeta) e.getCurrentItem().getItemMeta()).getOwningPlayer().getPlayer();
                    boolean post = e.getView().getTitle().contains("Post-Game Inventory");
                    if (viewing == null) {return;}
                    String[] strings = e.getView().getTitle().split("\\|");
                    String type = strings[1].toLowerCase().trim();
                    open(p,viewing,!post,type);
            }
        }
    }

    public static void openStatsGUI (ArrayList<Player> players, Player p) {
        int invSize = 9+(int)Math.ceil(players.size() / 7.0)*9;
        Inventory inventory = Bukkit.createInventory(null,invSize,"Statistics");
        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }

        ArrayList<String> lore = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            String displayName = ChatColor.WHITE+players.get(i).getDisplayName();
            if ((boolean)DuelListeners.duelStatistics.get(p.getUniqueId()).get("dead")) {
                displayName = ChatColor.GRAY+players.get(i).getDisplayName() + ChatColor.RED + " (DEAD)";
            }

            lore.add(ChatColor.GRAY + "Hits: " + isThisStatTheBest(players.get(i),players,"hits_dealt") + (int)DuelListeners.duelStatistics.get(players.get(i).getUniqueId()).get("hits_dealt"));
            lore.add(ChatColor.GRAY + "Hits Taken: " + isThisStatTheBest(players.get(i),players,"hits_taken") + (int)DuelListeners.duelStatistics.get(players.get(i).getUniqueId()).get("hits_taken"));
            lore.add(ChatColor.GRAY + "Damage Dealt: " + isThisStatTheBest(players.get(i),players,"damage_dealt") + (int)DuelListeners.duelStatistics.get(players.get(i).getUniqueId()).get("damage_dealt"));
            lore.add(ChatColor.GRAY + "Longest Combo: " + isThisStatTheBest(players.get(i),players,"longest_combo") + (int)DuelListeners.duelStatistics.get(players.get(i).getUniqueId()).get("longest_combo"));

            inventory.addItem(ItemStackMethods.createSkullItem(displayName,players.get(i),lore,null));
            lore.clear();
        }



        p.openInventory(inventory);
    }

    public static ChatColor isThisStatTheBest (Player p,ArrayList<Player> competition,String category) {
        int myStat = (int)DuelListeners.duelStatistics.get(p.getUniqueId()).get(category);
        List<Integer> statList = new ArrayList<>();
        for (Player player : competition) {
            int stat = (int)DuelListeners.duelStatistics.get(player.getUniqueId()).get(category);
            statList.add(stat);
        }
        Collections.sort(statList);
        Collections.reverse(statList);
        if (myStat >= statList.get(0)) {
            return ChatColor.GOLD;
        }
        return ChatColor.WHITE;}

    public static void sendMatchRecap (Player p,List<Player> winner,boolean rated) {
        ArrayList<Player> duelMembers = new ArrayList<>();
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {

                if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").equals(DuelListeners.duelStatistics.get(p.getUniqueId()).get("uuid")) ) {
                    duelMembers.add(onlinePlayers);
                }
            }
        }
        p.sendMessage(C.t("&8&m         &r&8|&r &7Match Recap &8|&m        "));
        TextComponent bulletPoint = new TextComponent("▸ ");
        bulletPoint.setColor(net.md_5.bungee.api.ChatColor.of("#50bd4a"));
        for (Player player : duelMembers) {
            String playerElo = "";
            if (rated) {
                int eloChange = (int) DuelListeners.duelStatistics.get(player.getUniqueId()).get("elo");
                playerElo = ChatColor.WHITE + " (" + ChatColor.GREEN + "+" + eloChange + ChatColor.WHITE + ")";
                if (eloChange < 0) {
                    playerElo = ChatColor.WHITE + " (" + ChatColor.RED + eloChange + ChatColor.WHITE + ")";
                }
            }
            if (winner.contains(player)) {

                TextComponent displayName = new TextComponent(player.getDisplayName()+playerElo);
                TextComponent winnerMessage = new TextComponent(ChatColor.GOLD+" (WINNER)");
                displayName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/previewinv " + player.getName() + " " + DuelListeners.duelStatistics.get(player.getUniqueId()).get("type")));
                p.spigot().sendMessage(bulletPoint,displayName,winnerMessage);

            } else {
                TextComponent displayName = new TextComponent(player.getDisplayName()+playerElo);
                displayName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/previewinv " + player.getName()+ " " + DuelListeners.duelStatistics.get(player.getUniqueId()).get("type")));
                p.spigot().sendMessage(bulletPoint,displayName);
            }
        }
        p.sendMessage("");
        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Click names to see more information..");
        p.sendMessage(C.t("&8&m                                    "));

    }

    public static void previewInventorySnapshot(Player user, Player target) {
        int invSize = 45;
        Inventory inventory = Bukkit.createInventory(null,invSize,target.getDisplayName()+"'s Inventory");

        try {
            ItemStack[] targetInventory = InventoryToBase64.itemStackArrayFromBase64(DuelMethods.inventoryPreview.get(target));
            ItemStack[] targetArmour = InventoryToBase64.itemStackArrayFromBase64(DuelMethods.armourPreview.get(target));
            for (ItemStack i : targetInventory) {
                if (i != null) {
                    inventory.addItem(i);
                }
            }
            for (int i = 0; i < 3; i++) {
                if (targetArmour[i] != null) {
                    inventory.setItem(28 + i, targetArmour[i]);
                }
            }
            for (int i = 1; i <= 9; i++) {
                inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
            }

        } catch (IOException err) {err.printStackTrace();}

        user.openInventory(inventory);

    }
}
