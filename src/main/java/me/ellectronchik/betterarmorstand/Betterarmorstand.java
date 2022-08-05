package me.ellectronchik.betterarmorstand;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;


public final class Betterarmorstand extends JavaPlugin {
    public Inventory standGUI;

    @Override
    public void onEnable() {
        // Plugin startup logic

        File config = new File("plugins/BetterArmorStand/config.yml");
        if(!config.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Config had been loaded");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        standGUI = Bukkit.createInventory(null, 54, "Settings");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "BetterArmorStand "+ getDescription().getVersion() +" enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "BetterArmorStand "+ getDescription().getVersion() + " disabled");
    }
}
