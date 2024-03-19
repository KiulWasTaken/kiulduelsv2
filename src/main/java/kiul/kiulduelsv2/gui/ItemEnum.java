package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.awt.*;
import java.util.List;

public enum ItemEnum {
    itemamount(C.t("&6Adjust Item Size"), Material.SPECTRAL_ARROW, null, null, null, null),
    backtomain(C.t("&cBack to main"), Material.RED_STAINED_GLASS_PANE, null, null, null, null),
    clearinventory(C.t("&4Clear inventory"), Material.BARRIER, null, null, null, null),

    itemType(C.t("&6Adjust Item Type"), Material.POTION, null, null, null, null),
    /** Blocks */
    netherrack(C.t(  ""), Material.NETHERRACK,  1, "blocks", null, null),
    cobblestone(C.t( ""), Material.COBBLESTONE, 2, "blocks", null, null),
    obsidian(C.t(    ""), Material.OBSIDIAN,    3, "blocks", null, null),
    oak_plank(C.t(   ""), Material.OAK_PLANKS,  4, "blocks", null, null),
    oak_leaves(C.t(  ""), Material.OAK_LEAVES,  5, "blocks", null, null),
    soul_sand(C.t(   ""), Material.SOUL_SAND,   6, "blocks", null, null),
    soul_soil(C.t(   ""), Material.SOUL_SOIL,   7, "blocks", null, null),
    hay_bale(C.t(    ""), Material.HAY_BLOCK,   8, "blocks", null, null),
    /** Potions */
    shortSwiftness(C.t(         ""), Material.POTION,  1,  "potions", null, new    PotionData(PotionType.SPEED,        false, false)),
    longSwiftness(C.t(         ""), Material.POTION,  2,  "potions", null, new    PotionData(PotionType.SPEED,        true, false)),
    Swiftness2(C.t(         ""), Material.POTION,  3,  "potions", null, new    PotionData(PotionType.SPEED,        false, true)),
    shortFireResistance(C.t(         ""), Material.POTION,  4,  "potions", null, new    PotionData(PotionType.FIRE_RESISTANCE,        false, false)),
    longFireResistance(C.t(         ""), Material.POTION,  5,  "potions", null, new    PotionData(PotionType.FIRE_RESISTANCE,        true, false)),
    shortInvisibility(C.t(         ""), Material.POTION,  6,  "potions", null, new    PotionData(PotionType.INVISIBILITY,        false, false)),
    longInvisibility(C.t(         ""), Material.POTION,  7,  "potions", null, new    PotionData(PotionType.INVISIBILITY,        true, false)),
    shortStrength(C.t(         ""), Material.POTION,  10,  "potions", null, new    PotionData(PotionType.STRENGTH,        false, false)),
    longStrength(C.t(         ""), Material.POTION,  11,  "potions", null, new    PotionData(PotionType.STRENGTH,        true, false)),
    Strength2(C.t(         ""), Material.POTION,  12,  "potions", null, new    PotionData(PotionType.STRENGTH,        false, true)),
    InstantHealth(C.t(         ""), Material.POTION,  13,  "potions", null, new    PotionData(PotionType.INSTANT_HEAL,        false, false)),
    InstantHealth2(C.t(         ""), Material.POTION,  14,  "potions", null, new    PotionData(PotionType.INSTANT_HEAL,        false, true)),
    shortWaterBreathing(C.t(         ""), Material.POTION,  8,  "potions", null, new    PotionData(PotionType.WATER_BREATHING,        false, false)),
    longWaterBreathing(C.t(         ""), Material.POTION,  9,  "potions", null, new    PotionData(PotionType.WATER_BREATHING,        true, false)),
    shortJump(C.t(         ""), Material.POTION,  19,  "potions", null, new    PotionData(PotionType.JUMP,        false, false)),
    longJump(C.t(         ""), Material.POTION,  20,  "potions", null, new    PotionData(PotionType.JUMP,        true, false)),
    Jump2(C.t(         ""), Material.POTION,  21,  "potions", null, new    PotionData(PotionType.JUMP,        false, true)),
    InstantDamage(C.t(         ""), Material.POTION,  15,  "potions", null, new    PotionData(PotionType.INSTANT_DAMAGE,        false, false)),
    InstantDamage2(C.t(         ""), Material.POTION,  16,  "potions", null, new    PotionData(PotionType.INSTANT_DAMAGE,        false, true)),
    shortWeak(C.t(         ""), Material.POTION,  17,  "potions", null, new    PotionData(PotionType.WEAKNESS,        false, false)),
    longWeak(C.t(         ""), Material.POTION,  18,  "potions", null, new    PotionData(PotionType.WEAKNESS,        true, false)),
    shortRegeneration(C.t(         ""), Material.POTION,  28,  "potions", null, new    PotionData(PotionType.REGEN,        false, false)),
    longRegeneration(C.t(         ""), Material.POTION,  29,  "potions", null, new    PotionData(PotionType.REGEN,        true, false)),
    Regeneration2(C.t(         ""), Material.POTION,  30,  "potions", null, new    PotionData(PotionType.REGEN,        false, true)),
    shortPoison(C.t(         ""), Material.POTION,  22,  "potions", null, new    PotionData(PotionType.POISON,        false, false)),
    longPoison(C.t(         ""), Material.POTION,  23,  "potions", null, new    PotionData(PotionType.POISON,        true, false)),
    Poison2(C.t(         ""), Material.POTION,  24,  "potions", null, new    PotionData(PotionType.POISON,        false, true)),
    shortSlowFalling(C.t(         ""), Material.POTION,  34,  "potions", null, new    PotionData(PotionType.SLOW_FALLING,        false, false)),
    longSlowFalling(C.t(         ""), Material.POTION,  35,  "potions", null, new    PotionData(PotionType.SLOW_FALLING,        true, false)),
    shortTurtleMaster(C.t(         ""), Material.POTION,  31,  "potions", null, new    PotionData(PotionType.TURTLE_MASTER,        false, false)),
    longTurtleMaster(C.t(         ""), Material.POTION,  32,  "potions", null, new    PotionData(PotionType.TURTLE_MASTER,        true, false)),
    TurtleMaster2(C.t(         ""), Material.POTION,  33,  "potions", null, new    PotionData(PotionType.TURTLE_MASTER,        false, true)),
    shortSlowness(C.t(         ""), Material.POTION,  25,  "potions", null, new    PotionData(PotionType.SLOWNESS,        false, false)),
    longSlowness(C.t(         ""), Material.POTION,  26,  "potions", null, new    PotionData(PotionType.SLOWNESS,        true, false)),
    Slowness2(C.t(         ""), Material.POTION,  27,  "potions", null, new    PotionData(PotionType.SLOWNESS,        false, true)),
    honeyBottle(C.t(         ""), Material.HONEY_BOTTLE,  36,  "potions", null, null),
    /** Food */
    steak(C.t(                ""), Material.COOKED_BEEF,            1, "food", null, null),
    porkchop(C.t(             ""), Material.COOKED_PORKCHOP,        2, "food", null, null),
    chorusfruit(C.t(          ""), Material.CHORUS_FRUIT,           3, "food", null, null),
    goldenapple(C.t(          ""), Material.GOLDEN_APPLE,           4, "food", null, null),
    enchantedgoldenapple(C.t( ""), Material.ENCHANTED_GOLDEN_APPLE, 5, "food", null, null),
    goldencarrot(C.t(         ""), Material.GOLDEN_CARROT,          6, "food", null, null),
    suspiciousstew(C.t(       ""), Material.SUSPICIOUS_STEW,        7, "food", null, null),
    honeybottle(C.t(          ""), Material.HONEY_BOTTLE,           8, "food", null, null),
    /** Weapons */
    netheritehelm(C.t(       ""), Material.NETHERITE_HELMET,     1, "weapons", null, null),
    netheritechestplate(C.t( ""), Material.NETHERITE_CHESTPLATE, 2, "weapons", null, null),
    netheriteleggings(C.t(   ""), Material.NETHERITE_LEGGINGS,   3, "weapons", null, null),
    netheriteboots(C.t(      ""), Material.NETHERITE_BOOTS,      4, "weapons", null, null),
    netheritesword(C.t(      ""), Material.NETHERITE_SWORD,      5, "weapons", null, null),
    netheritepickaxe(C.t(    ""), Material.NETHERITE_PICKAXE,    6, "weapons", null, null),
    netheriteaxe(C.t(        ""), Material.NETHERITE_AXE,        7, "weapons", null, null),
    netheriteshovel(C.t(     ""), Material.NETHERITE_SHOVEL,     8, "weapons", null, null),
    netheritehoe(C.t(        ""), Material.NETHERITE_HOE,        9, "weapons", null, null),
    diamondhelm(C.t(         ""), Material.DIAMOND_HELMET,       10, "weapons", null, null),
    diamondchestplate(C.t(   ""), Material.DIAMOND_CHESTPLATE,   11, "weapons", null, null),
    diamondleggings(C.t(     ""), Material.DIAMOND_LEGGINGS,     12, "weapons", null, null),
    diamondboots(C.t(        ""), Material.DIAMOND_BOOTS,        13, "weapons", null, null),
    bow(C.t(                 ""), Material.BOW,                  14, "weapons", null, null),
    crossbow(C.t(            ""), Material.CROSSBOW,             15, "weapons", null, null),
    shield(C.t(              ""), Material.SHIELD,               16, "weapons", null, null),
    totem(C.t(               ""), Material.TOTEM_OF_UNDYING,     17, "weapons", null, null),
    enderpearl(C.t(          ""), Material.ENDER_PEARL,          18, "weapons", null, null),
    /** Utilities */
    crystal(C.t(          ""), Material.END_CRYSTAL,        1, "utilities", null, null),
    obsidian1(C.t(         ""), Material.OBSIDIAN,           2, "utilities", null, null),
    anchor(C.t(           ""), Material.RESPAWN_ANCHOR,     3, "utilities", null, null),
    glowstone(C.t(        ""), Material.GLOWSTONE,          4, "utilities", null, null),
    tntminecart(C.t(      ""), Material.TNT_MINECART,       5, "utilities", null, null),
    rail(C.t(             ""), Material.RAIL,               6, "utilities", null, null),
    trident(C.t(          ""), Material.TRIDENT,            7, "utilities", null, null),
    cobweb(C.t(           ""), Material.COBWEB,             8, "utilities", null, null),
    exp(C.t(              ""), Material.EXPERIENCE_BOTTLE,  9, "utilities", null, null),
    waterbucket(C.t(      ""), Material.WATER_BUCKET,       10, "utilities", null, null),
    pufferfishbucket(C.t( ""), Material.PUFFERFISH_BUCKET,  11, "utilities", null, null),
    lavabucket(C.t(       ""), Material.LAVA_BUCKET,        12, "utilities", null, null),
    milkbucket(C.t(       ""), Material.MILK_BUCKET,        13, "utilities", null, null),
    snowbucket(C.t(       ""), Material.POWDER_SNOW_BUCKET, 14, "utilities", null, null),
    bucket(C.t(           ""), Material.BUCKET,             15, "utilities", null, null),
    shears(C.t(           ""), Material.SHEARS,             16, "utilities", null, null),
    oakdoor(C.t(          ""), Material.OAK_DOOR,           17, "utilities", null, null),
    tnt(C.t(              ""), Material.TNT,                18, "utilities", null, null),
    flintnsteel(C.t(      ""), Material.FLINT_AND_STEEL,    19, "utilities", null, null),
    fishingrod(C.t(       ""), Material.FISHING_ROD,        20, "utilities", null, null),
    enderpearl1(C.t(       ""), Material.ENDER_PEARL,        21, "utilities", null, null),
    /** Item main */
    blocks(C.t("&#9c8e8e&lB&#c4b2b2&ll&#ebd6d6&lo&#ebd6d6&lc&#c4b2b2&lk&#9c8e8e&ls"), Material.COBBLESTONE, 12, "blocks", 18, null),
    potions(C.t("&#9c8e8e&lP&#bdacac&lo&#decaca&lt&#ffe8e8&li&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.SPLASH_POTION, 13, "potions", 45, null),
    food(C.t("&#9c8e8e&lF&#decaca&lo&#decaca&lo&#9c8e8e&ld"), Material.GOLDEN_CARROT, 14, "food", 18, null),
    weapons(C.t("&#9c8e8e&lW&#bdacac&le&#decaca&la&#ffe8e8&lp&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.STONE_AXE, 15, "weapons", 27, null),
    utilities(C.t("&#9c8e8e&lU&#b5a5a5&lt&#cebbbb&li&#e6d2d2&ll&#ffe8e8&li&#e6d2d2&lt&#cebbbb&li&#b5a5a5&le&#9c8e8e&ls"), Material.TRIDENT, 16, "utilities", 36, null),
    enchantmenu(C.t(EnchantInventory.itemEnchantInvTitle), Material.ENCHANTED_BOOK, 23, null, 9, null);

    private String displayName;
    private Material material;
    private Integer inventorySlot;
    private String inventory;
    private Integer inventorySize;
    private PotionData potionData;

    ItemEnum(String displayName, Material material, Integer inventorySlot, String inventory, Integer inventorySize, PotionData potionData) {
        this.displayName = displayName;
        this.material = material;
        this.inventorySlot = inventorySlot;
        this.inventory = inventory;
        this.inventorySize = inventorySize;
        this.potionData = potionData;
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

    public String getInventory() {
        return inventory;
    }

    public Integer getInventorySize() {
        return inventorySize;
    }

    public PotionData getPotionData() {
        return potionData;
    }

}
