package kiul.kiulduelsv2.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Arenadata {

        private static File file;
        private static FileConfiguration arenaDataFile;

        public static void setup() {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("Kiul-Duels-V2").getDataFolder(), "arenadata.yml");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                }
            }
            arenaDataFile = YamlConfiguration.loadConfiguration(file);
        }

        public static FileConfiguration get() {
            return arenaDataFile;
        }



        public static void save(){
            try {
                arenaDataFile.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save, arenaDataFile.");
            }
        }

        public static void reload(){
            arenaDataFile = YamlConfiguration.loadConfiguration(file);
        }


        
}
