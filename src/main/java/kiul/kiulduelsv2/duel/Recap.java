package kiul.kiulduelsv2.duel;

import it.unimi.dsi.fastutil.Hash;
import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Recap implements Listener {


    public static void open (Player open,Player recapping,boolean post,boolean enderchest,boolean stats,Duel duel) {
        int invSize = enderchest ? 36 : 45;
        String name = enderchest ? post ? recapping.getName()+ "'s Post-Game Enderchest | " + duel.getKitType().toUpperCase() : recapping.getName()+ "'s Initial Enderchest Used | " + duel.getKitType().toUpperCase() : post ? recapping.getName()+ "'s Post-Game Inventory | " + duel.getKitType().toUpperCase() : recapping.getName()+ "'s Initial Kit Used | " + duel.getKitType().toUpperCase();
        Inventory inventory = Bukkit.createInventory(null,invSize,name);

        String kitContentsBase64
                = enderchest ? post ?
                duel.getEnderchestPreview().get(recapping)
                : (String) CustomKitData.get().get(recapping.getUniqueId() + "." + duel.getKitType() + ".kit-slot-" + KitMethods.kitSlot.get(recapping).get(duel.getKitType()) + ".enderchest")
                    : post ? duel.getInventoryPreview().get(recapping)
                : (String) CustomKitData.get().get(recapping.getUniqueId() + "." + duel.getKitType() + ".kit-slot-" + KitMethods.kitSlot.get(recapping).get(duel.getKitType()) + ".inventory");


        try{inventory.setContents(InventoryToBase64.itemStackArrayFromBase64(kitContentsBase64));}catch(IOException err){err.printStackTrace();}
        ArrayList<String> lore = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.GRAY_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }
        String displayName = ChatColor.WHITE+recapping.getName();
        if (duel.getIsDead().get(recapping.getUniqueId().toString())) {
            displayName = ChatColor.GRAY+recapping.getName() + ChatColor.RED + " (DEAD)";
        }
        if (stats) {
            lore.addAll(barChart("DDΔ vs. You",recapping.getUniqueId().toString(),open.getUniqueId().toString(),duel.getDamageDealtToPlayer(),duel.getDamageTakenFromPlayer()));
            lore.addAll(barChart("DuDΔ vs. You",recapping.getUniqueId().toString(),open.getUniqueId().toString(),duel.getDurabilityDamageDealtToPlayer(),duel.getDurabilityDamageTakenFromPlayer()));
            lore.addAll(barChart("Damage Dealt vs. You",recapping.getUniqueId().toString(),open.getUniqueId().toString(),duel.getDamageTypeDealtToPlayer(),"|"));
            lore.addAll(barChart("Damage Taken vs. You",recapping.getUniqueId().toString(),open.getUniqueId().toString(),duel.getDamageTypeTakenFromPlayer(),"|"));
        } else {
            lore.addAll(barChart("DDΔ vs. All",recapping.getUniqueId().toString(),duel.getDamageDealt(),duel.getDamageTaken()));
            lore.addAll(barChart("DuDDΔ vs. All",recapping.getUniqueId().toString(),duel.getDurabilityDamageDealt(),duel.getDurabilityDamageTaken()));
            lore.addAll(barChart("Damage Dealt vs. All",recapping.getUniqueId().toString(),duel.getDamageTypeDealt(),"|"));
            lore.addAll(barChart("Damage Taken vs. All",recapping.getUniqueId().toString(),duel.getDamageTypeTaken(),"|"));
        }


        //⏵
        inventory.setItem(invSize-5,ItemStackMethods.createSkullItem(displayName,recapping,lore,"stats-"+stats));

        lore.clear();
        lore.add(post ? ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to view pre-game inventory" : ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to view post-game inventory");
        inventory.setItem(invSize-4,C.createItemStack(post ? C.t("Pre-Game Inventory") : C.t("Post-Game Inventory"),post ? Material.CLOCK : Material.GOLD_INGOT,1,lore.toArray(String[]::new),null,null,"post",null));
        lore.clear();
        lore.add(enderchest ? C.t("&#258273⏵ &7Click to view enderchest inventory") : ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to view player inventory");
        inventory.setItem(invSize-6,C.createItemStack(enderchest ? C.t("&#258273Enderchest") : C.t("&6Inventory"),enderchest ? Material.CHEST : Material.ENDER_CHEST,1,lore.toArray(String[]::new),null,null,"ec",null));


        open.openInventory(inventory);
    }


    @EventHandler
    public void recapClickEvent (InventoryClickEvent e) {
        Player p = (Player) e.getView().getPlayer();
        if (e.getCurrentItem() == null) {return;}
        if (e.getView().getTitle().contains("Post-Game") || e.getView().getTitle().contains("Initial")) {
            e.setCancelled(true);
        if (!e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {return;}
            String localName = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"),PersistentDataType.STRING);
            String[] splitPlayer = e.getView().getTitle().split("'");
            Player viewing = Bukkit.getPlayer(splitPlayer[0]);
            boolean post = e.getView().getTitle().contains("Post-Game Inventory");
            boolean enderchest = e.getView().getTitle().contains("Enderchest");
            String[] split = localName.split("-");
            boolean stats = split[split.length-1].equals("true");
            if (viewing == null) {return;}
            switch (localName) {
                case "ec":
                    open(p,viewing,post,!enderchest,stats,DuelMethods.lastDuel.get(p.getUniqueId()));
                    break;
                case "stats":
                    open(p,viewing,post,enderchest,!stats,DuelMethods.lastDuel.get(p.getUniqueId()));
                    break;
                case "post":
                    open(p,viewing,!post,enderchest,stats,DuelMethods.lastDuel.get(p.getUniqueId()));
                    break;
            }
        }
    }

    public static void openStatsGUI (Player p, Duel duel) {
        List<UUID> UUIDs = duel.getPlayers();
        int invSize = 9+(int)Math.ceil(UUIDs.size() / 7.0)*9;
        Inventory inventory = Bukkit.createInventory(null,invSize,"Post-Game Statistics");
        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }

        // add item to bottom middle of inv that shows "general" stats like pearls used, exp used, etc.

        ArrayList<String> lore = new ArrayList<>();
        for (int i = 0; i < UUIDs.size(); i++) {
            UUID uuid = UUIDs.get(i);
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            String displayName = ChatColor.WHITE+player.getName();
            if (duel.getIsDead().get(uuid.toString())) {
                displayName = ChatColor.GRAY+player.getName() + ChatColor.RED + " (DEAD)";
            }
            lore.addAll(barChart("DDΔ vs. All",player.getUniqueId().toString(),duel.getDamageDealt(),duel.getDamageTaken()));
            lore.addAll(barChart("DuDDΔ vs. All",player.getUniqueId().toString(),duel.getDurabilityDamageDealt(),duel.getDurabilityDamageTaken()));
            lore.addAll(barChart("Damage Dealt vs. All",player.getUniqueId().toString(),duel.getDamageTypeDealt(),"|"));
            lore.addAll(barChart("Damage Taken vs. All",player.getUniqueId().toString(),duel.getDamageTypeTaken(),"|"));

            inventory.addItem(ItemStackMethods.createSkullItem(displayName,Bukkit.getOfflinePlayer(uuid),lore,"preview-"+player.getName()));
            lore.clear();
        }



        p.openInventory(inventory);
    }

    public static List<String> barChart (String title,String hashMapKey, HashMap<String,Double> positiveStat, HashMap<String,Double> negativeStat) {
        double positiveValue = positiveStat.get(hashMapKey);
        double negativeValue = negativeStat.get(hashMapKey);
        double max = negativeValue+positiveValue;
        boolean positive = positiveValue-negativeValue > 0;
        String ratioString = positive ? C.GREEN + C.decimalFormat.format(max/positiveValue) : C.RED + C.decimalFormat.format(max/negativeValue);
        String positiveString = C.decimalFormat.format(positiveStat.get(hashMapKey));
        String negativeString = C.decimalFormat.format(negativeStat.get(hashMapKey));

        String barChart;
        String positiveChar = C.GREEN+"|";
        String negativeChar = C.RED+"|";
        int numChars = 20;
        barChart = positiveChar.repeat(1+(int)(max/positiveValue)*numChars) + negativeChar.repeat(1+(int)(max/negativeValue)*numChars);
        String header = title + " [" + C.GREEN + positiveString + ChatColor.WHITE + " / " + C.RED + negativeString + ChatColor.WHITE+"]";

        int ratioOffset = (int)(numChars*1.5)-ratioString.length();
        String charOffset = " ";
        List<String> lore = new ArrayList<>();
        lore.add(header);
        lore.add(barChart);
        lore.add(charOffset.repeat(ratioOffset)+ratioString);
        return lore;
    }

    public static List<String> barChart (String title,String hashMapKey,String targetKey, HashMap<String,HashMap<String,Double>> positiveStat, HashMap<String,HashMap<String,Double>> negativeStat) {
        double positiveValue = positiveStat.get(hashMapKey).get(targetKey);
        double negativeValue = negativeStat.get(hashMapKey).get(targetKey);
        double max = negativeValue+positiveValue;
        boolean positive = positiveValue-negativeValue > 0;
        String ratioString = positive ? C.GREEN + C.decimalFormat.format(max/positiveValue) : C.RED + C.decimalFormat.format(max/negativeValue);
        String positiveString = C.decimalFormat.format(positiveStat.get(hashMapKey));
        String negativeString = C.decimalFormat.format(negativeStat.get(hashMapKey));

        String barChart;
        String positiveChar = C.GREEN+"|";
        String negativeChar = C.RED+"|";
        int numChars = 20;
        barChart = positiveChar.repeat(1+(int)(max/positiveValue)*numChars) + negativeChar.repeat(1+(int)(max/negativeValue)*numChars);
        String header = title + " [" + C.GREEN + positiveString + ChatColor.WHITE + " / " + C.RED + negativeString + ChatColor.WHITE+"]";

        int ratioOffset = (int)(numChars*1.5)-ratioString.length();
        String charOffset = " ";
        List<String> lore = new ArrayList<>();
        lore.add(header);
        lore.add(barChart);
        lore.add(charOffset.repeat(ratioOffset)+ratioString);
        return lore;
    }

    static HashMap<String,String> legendIcons = new HashMap<>() {{
        put("mace","m");
        put("melee","m");
        put("ranged","m");
        put("explosive","m");
        put("unknown","?");
    }};

    static List<String> barColours = new ArrayList<>() {{
        add(net.md_5.bungee.api.ChatColor.of(new Color(0,255,127)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(0,191,255)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(25,25,112)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(255,69,0)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(170,170,170)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(85,47,107)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(255,20,147)) + "");
        add(net.md_5.bungee.api.ChatColor.of(new Color(255,215,0)) + "");
    }};

    public static List<String> barChart (String title,String hashMapKey, HashMap<String,HashMap<String,Double>> categoricalHashMap,String bar) {
        double max = 0.0;
        List<String> hashMapCategories = new ArrayList<>(categoricalHashMap.get(hashMapKey).keySet());
        HashMap<String,String> barColour = new HashMap<>();

        for (int i = 0; i <  hashMapCategories.size(); i++) {
            String category = hashMapCategories.get(i);
            HashMap<String,Double> nestedMap = categoricalHashMap.get(hashMapKey);
            max+=nestedMap.get(category);
            String uniqueBar = barColours.get(i)+bar; // seeded colours for up to 8 categories
            barColour.put(category,uniqueBar);
        }
        String header = title + " [";
        String legend = "";
        String barChart = "";
        int numChars = 20;

        for (int i = 0; i <  hashMapCategories.size(); i++) {
            String category = hashMapCategories.get(i);
            HashMap<String,Double> nestedMap = categoricalHashMap.get(hashMapKey);
            double value = nestedMap.get(category);
            if (value <= 0) {continue;}
            boolean isNext = i < hashMapCategories.size()-1;
            legend += category + " ";
            header += isNext ? barColours.get(i)+C.decimalFormat.format(value)+ChatColor.WHITE+"/" : barColours.get(i)+C.decimalFormat.format(value)+ChatColor.WHITE+"]";
            barChart += barColour.get(category).repeat(1+(int)(max/value)*numChars);
        }

        List<String> lore = new ArrayList<>();
        lore.add(header);
        lore.add(barChart);
        lore.add(legend);
        return lore;
    }

    public static List<String> barChart (String title,String hashMapKey,String targetKey, HashMap<String,HashMap<String,HashMap<String,Double>>> categoricalHashMap,String bar) {
        double max = 0.0;
        List<String> hashMapCategories = new ArrayList<>(categoricalHashMap.get(hashMapKey).get(targetKey).keySet());
        HashMap<String,String> barColour = new HashMap<>();

        for (int i = 0; i <  hashMapCategories.size(); i++) {
            String category = hashMapCategories.get(i);
            HashMap<String,Double> nestedMap = categoricalHashMap.get(hashMapKey).get(targetKey);
            max+=nestedMap.get(category);
            String uniqueBar = barColours.get(i)+bar; // seeded colours for up to 8 categories
            barColour.put(category,uniqueBar);
        }
        String header = title + " [";
        String legend = "";
        String barChart = "";
        int numChars = 20;

        for (int i = 0; i <  hashMapCategories.size(); i++) {
            String category = hashMapCategories.get(i);
            HashMap<String,Double> nestedMap = categoricalHashMap.get(hashMapKey).get(targetKey);
            double value = nestedMap.get(category);
            if (value <= 0) {continue;}
            boolean isNext = i < hashMapCategories.size()-1;
            legend += category + legendIcons.get(category)+" "+barColours.get(i)+bar+"  ";
            header += isNext ? barColours.get(i)+C.decimalFormat.format(value)+ChatColor.WHITE+"/" : barColours.get(i)+C.decimalFormat.format(value)+ChatColor.WHITE+"]";
            barChart += barColour.get(category).repeat(1+(int)(max/value)*numChars);
        }

        List<String> lore = new ArrayList<>();
        lore.add(header);
        lore.add(barChart);
        lore.add(legend);
        return lore;
    }

    public static void sendMatchRecap (Player p,List<UUID> winner,Duel duel) {
        List<UUID> duelMembers = duel.getPlayers();

        p.sendMessage(C.t("&8&m         &r &7Match Recap &8&m        "));
        TextComponent bulletPoint = new TextComponent("▸ ");
        bulletPoint.setColor(net.md_5.bungee.api.ChatColor.of("#50bd4a"));

        for (UUID uuid : duelMembers) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            String playerElo = "";
            if (duel.isRated()) {
                int eloChange = duel.getEloChange().get(uuid);
                playerElo = ChatColor.WHITE + " (" + ChatColor.GREEN + "+" + eloChange + ChatColor.WHITE + ")";
                if (eloChange < 0) {
                    playerElo = ChatColor.WHITE + " (" + ChatColor.RED + eloChange + ChatColor.WHITE + ")";
                }
            }
            if (winner.contains(uuid)) {

                TextComponent displayName = new TextComponent(offlinePlayer.getName()+playerElo);
                TextComponent winnerMessage = new TextComponent(ChatColor.GOLD+" (WINNER)");
                displayName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/previewinv " + offlinePlayer.getName() + " " + duel.getKitType()));
                p.spigot().sendMessage(bulletPoint,displayName,winnerMessage);

            } else {
                TextComponent displayName = new TextComponent(offlinePlayer.getName()+playerElo);
                displayName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/previewinv " + offlinePlayer.getName()+ " " + duel.getKitType()));
                p.spigot().sendMessage(bulletPoint,displayName);
            }
        }
        p.sendMessage("");
        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Click names to see more information..");
        p.sendMessage(C.t("&8&m                                    "));

    }
}
