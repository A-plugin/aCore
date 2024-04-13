package org.apo.teamcore;

import org.apo.teamcore.Commands.Commands;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Team_Core extends JavaPlugin implements Listener {
    Configuration config=getConfig();

    public static Team_Core instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance=this;
        getServer().getPluginManager().registerEvents(new org.apo.teamcore.Listener.Listener(), this);
        getLogger().info(ChatColor.LIGHT_PURPLE+"Made By.APO2073");
        new Commands(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}