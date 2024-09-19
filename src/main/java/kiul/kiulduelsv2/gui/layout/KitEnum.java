package kiul.kiulduelsv2.gui.layout;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum KitEnum {
    /**
     * Kit
     */
    EXPORT("&e&lEXPORT",null,null, Material.WRITABLE_BOOK,null, new String[]{
            ChatColor.GRAY+"Export your selected kit slot to the Kit Library,",
            ChatColor.GRAY+"where other players can view and test your layout.",
            "",
            ChatColor.RED+""+ChatColor.BOLD+"COMING SOON"}, 18,"export"),
    IMPORT("&e&lIMPORT",null,null, Material.BOOK,null, new String[]{
            ChatColor.GRAY+"Import a kit from the Kit Library into your selected",
            ChatColor.GRAY+"kit slot. Will erase all data in your current slot.",
            "",
            ChatColor.RED+""+ChatColor.BOLD+"COMING SOON"}, 27,"import"),
    SMP("&#FBB908&lS&#FBC208&lT&#FACC08&lA&#FAD508&lN&#F9DF08&lD&#F9E808&lA&#F8F208&lR&#F8FB08&lD",null,null, Material.TOTEM_OF_UNDYING,null, new String[]{
            ChatColor.GRAY+"SMP Kit Consistent With Rules On Kiul SMP",
            ChatColor.GRAY+"All forms of non-crystal/anchor PVP are allowed",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"    ❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Slow Falling Effect",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 10,"smp"),
    SHIELD("&#2623FA&lA&#2D32FB&lT&#3441FD&lT&#3B4FFE&lR&#425EFF&lI&#425EFF&lT&#425EFF&lI&#425EFF&lO&#425EFF&lN&#425EFF",null,null, Material.SHIELD,null, new String[]{
            ChatColor.GRAY+"Shield-Based SMP Kit designed for practice mains",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"    ❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Slow Falling Effect",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 19,"shield"),
    CRYSTAL("&#FF40F2&lV&#F84AF6&lA&#F055FB&lN&#E95FFF&lI&#E95FFF&lL&#E95FFF&lL&#E95FFF&lA",null,null, Material.END_CRYSTAL,null, new String[]{
            ChatColor.GRAY+"Vanilla PVP (Crystal Meta) Kit",
            ChatColor.GRAY+"no restrictions on weapons or gear",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 37,"crystal"),
    CART("&#FF4800&lT&#FF4402&lN&#FF3F05&lT &#FF3709&lM&#FF330B&lI&#FF2E0E&lN&#FF2A10&lE&#FF2612&lC&#FF2214&lA&#FF1D17&lR&#FF1919&lT",null,null, Material.TNT_MINECART,null, new String[]{
            ChatColor.GRAY+"Debuff/Mace-less Cart Practice Kit ",
            ChatColor.GRAY+"no restrictions on weapons or gear.",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 28,"cart"),
//    ERASE("&f&lERASE",null,null, Material.FEATHER,null, new String[]{
//            ChatColor.GRAY+"Erase all data from your selected kit slot. This is",
//            ChatColor.GRAY+"unrecoverable, are you sure you want to do this?"}, 36,"erase"),

    Slot1SMP("&#cb68431","&#9d5123",null, Material.TERRACOTTA,Material.ORANGE_TERRACOTTA, new String[]{}, 12,"SLOT-1-SMP"),
    Slot2SMP("&#cb68432","&#9d5123",null, Material.TERRACOTTA,Material.ORANGE_TERRACOTTA, new String[]{}, 13,"SLOT-2-SMP"),
    Slot3SMP("&#cb68433","&#9d5123",null, Material.TERRACOTTA,Material.ORANGE_TERRACOTTA, new String[]{}, 14,"SLOT-3-SMP"),
    Slot4SMP("&#cb68434","&#9d5123","kiulduels.extraslots", Material.TERRACOTTA,Material.ORANGE_TERRACOTTA, new String[]{}, 15,"SLOT-4-SMP"),
    Slot5SMP("&#cb68435","&#9d5123","kiulduels.extraslots", Material.TERRACOTTA,Material.ORANGE_TERRACOTTA, new String[]{}, 16,"SLOT-5-SMP"),
    Slot1Shield("&#565c5c1","&#766f8c",null, Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA, new String[]{}, 21,"SLOT-1-SHIELD"),
    Slot2Shield("&#565c5c2","&#766f8c",null, Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA, new String[]{}, 22,"SLOT-2-SHIELD"),
    Slot3Shield("&#565c5c3","&#766f8c",null, Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA, new String[]{}, 23,"SLOT-3-SHIELD"),
    Slot4Shield("&#565c5c4","&#766f8c","kiulduels.extraslots", Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA, new String[]{}, 24,"SLOT-4-SHIELD"),
    Slot5Shield("&#565c5c5","&#766f8c","kiulduels.extraslots", Material.CYAN_TERRACOTTA,Material.LIGHT_BLUE_TERRACOTTA, new String[]{}, 25,"SLOT-5-SHIELD"),
    Slot1Cart("&#8d3a2d1","&#a65151",null, Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 30,"SLOT-1-CART"),
    Slot2Cart("&#8d3a2d2","&#a65151",null, Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 31,"SLOT-2-CART"),
    Slot3Cart("&#8d3a2d3", "&#a65151",null, Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 32,"SLOT-3-CART"),
    Slot4Cart("&#8d3a2d4","&#a65151","kiulduels.extraslots", Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 33,"SLOT-4-CART"),
    Slot5Cart("&#8d3a2d5", "&#a65151","kiulduels.extraslots", Material.RED_TERRACOTTA,Material.PINK_TERRACOTTA, new String[]{}, 34,"SLOT-5-CART"),
    Slot1Crystal("&#7b4a591","&#94566c",null, Material.PURPLE_TERRACOTTA,Material.MAGENTA_TERRACOTTA, new String[]{}, 39,"SLOT-1-CRYSTAL"),
    Slot2Crystal("&#7b4a592","&#94566c",null, Material.PURPLE_TERRACOTTA,Material.MAGENTA_TERRACOTTA, new String[]{}, 40,"SLOT-2-CRYSTAL"),
    Slot3Crystal("&#7b4a593", "&#94566c",null, Material.PURPLE_TERRACOTTA,Material.MAGENTA_TERRACOTTA, new String[]{}, 41,"SLOT-3-CRYSTAL"),
    Slot4Crystal("&#7b4a594","&#94566c","kiulduels.extraslots", Material.PURPLE_TERRACOTTA,Material.MAGENTA_TERRACOTTA, new String[]{}, 42,"SLOT-4-CRYSTAL"),
    Slot5Crystal("&#7b4a595", "&#94566c","kiulduels.extraslots", Material.PURPLE_TERRACOTTA,Material.MAGENTA_TERRACOTTA, new String[]{}, 43,"SLOT-5-CRYSTAL");



    private String displayName;
    private String activeHex;
    private String requiredPermission;
    private Material material;
    private Material activeMaterial;
    private String[] lore;
    private Integer inventorySlot;
    private String localName;


    KitEnum(String displayName,String activeHex,String requiredPermission, Material material,Material activeMaterial, String[] lore, Integer inventorySlot, String localName) {
        this.displayName = displayName;
        this.activeHex = activeHex;
        this.requiredPermission = requiredPermission;
        this.material = material;
        this.activeMaterial = activeMaterial;
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

    public String getActiveHex() {
        return activeHex;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public Material getActiveMaterial() {
        return activeMaterial;
    }

    public Integer getInventorySlot() {
        return inventorySlot - 1;
    }

    public String getlocalName() {
        return localName;
    }

}
