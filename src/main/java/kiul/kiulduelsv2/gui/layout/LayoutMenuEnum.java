package kiul.kiulduelsv2.gui.layout;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum LayoutMenuEnum {
    /**
     * Queue
     */

    EXPORT("&f&lSAVE/EXIT KIT", Material.WRITABLE_BOOK, new String[]{
            ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Save Kit to Selected Slot",
            ChatColor.GRAY + "Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Exit Without Saving"}, 5,"save",null),
    ITEM_INV("&6&lADD ITEMS", Material.CHEST, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 1,"item",null),
    ENCHANT_ITEM("&4&lENCHANT ITEM", Material.ENCHANTING_TABLE, new String[]{
            ChatColor.GRAY+"Edit the enchantments of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 2,"enchant",null),
    RENAME_ITEM("&7&lRENAME", Material.ANVIL, new String[]{
            ChatColor.GRAY+"Change the name of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Edit"}, 3,"rename",null),
    TRIM_ITEM("&f&lCUSTOMIZE", Material.SMITHING_TABLE, new String[]{
            ChatColor.GRAY+"Edit the trim of the item",
            ChatColor.GRAY+"in your main hand.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to Expand"}, 4,"trim",null),
    SAVE_ITEM("&f&lSAVE ITEM", Material.BOOK, new String[]{
            ChatColor.GRAY+"SAVE the selected item to the",
            ChatColor.GRAY+"\"saved items\" tab in the editor.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to SAVE"}, null,"save_item",null),
    ERASE_ITEM("&c&lERASE ITEM", Material.ENCHANTED_BOOK, new String[]{
            ChatColor.GRAY+"Remove the selected item from the ",
            ChatColor.GRAY+"\"saved items\" tab in the editor.",
            "",
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to ERASE"}, null,"erase_item",null);

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
