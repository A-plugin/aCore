package org.apo.teamcore.Listener;

import org.apo.teamcore.Team_Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void slime(SlimeSplitEvent e) {
        if (e.getEntity().getScoreboardTags()!=null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void dama(EntityDamageEvent e) {
        if (e.getEntity() instanceof Slime) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                if (e.getEntity().getScoreboardTags()!=null){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void bossbar(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Slime) {
            Slime slime = (Slime) e.getEntity();
            if (slime.getScoreboardTags() != null) {
                e.setDamage(1);
                double maxHealth = slime.getMaxHealth();
                double currentHealth = slime.getHealth();
                double progress = currentHealth / maxHealth;
                progress = Math.max(0.0, Math.min(progress, 1.0));
                BossBar b = Bukkit.createBossBar(slime.getScoreboardTags().toString(), BarColor.GREEN, BarStyle.SEGMENTED_20);
                b.setVisible(false);
                b.addPlayer((Player) e.getDamager());
                b.setVisible(true);
                b.setProgress(progress);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        b.setVisible(false);
                    }
                }.runTaskLater(Team_Core.instance, 15L);
            }
        }
    }

    @EventHandler
    public void death(EntityDeathEvent e) {
        if (e.getEntity() instanceof Slime) {
            if (e.getEntity().getScoreboardTags() !=null) {
                String tag=e.getEntity().getScoreboardTags().toString();
                tag=tag.replace("[","");
                tag=tag.replace("]","");
                Bukkit.broadcastMessage("§a[Core]§f 코어 §c"+tag+"§f 이(가) 파괴되었습니다!");
                e.setDroppedExp(0);
                Location location = e.getEntity().getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                location.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, location, 50);
                location.getWorld().spawnParticle(Particle.FLAME, location, 50);

                for (Entity en: e.getEntity().getNearbyEntities(2,2,2)) {
                    if (en instanceof FallingBlock || en instanceof ArmorStand) {
                        en.remove();
                    }
                }
            }
        }
     }

     @EventHandler
     public void item(ItemSpawnEvent e) {
        for (Entity en:e.getEntity().getNearbyEntities(2,2,2)) {
            Item i=e.getEntity();
            if (en instanceof Slime) {
                BlockData d = Bukkit.createBlockData(i.getItemStack().getType());
                FallingBlock f = en.getWorld().spawnFallingBlock(en.getLocation(), d);
                f.setGravity(false);
                f.setPersistent(true);
                f.addScoreboardTag(en.getScoreboardTags().toString());
                e.getEntity().remove();
                Bukkit.broadcastMessage("a");
            }
        }
     }

     @EventHandler
    public void Falling(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            e.setCancelled(true);
        }
     }

}
