package dev.murqin.globaldiscounts.listener;

import dev.murqin.globaldiscounts.service.DiscountService;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.potion.PotionEffectType;

/**
 * Ticaret envanteri açıldığında indirimleri senkronize eden listener.
 */
public class TradeListener implements Listener {

    private final DiscountService discountService;

    public TradeListener(DiscountService discountService) {
        this.discountService = discountService;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Merchant envanteri mi kontrol et
        if (!(event.getInventory() instanceof MerchantInventory merchantInventory)) {
            return;
        }

        // Oyuncu mu kontrol et
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        Merchant merchant = merchantInventory.getMerchant();
        
        // Köylü mü kontrol et
        if (!(merchantInventory.getHolder() instanceof Villager villager)) {
            return;
        }

        // Senkronizasyon devre dışı mı kontrol et
        if (!discountService.isSyncEnabled(villager)) {
            return;
        }

        // Hero of the Village efektini atla
        if (player.hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE)) {
            return;
        }

        // İndirimleri yakala ve uygula
        discountService.captureDiscounts(villager, merchant);
        discountService.applyStoredDiscounts(villager, merchant);
    }
}
