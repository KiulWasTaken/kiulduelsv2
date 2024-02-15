package kiul.kiulduelsv2.gui;

import org.bukkit.Material;
import org.bukkit.Sound;

public enum QueueEnum {
    /**
     * Queue
     */
    SMP1v1("&aQueue", Material.LIME_DYE, new String[]{""}, 5,"SMP",null),
    infoBoard("&fLeaderboard", Material.PAPER, new String[]{""}, 9,"info",null);

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;
    private String skullValue;


    QueueEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName,String skullValue) {
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
