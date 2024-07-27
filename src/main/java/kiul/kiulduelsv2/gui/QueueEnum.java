package kiul.kiulduelsv2.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

public enum QueueEnum {
    /**
     * Queue
     */

    SMP_RATED("&#FBB908&lS&#FBC208&lT&#FACC08&lA&#FAD508&lN&#F9DF08&lD&#F9E808&lA&#F8F208&lR&#F8FB08&lD&r&#F8FB08", Material.TOTEM_OF_UNDYING, new String[]{
            ChatColor.GRAY+"SMP Kit consistent with rules on Kiul SMP.",
            ChatColor.GRAY+"All forms of non-crystal PVP are allowed",
            ChatColor.GRAY+"includes mace & cart."}, 15,"SMP-RATED",null),
//    SMP_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.SHIELD, new String[]{""}, 5,"SMP-UNRATED",null),
    CRYSTAL_RATED("&#FF40F2&lV&#F84AF6&lA&#F055FB&lN&#E95FFF&lI&#E95FFF&lL&#E95FFF&lL&#E95FFF&lA&r&#E95FFF", Material.END_CRYSTAL, new String[]{
            ChatColor.GRAY+"Vanilla PVP (crystal meta) kit",
            ChatColor.GRAY+"no restrictions on weapons or gear."}, 17,"CRYSTAL-RATED",null),
    //    CRYSTAL_UNRATED("&#65B34E&lS&#5EA743&lM&#569B38&lP", Material.END_CRYSTAL, new String[]{""}, 5,"CRYSTAL-UNRATED",null),
    ONESHOTLESS_RATED("&#2623FA&lO&#2C2FFB&lN&#313BFC&lE&#3746FD&lS&#3C52FE&lH&#425EFF&lO&#425EFF&lT&#425EFF&lL&#425EFF&lE&#425EFF&lS&#425EFF&lS&r&#425EFF", Material.SHIELD, new String[]{
            ChatColor.GRAY+"SMP Kit designed for practice mains",
            ChatColor.GRAY+"restricted use of SMP meta weapons",
            ChatColor.GRAY+"mace & cart disabled."}, 16,"SHIELD-RATED",null),
    QUEUE_ALL("&#FFFFFF&lQ&#FFFFFF&lU&#FFFFFF&lE&#FFFFFF&lU&#FFFFFF&lE&#FFFFFF&l-&#FFFFFF&lA&#FFFFFF&lL&#FFFFFF&lL", Material.NETHER_STAR, new String[]{
            ChatColor.GRAY+"Connects you to all queues at once",
            ChatColor.GRAY+"ideal for getting into a match quickly.",}, 14,"ALL",null),
    Career("&6Career", Material.CLOCK, new String[]{ChatColor.GRAY+"displays the outcome and elo",ChatColor.GRAY+"change of all your recent matches","",ChatColor.STRIKETHROUGH+""+ChatColor.GRAY+"                                    "}, 11,"career",null);

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
