package com.ruskydev.rotatingmotds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class RotatingMOTDs extends JavaPlugin {
    private MOTDManager motdManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        boolean enabled = getConfig().getBoolean("enabled");
        if (!enabled) {
            getLogger().info("Rotating MOTDs is disabled. Set 'enabled' to true in config.yml to enable it.");
            return;
        }

        motdManager = new MOTDManager(this);
        int intervalSeconds = getConfig().getInt("intervalSeconds", 60);
        motdManager.runTaskTimer(this, 0, intervalSeconds * 20L);
        getLogger().info("Rotating MOTDs has been enabled with an interval of " + intervalSeconds + " seconds.");
    }

    @Override
    public void onDisable() {
        if (motdManager != null) {
            motdManager.cancel();
        }
        getLogger().info("Rotating MOTDs has been disabled.");
    }

    public class MOTDManager extends BukkitRunnable {
        private final RotatingMOTDs plugin;
        private final FileConfiguration config;
        private final List<String> motds;
        private final String motdColor;

        public MOTDManager(RotatingMOTDs plugin) {
            this.plugin = plugin;
            this.config = plugin.getConfig();
            this.motds = config.getStringList("motds");
            this.motdColor = ChatColor.translateAlternateColorCodes('&', config.getString("motdsColor"));
        }

        @Override
        public void run() {
            if (motds.isEmpty()) {
                return;
            }

            int randomIndex = new Random().nextInt(motds.size());
            String motd = motdColor + motds.get(randomIndex);

            try {
                Bukkit.getServer().setMotd(motd);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while setting MOTD: " + e.getMessage());
            }
        }
    }
}
