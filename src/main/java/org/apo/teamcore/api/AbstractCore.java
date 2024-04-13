package org.apo.teamcore.api;

import org.apo.teamcore.api.CoreAPI;
import org.apo.teamcore.Team_Core;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.List;

@SuppressWarnings("deprecation")
public abstract class AbstractCore implements CoreAPI {
    Team_Core tc=Team_Core.instance;
    public List<String> getCoreList() {
        return tc.getConfig().getStringList("core-list");
    }

    @Override
    public boolean CoreAlive(String name) {
        for (World w:Bukkit.getServer().getWorlds()) {
            for (Entity e: w.getEntities()) {
                if (e.getScoreboardTags().contains(name)) {
                    if (!e.isDead()) return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getCoreHealth(String name) {
        return tc.getConfig().getInt(name+".health");
    }

    @Override
    public String getCoreMaterial(String name) {
        return tc.getConfig().getString(name+".material");
    }
}
