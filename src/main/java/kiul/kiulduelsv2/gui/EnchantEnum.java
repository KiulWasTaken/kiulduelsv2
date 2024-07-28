package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public enum EnchantEnum {
    /** Items */
    netheritesword(C.t("&6Sword enchantments"), Material.NETHERITE_SWORD, 1, null, null),
    bow(C.t("&6Bow enchantments"), Material.BOW, 10, null, null),
    netheritepickaxe(C.t("&6Pickaxe enchantments"), Material.NETHERITE_PICKAXE, 19, null, null),
    trident(C.t("&6Trident enchantments"), Material.TRIDENT, 28, null, null),
    crossbow(C.t("&6Crossbow enchantments"), Material.CROSSBOW, 37, null, null),
    netheritehelm(C.t("&6Helmet enchantments"), Material.NETHERITE_HELMET, 24, null, null),
    netheriteleggings(C.t("&6Legging enchantments"), Material.NETHERITE_LEGGINGS, 25, null, null),
    netheriteboots(C.t("&6Boot enchantments"), Material.NETHERITE_BOOTS, 26, null, null),
    resetenchants(C.t("&4Remove all enchants"), Material.BARRIER, 41, null, null),
    iteminventory(C.t(ItemInventory.itemInvTitle), Material.KNOWLEDGE_BOOK, 32, null, null),
    /** Enchantment books */
    sharp(C.t(""), Material.ENCHANTED_BOOK, 2, Enchantment.SHARPNESS, 5),
    fireaspect(C.t(""), Material.ENCHANTED_BOOK, 3, Enchantment.FIRE_ASPECT, 2),
    sweepingedge(C.t(""), Material.ENCHANTED_BOOK, 4, Enchantment.SWEEPING_EDGE, 3),
    kb1(C.t(""), Material.ENCHANTED_BOOK, 5, Enchantment.KNOCKBACK, 1),
    kb2(C.t(""), Material.ENCHANTED_BOOK, 6, Enchantment.KNOCKBACK, 2),
    mending(C.t(""), Material.ENCHANTED_BOOK, 8, Enchantment.MENDING, 1),
    unbreaking(C.t(""), Material.ENCHANTED_BOOK, 9, Enchantment.UNBREAKING, 3),
    power(C.t(""), Material.ENCHANTED_BOOK, 11, Enchantment.POWER, 5),
    flame(C.t(""), Material.ENCHANTED_BOOK, 12, Enchantment.FLAME, 1),
    punch(C.t(""), Material.ENCHANTED_BOOK, 13, Enchantment.PUNCH, 2),
    infinity(C.t(""), Material.ENCHANTED_BOOK, 14, Enchantment.INFINITY, 1),
    effieciency(C.t(""), Material.ENCHANTED_BOOK, 20, Enchantment.EFFICIENCY, 5),
    fortune(C.t(""), Material.ENCHANTED_BOOK, 21, Enchantment.FORTUNE, 3),
    protection(C.t(""), Material.ENCHANTED_BOOK, 27, Enchantment.PROTECTION, 4),
    impaling(C.t(""), Material.ENCHANTED_BOOK, 29, Enchantment.IMPALING, 5),
    riptide(C.t(""), Material.ENCHANTED_BOOK, 30, Enchantment.RIPTIDE, 3),
    loyalty(C.t(""), Material.ENCHANTED_BOOK, 31, Enchantment.LOYALTY, 3),
    aquainfinity(C.t(""), Material.ENCHANTED_BOOK, 33, Enchantment.AQUA_AFFINITY, 1),
    swiftsneak(C.t(""), Material.ENCHANTED_BOOK, 34, Enchantment.SWIFT_SNEAK, 3),
    featherfalling(C.t(""), Material.ENCHANTED_BOOK, 35, Enchantment.FEATHER_FALLING, 4),
    blastprotection(C.t(""), Material.ENCHANTED_BOOK, 36, Enchantment.BLAST_PROTECTION, 4),
    piercing(C.t(""), Material.ENCHANTED_BOOK, 38, Enchantment.PIERCING, 4),
    quickcharge(C.t(""), Material.ENCHANTED_BOOK, 39, Enchantment.QUICK_CHARGE, 3),
    respiration(C.t(""), Material.ENCHANTED_BOOK, 42, Enchantment.RESPIRATION, 3),
    depthstrider(C.t(""), Material.ENCHANTED_BOOK, 44, Enchantment.DEPTH_STRIDER, 3);

    private String displayName;
    private Material material;
    private Integer inventorySlot;
    private Enchantment enchantment;
    private Integer enchantLvl;

    EnchantEnum(String displayName, Material material, Integer inventorySlot, Enchantment enchantment, Integer enchantLvl) {
        this.displayName = displayName;
        this.material = material;
        this.inventorySlot = inventorySlot;
        this.enchantment = enchantment;
        this.enchantLvl = enchantLvl;
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

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public Integer getEnchantLvl(){ return enchantLvl; }

}
