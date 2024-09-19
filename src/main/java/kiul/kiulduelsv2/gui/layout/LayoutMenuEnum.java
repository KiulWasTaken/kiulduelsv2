package kiul.kiulduelsv2.gui.layout;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum LayoutMenuEnum {
    /**
     * Queue
     */

    EXPORT("&f&lSAVE / EXIT", Material.WRITABLE_BOOK, new String[]{
            ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Save",
            ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Exit"}, 32,"save",null),
    ITEM_INV("&6&lADD ITEMS", Material.CHEST, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 13,"item",null),
    ENCHANT_ITEM("&4&lENCHANT ITEM", Material.ENCHANTING_TABLE, new String[]{
            ChatColor.GRAY+"Edit the enchantments of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 15,"enchant",null),
    RENAME_ITEM("&7&lRENAME", Material.ANVIL, new String[]{
            ChatColor.GRAY+"Change the name of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Edit"}, 16,"rename",null),
    TRIM_ITEM("&f&lCUSTOMIZE", Material.SMITHING_TABLE, new String[]{
            ChatColor.GRAY+"Edit the trim of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 17,"trim",null),
    SAVE_ITEM("&f&lSAVE/UPLOAD ITEM", Material.NETHER_STAR, new String[]{
            ChatColor.GRAY+"SAVE the item in your main hand",
            ChatColor.GRAY+"to the \"saved items\" tab in the editor.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to SAVE"}, 11,"save_item",null);

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;
    private String skullValue;

    LayoutMenuEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName, String skullValue) {
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
