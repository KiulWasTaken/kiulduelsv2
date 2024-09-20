package kiul.kiulduelsv2.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomKitData {
    private static File file;
    private static FileConfiguration customKitFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Kiul-Duels-V2").getDataFolder(), "kits.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        customKitFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customKitFile;
    }



    public static void save(){
        try {
            customKitFile.save(file);
        } catch (IOException e) {
            System.out.println("Failed to save, customKitFile.");
        }
    }

    public static void reload(){
        customKitFile = YamlConfiguration.loadConfiguration(file);
    }
}
