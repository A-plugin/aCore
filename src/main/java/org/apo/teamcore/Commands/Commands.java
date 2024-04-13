package org.apo.teamcore.Commands;

import org.apo.teamcore.Team_Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Commands implements TabExecutor {

    public Commands(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("core");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String ladel, String[] args) {
        if (commandSender instanceof Player) {
            Player p=(Player) commandSender;
            if (p.hasPermission("Core.core")) {
                if (args.length==1) if (args[0].equalsIgnoreCase("remove")) p.sendMessage(ChatColor.RED+"[Core] /core [remove] [name]");
                if (args.length==1 || args.length==2 || args.length==3) {
                    if (!args[0].equalsIgnoreCase("remove")) p.sendMessage(ChatColor.RED+"[Core] /core [create] [name] [health] [block]");
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    create(args[1], args[2], args[3], p);
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    remove(args[1], p);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length==1) {
            completions.add("create");
            completions.add("remove");
        }
        if (args[0].equalsIgnoreCase("create")){
            if (args.length == 2) completions.add("name");
            if (args.length == 3) completions.add("health");
            if (args.length==4) {
                for (Material material : Material.values()) {
                    if (material.isBlock()){
                        completions.add(material.name().toLowerCase());
                    }
                }
            }
        }
        if (args[0].equalsIgnoreCase("remove")){
            if (args.length == 2) completions.add("name");
        }
        return completions;
    }

    Configuration config= Team_Core.instance.getConfig();


    public void create(String n, String hp,String m, Player p) {
        Location l=p.getEyeLocation();
        Vector v=l.getDirection().multiply(3);
        Location sl=l.add(v);
        Material material = Material.matchMaterial(m.toUpperCase());
        String reg="^[\\d]*$";
        if (material != null) {
            if (Pattern.matches(reg,hp)){
                BlockData d = Bukkit.createBlockData(m);
                FallingBlock f = p.getWorld().spawnFallingBlock(sl, d);
                f.setGravity(false);
                f.setPersistent(true);
                f.addScoreboardTag(n);
                Slime sli= p.getWorld().spawn(sl, Slime.class, s -> {
                    s.setAI(false);
                    s.setSize(4);
                    s.setMaxHealth(Double.parseDouble(hp));
                    s.setHealth(Double.parseDouble(hp));
                    s.addScoreboardTag(n);
                    s.setCustomName(n);
                    s.setCustomNameVisible(true);
                    s.setInvisible(true);
                    s.setSilent(true);
                });
                p.getWorld().spawn(sl, ArmorStand.class, a-> {
                    a.setSmall(true);
                    a.setSilent(true);
                    a.setInvisible(true);
                    a.setInvulnerable(true);
                    a.setGravity(false);
                    a.addPassenger(sli);
                    a.addPassenger(f);
                    a.addScoreboardTag(n);
                });

                config.set(n+".name", n);
                config.set(n+"health", hp);
                config.set(n+"material", m);
                List<String> corel=config.getStringList("core-list");
                if (corel==null) corel=new ArrayList<>();
                corel.add(n);
                config.set("core-list", corel);
                Team_Core.instance.saveConfig();
                p.sendMessage("§a[Core]§f 코어 §e"+n+"§f을(를) §a생성§f했습니다.");
            } else {
                p.sendMessage(ChatColor.RED + "[Core] Nan Health");
            }
        } else {
            p.sendMessage(ChatColor.RED + "[Core] Invalid block type");
        }
    }
    public void remove(String n, Player p) {
        for (Entity en : p.getWorld().getEntities()) {
            if (en.getScoreboardTags().toString().contains(n)) {
                en.remove();
                p.sendMessage(en.toString());
                p.sendMessage("§a[Core]§f 코어 §e" + en.getScoreboardTags() + "§f을(를) §c삭제§f했습니다. (§c현제 이 기능은 불완전합니다§f)");
            }
        }
    }
}
