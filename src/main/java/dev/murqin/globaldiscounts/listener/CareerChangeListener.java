package dev.murqin.globaldiscounts.listener;

import dev.murqin.globaldiscounts.service.DiscountService;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

/**
 * Köylü meslek değiştirdiğinde indirimleri temizleyen listener.
 */
public class CareerChangeListener implements Listener {

    private final DiscountService discountService;

    public CareerChangeListener(DiscountService discountService) {
        this.discountService = discountService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCareerChange(VillagerCareerChangeEvent event) {
        Villager villager = event.getEntity();
        discountService.handleCareerChange(villager);
    }
}
