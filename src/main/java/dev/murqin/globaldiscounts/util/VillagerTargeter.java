package dev.murqin.globaldiscounts.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import java.util.Optional;

/**
 * Oyuncunun baktığı köylüyü tespit eden utility sınıfı.
 * Spigot uyumluluğu için manuel raycast kullanır.
 */
public class VillagerTargeter {

    private static final double MAX_DISTANCE = 5.0;
    private static final double MAX_ANGLE = 0.26; // ~15 derece

    /**
     * Oyuncunun baktığı köylüyü bulur.
     * 
     * @param player Hedefleyen oyuncu
     * @return Hedeflenen köylü veya empty
     */
    public Optional<Villager> getTargetVillager(Player player) {
        Villager target = null;
        double closestDistance = MAX_DISTANCE;
        
        for (Entity entity : player.getNearbyEntities(MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE)) {
            if (entity instanceof Villager villager) {
                Vector toEntity = villager.getEyeLocation().toVector()
                    .subtract(player.getEyeLocation().toVector());
                Vector direction = player.getLocation().getDirection();
                
                double angle = toEntity.angle(direction);
                double distance = player.getLocation().distance(villager.getLocation());
                
                if (angle < MAX_ANGLE && distance < closestDistance) {
                    closestDistance = distance;
                    target = villager;
                }
            }
        }
        
        return Optional.ofNullable(target);
    }
}
