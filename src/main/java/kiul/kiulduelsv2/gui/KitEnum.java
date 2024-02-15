package kiul.kiulduelsv2.gui;

import org.bukkit.Material;

public enum KitEnum {
    /**
     * Kit
     */
    Slot1("&71", Material.CYAN_TERRACOTTA, new String[]{""}, 4,"SLOT-1"),
    Slot2("&72", Material.CYAN_TERRACOTTA, new String[]{""}, 5,"SLOT-2"),
    Slot3("&73", Material.CYAN_TERRACOTTA, new String[]{""}, 6,"SLOT-3"),
    Slot4("&74", Material.CYAN_TERRACOTTA, new String[]{""}, 13,"SLOT-4"),
    Slot5("&75", Material.CYAN_TERRACOTTA, new String[]{""}, 14,"SLOT-5"),
    Slot6("&76", Material.CYAN_TERRACOTTA, new String[]{""}, 15,"SLOT-6"),
    Slot7("&77", Material.CYAN_TERRACOTTA, new String[]{""}, 22,"SLOT-7"),
    Slot8("&78", Material.CYAN_TERRACOTTA, new String[]{""}, 23,"SLOT-8"),
    Slot9("&79", Material.CYAN_TERRACOTTA, new String[]{""}, 24,"SLOT-9");

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;


    KitEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName) {
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

    public String getlocalName() {
        return localName;
    }

}
