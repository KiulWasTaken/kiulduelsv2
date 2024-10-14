package kiul.kiulduelsv2.gui.layout;

import kiul.kiulduelsv2.C;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

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
    blueIce(C.t(    ""), Material.BLUE_ICE,   9, "blocks", null, null),
    /** Potions */
    shortSwiftness(C.t(         ""), Material.POTION,  1,  "potions", null, PotionType.SWIFTNESS),
    longSwiftness(C.t(         ""), Material.POTION,  2,  "potions", null, PotionType.LONG_SWIFTNESS),
    Swiftness2(C.t(         ""), Material.POTION,  3,  "potions", null, PotionType.STRONG_SWIFTNESS),
    shortFireResistance(C.t(         ""), Material.POTION,  4,  "potions", null, PotionType.FIRE_RESISTANCE),
    longFireResistance(C.t(         ""), Material.POTION,  5,  "potions", null, PotionType.LONG_FIRE_RESISTANCE),
    shortInvisibility(C.t(         ""), Material.POTION,  6,  "potions", null, PotionType.INVISIBILITY),
    longInvisibility(C.t(         ""), Material.POTION,  7,  "potions", null, PotionType.LONG_INVISIBILITY),
    shortStrength(C.t(         ""), Material.POTION,  10,  "potions", null, PotionType.STRENGTH),
    longStrength(C.t(         ""), Material.POTION,  11,  "potions", null,PotionType.LONG_STRENGTH),
    Strength2(C.t(         ""), Material.POTION,  12,  "potions", null, PotionType.STRONG_STRENGTH),
    InstantHealth(C.t(         ""), Material.POTION,  13,  "potions", null,PotionType.HEALING),
    InstantHealth2(C.t(         ""), Material.POTION,  14,  "potions", null,PotionType.STRONG_HEALING),
    shortWaterBreathing(C.t(         ""), Material.POTION,  8,  "potions", null, PotionType.WATER_BREATHING),
    longWaterBreathing(C.t(         ""), Material.POTION,  9,  "potions", null, PotionType.LONG_WATER_BREATHING),
    shortJump(C.t(         ""), Material.POTION,  19,  "potions", null, PotionType.LEAPING),
    longJump(C.t(         ""), Material.POTION,  20,  "potions", null,PotionType.LONG_LEAPING),
    Jump2(C.t(         ""), Material.POTION,  21,  "potions", null, PotionType.STRONG_LEAPING),
    InstantDamage(C.t(         ""), Material.POTION,  15,  "potions", null, PotionType.HARMING),
    InstantDamage2(C.t(         ""), Material.POTION,  16,  "potions", null, PotionType.STRONG_HARMING),
    shortWeak(C.t(         ""), Material.POTION,  17,  "potions", null, PotionType.WEAKNESS),
    longWeak(C.t(         ""), Material.POTION,  18,  "potions", null, PotionType.LONG_WEAKNESS),
    shortRegeneration(C.t(         ""), Material.POTION,  28,  "potions", null,PotionType.REGENERATION),
    longRegeneration(C.t(         ""), Material.POTION,  29,  "potions", null, PotionType.LONG_REGENERATION),
    Regeneration2(C.t(         ""), Material.POTION,  30,  "potions", null, PotionType.STRONG_REGENERATION),
    shortPoison(C.t(         ""), Material.POTION,  22,  "potions", null, PotionType.POISON),
    longPoison(C.t(         ""), Material.POTION,  23,  "potions", null, PotionType.LONG_POISON),
    Poison2(C.t(         ""), Material.POTION,  24,  "potions", null, PotionType.STRONG_POISON),
    shortSlowFalling(C.t(         ""), Material.POTION,  34,  "potions", null, PotionType.SLOW_FALLING),
    longSlowFalling(C.t(         ""), Material.POTION,  35,  "potions", null,PotionType.LONG_SLOW_FALLING),
    shortTurtleMaster(C.t(         ""), Material.POTION,  31,  "potions", null,PotionType.TURTLE_MASTER),
    longTurtleMaster(C.t(         ""), Material.POTION,  32,  "potions", null,PotionType.LONG_TURTLE_MASTER),
    TurtleMaster2(C.t(         ""), Material.POTION,  33,  "potions", null,PotionType.STRONG_TURTLE_MASTER),
    shortSlowness(C.t(         ""), Material.POTION,  25,  "potions", null,PotionType.SLOWNESS),
    longSlowness(C.t(         ""), Material.POTION,  26,  "potions", null, PotionType.LONG_SLOWNESS),
    Slowness2(C.t(         ""), Material.POTION,  27,  "potions", null, PotionType.STRONG_SLOWNESS),
    honeyBottle(C.t(         ""), Material.HONEY_BOTTLE,  36,  "potions", null, null),
    infested(C.t(         ""), Material.POTION,  37,  "potions", null, PotionType.INFESTED),
    weaving(C.t(         ""), Material.POTION,  38,  "potions", null, PotionType.WEAVING),
    windCharged(C.t(         ""), Material.POTION,  39,  "potions", null, PotionType.WIND_CHARGED),
    oozing(C.t(         ""), Material.POTION,  40,  "potions", null, PotionType.OOZING),

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
    arrow(C.t(          ""), Material.ARROW,          19, "weapons", null, null),
    mac(C.t(""), Material.MACE, 20, "weapons", null, null),
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
    craftingtable(C.t(""), Material.CRAFTING_TABLE, 22, "utilities", null, null),
    ironblock(C.t(""), Material.IRON_BLOCK, 23, "utilities", null, null),
    windcharge(C.t(""), Material.WIND_CHARGE, 24, "utilities", null, null),
    breezerod(C.t(""), Material.BREEZE_ROD, 25, "utilities", null, null),
    armorstand(C.t(""), Material.ARMOR_STAND, 26, "utilities", null, null),
    enderchest(C.t(""), Material.ENDER_CHEST, 27, "utilities", null, null),
    /** Item main */
    blocks(C.t("&#9c8e8e&lB&#c4b2b2&ll&#ebd6d6&lo&#ebd6d6&lc&#c4b2b2&lk&#9c8e8e&ls"), Material.COBBLESTONE, 12, "blocks", 18, null),
    potions(C.t("&#9c8e8e&lP&#bdacac&lo&#decaca&lt&#ffe8e8&li&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.SPLASH_POTION, 13, "potions", 54, null),
    food(C.t("&#9c8e8e&lF&#decaca&lo&#decaca&lo&#9c8e8e&ld"), Material.GOLDEN_CARROT, 14, "food", 18, null),
    saved_items(C.t("&fsaved items"), Material.NETHER_STAR, 23, "saved", 18, null),
    weapons(C.t("&#9c8e8e&lW&#bdacac&le&#decaca&la&#ffe8e8&lp&#decaca&lo&#bdacac&ln&#9c8e8e&ls"), Material.STONE_AXE, 15, "weapons", 36, null),
    utilities(C.t("&#9c8e8e&lU&#b5a5a5&lt&#cebbbb&li&#e6d2d2&ll&#ffe8e8&li&#e6d2d2&lt&#cebbbb&li&#b5a5a5&le&#9c8e8e&ls"), Material.TRIDENT, 16, "utilities", 36, null);
//    ownInventory(C.t("&#3C4766&lI&#3F4F67&ln&#425769&lv&#455E6A&le&#48666B&ln&#455E6A&lt&#425769&lo&#3F4F67&lr&#3C4766&ly"), Material.ENDER_CHEST, 23, null, 9, null);

    private String displayName;
    private Material material;
    private Integer inventorySlot;
    private String inventory;
    private Integer inventorySize;
    private PotionType potionType;

    ItemEnum(String displayName, Material material, Integer inventorySlot, String inventory, Integer inventorySize, PotionType potionType) {
        this.displayName = displayName;
        this.material = material;
        this.inventorySlot = inventorySlot;
        this.inventory = inventory;
        this.inventorySize = inventorySize;
        this.potionType = potionType;
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

    public PotionType getPotionType() {
        return potionType;
    }

}
