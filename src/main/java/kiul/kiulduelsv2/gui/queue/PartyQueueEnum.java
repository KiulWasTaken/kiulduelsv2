package kiul.kiulduelsv2.gui.queue;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum PartyQueueEnum {
    /**
     * Queue
     */

    PARTY_FFA("&#400C60&lPARTY FFA", Material.PURPLE_SHULKER_BOX, new String[]{
            ChatColor.GRAY+"Fight in a Free-For-All against your own party members.",
            ChatColor.GRAY+"All players will spawn in the center of the map & last",
            ChatColor.GRAY+"man standing wins."}, 49,"ffa",null),
    PARTY_SPLIT("&#660066&lPARTY SPLIT", Material.MAGENTA_SHULKER_BOX, new String[]{
            ChatColor.GRAY+"Fight team vs team against your own party members.",
            ChatColor.GRAY+"Players are sorted into teams based on their choice",
            ChatColor.GRAY+"(or randomly if they did not use the item in the lobby)",
            ChatColor.GRAY+"and will spawn on opposing ends of the map with the rest",
            ChatColor.GRAY+"of their team. Last team standing wins."}, 51,"split",null);

//    PARTY_VS_PARTY("&#662A486&lPARTY VS. PARTY", Material.PINK_SHULKER_BOX, new String[]{
//            ChatColor.GRAY+"Fight team vs team against another party.",
//            ChatColor.GRAY+"Players are sorted into teams based on which",
//            ChatColor.GRAY+"party to belong to. This game-mode allows unfair",
//            ChatColor.GRAY+"matches. players will spawn on opposing ends of ",
//            ChatColor.GRAY+"the map with the rest of their team. Last team",
//            ChatColor.GRAY+"standing wins."}, 16,"versus",null),

//    Career("&6Career", Material.CLOCK, new String[]{ChatColor.GRAY+"displays the outcome and elo",ChatColor.GRAY+"change of all your recent matches","",ChatColor.STRIKETHROUGH+""+ChatColor.GRAY+"                                    "}, 12,"career",null);

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;
    private String skullValue;

    PartyQueueEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName, String skullValue) {
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
        this.inventorySlot = inventorySlot;
        this.localName = localName;
        this.skullValue = skullValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getLore() {
        return lore;
    }

    public Integer getInventorySlot() {
        return inventorySlot - 1;
    }

    public String getlocalName() {
        return localName;
    }
    public String getSkullValue() {
        return skullValue;
    }


}
