package kiul.kiulduelsv2.gui.layout;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum CosmeticEnum {
    /**
     * Queue
     */

    MATERIAL_REDSTONE("&#d33b2b&lREDSTONE", Material.REDSTONE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 12,"redstone",0),
    MATERIAL_COPPER("&#df9330&lCOPPER", Material.COPPER_INGOT, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 13,"copper",0),
    MATERIAL_GOLD("&#f0da1e&lGOLD", Material.GOLD_INGOT, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 14,"gold",0),
    MATERIAL_EMERALD("&#2cd50c&lEMERALD", Material.EMERALD, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 15,"emerald",0),
    MATERIAL_DIAMOND("&b&lDIAMOND", Material.DIAMOND, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 16,"diamond",0),
    MATERIAL_LAPIS("&9&lLAPIS LAZULI", Material.LAPIS_LAZULI, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 21,"lapis",0),
    MATERIAL_AMYTHEST("&d&lAMYTHEST", Material.AMETHYST_SHARD, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 22,"amythest",0),
    MATERIAL_QUARTZ("&f&lQUARTZ", Material.QUARTZ, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 23,"quartz",0),
    MATERIAL_IRON("&7&lIRON", Material.IRON_INGOT, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 24,"iron",0),
    MATERIAL_NETHERITE("&8&lNETHERITE", Material.NETHERITE_INGOT, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim material"}, 25,"netherite",0),

    TRIM_HOST("&#a86727&lHOST", Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
            }, 12,"host",1),
    TRIM_RAISER("&#a86727&lRAISER", Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 13,"raiser",1),
    TRIM_SHAPER("&#a86727&lSHAPER", Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 14,"shaper",1),
    TRIM_WAYFINDER("&#a86727&lWAYFINDER", Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 15,"wayfinder",1),
    TRIM_SILENCE("&#1a6f4b&lSILENCE", Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 16,"silence",1),
    TRIM_COAST("&7&lCOAST", Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 21,"coast",1),
    TRIM_VEX("&7&lVEX", Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 22,"vex",1),
    TRIM_WARD("&7&lWARD", Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 23,"ward",1),
    TRIM_WILD("&7&lWILD", Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 24,"host",1),
    TRIM_SENTRY("&7&lSENTRY", Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 25,"sentry",1),
    TRIM_TIDE("&b&lTIDE", Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 30,"tide",1),
    TRIM_DUNE("&e&lDUNE", Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 31,"dune",1),
    TRIM_EYE("&e&lEYE", Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 32,"eye",1),
    TRIM_SNOUT("&c&lSNOUT", Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 33,"snout",1),
    TRIM_RIB("&c&lRIB", Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 34,"rib",1),
    TRIM_SPIRE("&d&lSPIRE", Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 39,"spire",1),
    TRIM_FLOW("&3&lFLOW", Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 40,"flow",1),
    TRIM_BOLT("&#f0911e&lBOLT", Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, new String[]{
            ChatColor.GOLD+"⏵ "+ChatColor.GRAY+"Click to select trim template",
    }, 41,"bolt",1);

//    PARTY_VS_PARTY("&#662A486&lPARTY VS. PARTY", Material.PINK_SHULKER_BOX, new String[]{
//            ChatColor.GRAY+"Fight team vs team against another party.",
//            ChatColor.GRAY+"Players are sorted into teams based on which",
//            ChatColor.GRAY+"party to belong to. This game-mode allows unfair",
//            ChatColor.GRAY+"matches. players will spawn on opposing ends of ",
//            ChatColor.GRAY+"the map with the rest of their team. Last team",
//            ChatColor.GRAY+"standing wins."}, 16,"versus",null),

//    Career("&6Career", Material.CLOCK, new String[]{ChatColor.GRAY+"displays the outcome and elo",ChatColor.GRAY+"change of all your recent matches","",ChatColor.STRIKETHROUGH+""+ChatColor.GRAY+"                                    "}, 12,"career",null);

    private String displayName;
    private Material material;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;
    private int page;

    CosmeticEnum(String displayName, Material material, String[] lore, Integer inventorySlot, String localName, int page) {
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
        this.inventorySlot = inventorySlot;
        this.localName = localName;
        this.page = page;
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

    public int getPage() {
        return page;
    }
}
