package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Material;

public enum ItemEnum {
    /**
     * Items
     */
    blocks(C.t("&#9c8e8e&lB&#c4b2b2&ll&#ebd6d6&lo&#ebd6d6&lc&#c4b2b2&lk&#9c8e8e&ls"), Material.COBBLESTONE, 12),
    potions(C.t("&#9c8e8e&lP&#bdacac&lo&#decaca&lt&#ffe8e8&li&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.SPLASH_POTION, 13),
    food(C.t("&#9c8e8e&lF&#decaca&lo&#decaca&lo&#9c8e8e&ld"), Material.GOLDEN_CARROT, 14),
    weapons(C.t("&#9c8e8e&lW&#bdacac&le&#decaca&la&#ffe8e8&lp&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.STONE_AXE, 15),
    utilities(C.t("&#9c8e8e&lU&#b5a5a5&lt&#cebbbb&li&#e6d2d2&ll&#ffe8e8&li&#e6d2d2&lt&#cebbbb&li&#b5a5a5&le&#9c8e8e&ls"), Material.TRIDENT, 16);

    private String displayName;
    private Material material;
    private Integer inventorySlot;


    ItemEnum(String displayName, Material material, Integer inventorySlot) {
        this.displayName = displayName;
        this.material = material;
        this.inventorySlot = inventorySlot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getInventorySlot() {
        return inventorySlot - 1;
    }

}
