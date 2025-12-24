package dev.murqin.globaldiscounts;

import dev.murqin.globaldiscounts.command.GvdCommandExecutor;
import dev.murqin.globaldiscounts.command.subcommand.*;
import dev.murqin.globaldiscounts.listener.CareerChangeListener;
import dev.murqin.globaldiscounts.listener.TradeListener;
import dev.murqin.globaldiscounts.service.DiscountService;
import dev.murqin.globaldiscounts.util.RecipeKeyGenerator;
import dev.murqin.globaldiscounts.util.VillagerTargeter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * GlobalVillagerDiscounts - Köylü ticaret indirimlerini tüm oyuncular arasında senkronize eder.
 * 
 * <p>İndirimleri tarif hash'ine göre (sonuç + malzeme) doğru eşleştirme için saklar.
 * Meslek değişikliğinde indirimleri temizler. Vanilla dedikodu sisteminden ayrıdır.</p>
 * 
 * @author murqin
 * @version 1.2.0
 */
public final class GlobalVillagerDiscounts extends JavaPlugin {

    @Override
    public void onEnable() {
        // Utility'leri oluştur
        RecipeKeyGenerator keyGenerator = new RecipeKeyGenerator(this);
        VillagerTargeter targeter = new VillagerTargeter();
        
        // Ana servisi oluştur
        DiscountService discountService = new DiscountService(this, keyGenerator);
        
        // Listener'ları kaydet
        getServer().getPluginManager().registerEvents(new TradeListener(discountService), this);
        getServer().getPluginManager().registerEvents(new CareerChangeListener(discountService), this);
        
        // Komut sistemini kur
        GvdCommandExecutor commandExecutor = new GvdCommandExecutor();
        commandExecutor.registerSubCommand(new InfoCommand(discountService, targeter));
        commandExecutor.registerSubCommand(new ClearCommand(discountService, targeter));
        commandExecutor.registerSubCommand(new ClearAllCommand(discountService));
        commandExecutor.registerSubCommand(new DisableCommand(discountService, targeter));
        commandExecutor.registerSubCommand(new EnableCommand(discountService, targeter));
        
        getCommand("gvd").setExecutor(commandExecutor);
        
        getLogger().info("GlobalVillagerDiscounts etkinleştirildi! Admin komutları için /gvd kullanın.");
    }

    @Override
    public void onDisable() {
        getLogger().info("GlobalVillagerDiscounts devre dışı bırakıldı.");
    }
}
