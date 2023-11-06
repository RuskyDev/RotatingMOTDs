package com.ruskydev.rotatingmotds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

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

        Bukkit.getServer().setMotd(motd);
    }
}
