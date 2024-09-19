package kiul.kiulduelsv2.gui.queue;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum QueueEnum {
    /**
     * Queue
     */

    SMP_RATED("&#FBB908&lS&#FBC208&lT&#FACC08&lA&#FAD508&lN&#F9DF08&lD&#F9E808&lA&#F8F208&lR&#F8FB08&lD&r&#F8FB08", Material.TOTEM_OF_UNDYING, new String[]{
            ChatColor.GRAY+"SMP Kit Consistent With Rules On Kiul SMP",
            ChatColor.GRAY+"All forms of non-crystal/anchor PVP are allowed",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"    ❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Slow Falling Effect",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 14,"SMP-RATED",null),
//    SMP_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.SHIELD, new String[]{""}, 5,"SMP-UNRATED",null),
    CRYSTAL_RATED("&#FF40F2&lV&#F84AF6&lA&#F055FB&lN&#E95FFF&lI&#E95FFF&lL&#E95FFF&lL&#E95FFF&lA&r&#E95FFF", Material.END_CRYSTAL, new String[]{
            ChatColor.GRAY+"Vanilla PVP (Crystal Meta) Kit",
            ChatColor.GRAY+"no restrictions on weapons or gear",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 17,"CRYSTAL-RATED",null),
    CART_RATED("&#FF4800&lT&#FF4402&lN&#FF3F05&lT &#FF3709&lM&#FF330B&lI&#FF2E0E&lN&#FF2A10&lE&#FF2612&lC&#FF2214&lA&#FF1D17&lR&#FF1919&lT&#FF1919", Material.TNT_MINECART, new String[]{
            ChatColor.GRAY+"Debuff/Mace-less Cart Practice Kit ",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 16,"CART-RATED",null),
    //    CRYSTAL_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.END_CRYSTAL, new String[]{""}, 5,"CRYSTAL-UNRATED",null),
    ATTRITION_RATED("&#2623FA&lA&#2D32FB&lT&#3441FD&lT&#3B4FFE&lR&#425EFF&lI&#425EFF&lT&#425EFF&lI&#425EFF&lO&#425EFF&lN&#425EFF", Material.SHIELD, new String[]{
            ChatColor.GRAY+"Shield-Based SMP Kit designed for practice mains",
            "",
            ChatColor.GREEN+"✔"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Debuff Effects",
            ChatColor.RED+"    ❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Slow Falling Effect",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Mace",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" TNT Minecarts",
            ChatColor.RED+"❌"+ChatColor.DARK_GRAY+" -" + ChatColor.GRAY+" Crystals & Anchors"}, 15,"SHIELD-RATED",null),
    QUEUE_ALL("&#FFFFFF&lQ&#FFFFFF&lU&#FFFFFF&lE&#FFFFFF&lU&#FFFFFF&lE&#FFFFFF&l-&#FFFFFF&lA&#FFFFFF&lL&#FFFFFF&lL", Material.NETHER_STAR, new String[]{
            ChatColor.GRAY+"Connects you to all ranked queues at once",
            ChatColor.GRAY+"ideal for getting into a match quickly.",}, 12,"ALL",null),
    Career("&6Career", Material.CLOCK, new String[]{ChatColor.GRAY+"displays the outcome and elo",ChatColor.GRAY+"change of all your recent matches","",ChatColor.DARK_GRAY+""+ChatColor.STRIKETHROUGH+"                                                       "}, 11,"career",null);

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
