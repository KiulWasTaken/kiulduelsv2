package kiul.kiulduelsv2.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

public enum QueueEnum {
    /**
     * Queue
     */

    SMP_RATED("&#B34E4E&lS&#A74343&lM&#9B3838&lP", Material.SHIELD, new String[]{""}, 14,"SMP-RATED",null),
//    SMP_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.SHIELD, new String[]{""}, 5,"SMP-UNRATED",null),
//    CRYSTAL_RATED("&#744EB3&lC&#744AAF&lR&#7547AB&lY&#7543A7&lS&#753FA3&lT&#763C9F&lA&#76389B&lL", Material.END_CRYSTAL, new String[]{""}, 5,"CRYSTAL-RATED",null),
    //    CRYSTAL_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.END_CRYSTAL, new String[]{""}, 5,"CRYSTAL-UNRATED",null),
    Career("&6Career", Material.CLOCK, new String[]{ChatColor.GRAY+"displays the outcome of",ChatColor.GRAY+" all your recent matches",""}, 12,"career",null);

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
