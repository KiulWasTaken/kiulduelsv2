package kiul.kiulduelsv2.gui.settings;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SettingsEnum {
    /**
     * Queue
     */

    DUEL_REQUESTS("&aDuel Requests", Material.REDSTONE, new String[]{
            ChatColor.GRAY+"Click to toggle whether other players can",
            ChatColor.GRAY+"send you duel requests"},1,"duel-requests"),
    PARTY_INVITES("&dParty Invites", Material.REDSTONE, new String[]{
            ChatColor.GRAY+"Click to toggle whether other players can",
            ChatColor.GRAY+"send you party invites"},2,"party-invites");

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;

    SettingsEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName) {
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
        this.inventorySlot = inventorySlot;
        this.localName = localName;
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


    public String getLocalName() {
        return localName;
    }
}
