package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.net.URL;
import java.util.*;



public enum HeadEnum {
    /**
     * Symbols
     */
            // RUBY - #aa0608 -> #eb352a
            // PERIDOT - #42b315 -> #8ef05e
            // TANZANITE - #1645c9 -> #5e9ff0
            // OPAL - #351296 -> #562deb
            // CONSTANT - #9876AA

    CLEAR_ENCHANTS("&#254B51&lC&#295654&lL&#2D6057&lE&#316B5A&lA&#34765C&lR &#3C8B62&lE&#409665&lN&#3C8A62&lC&#377D5E&lH&#33715B&lA&#2E6458&lN&#2A5854&lT&#254B51&lS", C.getURL("http://textures.minecraft.net/texture/a988419dd5b386f698a96913db1d97c2418e16d416d7f439d48acd41e3a436ce"), new String[]{C.t("&6⏵ &7Click to clear all enchants from your item")},"clear"),
    CUSTOMIZE_ITEM("&#62D2E2&lC&#6FD6E5&lU&#7DDAE8&lS&#8ADEEB&lT&#98E3ED&lO&#A5E7F0&lM&#B3EBF3&lI&#C0EFF6&lZ&#B0EAF3&lE &#91E1EC&lI&#81DCE9&lT&#72D7E5&lE&#62D2E2&lM", C.getURL("http://textures.minecraft.net/texture/692d5df805c239022fe1b45f940882bf40b559671937dc71fbc96f630250ebc4"), new String[]{C.t("&6⏵ &7Click to expand"),
    "",
    C.t("&c&lCOMING SOON")},"customize");

    private String displayName;
    private URL URL;
    private String[] lore;
    private String localName;


    HeadEnum(String displayName, URL url, String[] lore, String localName) {
        this.displayName = displayName;
        this.URL = url;
        this.lore = lore;
        this.localName = localName;

    }

    public String getDisplayName() {
        return displayName;
    }
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }
    public String[] getLore() {return lore;}
    public String getLocalName() {
        return localName;
    }
    public java.net.URL getURL() {
        return URL;
    }





}
