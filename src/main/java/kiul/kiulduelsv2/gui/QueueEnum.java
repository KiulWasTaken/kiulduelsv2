package kiul.kiulduelsv2.gui;

import org.bukkit.Material;
import org.bukkit.Sound;

public enum QueueEnum {
    /**
     * Queue
     */
    arcadeSMP("&#FF5555SMP &#FFFFFF| &#AAAAAAClassic", Material.NETHERITE_AXE, new String[]{""}, 21,"arcadeSMP",null),
    realisticSMP1v1("&#FF5555SMP &#FFFFFF| &#555555Realistic &#AAAAAA{1v1&#AAAAAA}", Material.GRAY_DYE, new String[]{""}, 12,"realSMP1",null),
    realisticSMP2v2("&#FF5555SMP &#FFFFFF| &#555555Realistic &#AAAAAA{2v2&#AAAAAA}", Material.GRAY_DYE, new String[]{""}, 3,"realSMP2",null),
    arcadeCrystal("&#FF55FFCrystal &#FFFFFF| &#AAAAAAClassic", Material.END_CRYSTAL, new String[]{""}, 25,"arcCrystal",null),
    realisticCrystal1v1("&#FF55FFCrystal &#FFFFFF| &#555555Realistic &#AAAAAA{1v1&#AAAAAA}", Material.GRAY_DYE, new String[]{""}, 16,"realCrystal1",null),
    realisticCrystal2v2("&#FF55FFCrystal &#FFFFFF| &#555555Realistic &#AAAAAA{2v2&#AAAAAA}", Material.GRAY_DYE, new String[]{""}, 7,"realCrystal2",null),
    SMPBoxing("&#00AAAANetherite Boxing &#FFFFFF| &#AAAAAAClassic", Material.NETHERITE_SWORD, new String[]{""}, 23,"boxing",null),
    axe("&#00AAAAAxe &#FFFFFF| &#AAAAAAClassic", Material.DIAMOND_AXE, new String[]{""}, 27,"axe",null),
    infoBoard("&#00AAAAAxe &#FFFFFF| &#AAAAAAClassic", Material.OAK_HANGING_SIGN, new String[]{""}, 5,"info",null);

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
