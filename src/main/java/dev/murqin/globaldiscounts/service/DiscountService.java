package dev.murqin.globaldiscounts.service;

import dev.murqin.globaldiscounts.util.RecipeKeyGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * İndirim senkronizasyonu için merkezi servis sınıfı.
 * Tüm indirim iş mantığını yönetir.
 */
public class DiscountService {

    private final Plugin plugin;
    private final RecipeKeyGenerator keyGenerator;
    private final NamespacedKey enabledKey;
    private final Logger logger;

    public DiscountService(Plugin plugin, RecipeKeyGenerator keyGenerator) {
        this.plugin = plugin;
        this.keyGenerator = keyGenerator;
        this.enabledKey = new NamespacedKey(plugin, "sync_enabled");
        this.logger = plugin.getLogger();
    }

    /**
     * Köylü için senkronizasyonun aktif olup olmadığını kontrol eder.
     */
    public boolean isSyncEnabled(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        return !pdc.has(enabledKey) || pdc.getOrDefault(enabledKey, PersistentDataType.BYTE, (byte) 1) == 1;
    }

    /**
     * Köylü için senkronizasyonu etkinleştirir.
     */
    public void enableSync(Villager villager) {
        villager.getPersistentDataContainer().set(enabledKey, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * Köylü için senkronizasyonu devre dışı bırakır.
     */
    public void disableSync(Villager villager) {
        villager.getPersistentDataContainer().set(enabledKey, PersistentDataType.BYTE, (byte) 0);
    }

    /**
     * Mevcut tariflerdeki indirimleri yakalar ve saklar.
     */
    public void captureDiscounts(Villager villager, Merchant merchant) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> recipes = merchant.getRecipes();

        for (MerchantRecipe recipe : recipes) {
            int currentDiscount = recipe.getSpecialPrice();
            
            if (currentDiscount < 0) {
                NamespacedKey key = keyGenerator.generate(recipe);
                Integer storedDiscount = pdc.get(key, PersistentDataType.INTEGER);
                
                if (storedDiscount == null || currentDiscount < storedDiscount) {
                    pdc.set(key, PersistentDataType.INTEGER, currentDiscount);
                }
            }
        }
    }

    /**
     * Saklanan indirimleri tariflere uygular.
     */
    public void applyStoredDiscounts(Villager villager, Merchant merchant) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> originalRecipes = merchant.getRecipes();
        List<MerchantRecipe> modifiedRecipes = new ArrayList<>();

        boolean hasChanges = false;
        
        for (MerchantRecipe original : originalRecipes) {
            NamespacedKey key = keyGenerator.generate(original);
            Integer storedDiscount = pdc.get(key, PersistentDataType.INTEGER);
            int currentDiscount = original.getSpecialPrice();
            
            if (storedDiscount != null && storedDiscount < 0 && storedDiscount < currentDiscount) {
                MerchantRecipe modified = cloneRecipeWithDiscount(original, storedDiscount);
                modifiedRecipes.add(modified);
                hasChanges = true;
            } else {
                modifiedRecipes.add(original);
            }
        }

        if (hasChanges) {
            merchant.setRecipes(modifiedRecipes);
        }
    }

    /**
     * Köylüden tüm senkronize indirimleri temizler.
     * 
     * @return Temizlenen indirim sayısı
     */
    public int clearDiscounts(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> recipes = villager.getRecipes();
        
        int cleared = 0;
        for (MerchantRecipe recipe : recipes) {
            NamespacedKey key = keyGenerator.generate(recipe);
            if (pdc.has(key)) {
                pdc.remove(key);
                cleared++;
            }
        }
        return cleared;
    }

    /**
     * Tüm dünyalardaki tüm köylülerin indirimlerini temizler.
     * 
     * @return [temizlenen köylü sayısı, temizlenen indirim sayısı]
     */
    public int[] clearAllDiscounts() {
        int villagersCleared = 0;
        int discountsCleared = 0;
        
        Server server = plugin.getServer();
        for (org.bukkit.World world : server.getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                int cleared = clearDiscounts(villager);
                if (cleared > 0) {
                    discountsCleared += cleared;
                    villagersCleared++;
                }
            }
        }
        
        return new int[]{villagersCleared, discountsCleared};
    }

    /**
     * Köylünün kayıtlı indirimlerini getirir.
     * 
     * @return [tarif adı, indirim miktarı] çiftlerinin listesi
     */
    public List<String[]> getStoredDiscounts(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> recipes = villager.getRecipes();
        List<String[]> result = new ArrayList<>();
        
        for (MerchantRecipe recipe : recipes) {
            NamespacedKey key = keyGenerator.generate(recipe);
            Integer stored = pdc.get(key, PersistentDataType.INTEGER);
            if (stored != null && stored < 0) {
                result.add(new String[]{
                    recipe.getResult().getType().name(),
                    String.valueOf(stored)
                });
            }
        }
        
        return result;
    }

    /**
     * Tarifi belirtilen indirimle klonlar.
     */
    private MerchantRecipe cloneRecipeWithDiscount(MerchantRecipe original, int discount) {
        int basePrice = original.getIngredients().get(0).getAmount();
        int adjustedPrice = basePrice + discount;
        
        if (adjustedPrice < 1) {
            discount = -(basePrice - 1);
        }
        
        MerchantRecipe modified = new MerchantRecipe(
            original.getResult(),
            original.getUses(),
            original.getMaxUses(),
            original.hasExperienceReward(),
            original.getVillagerExperience(),
            original.getPriceMultiplier(),
            original.getDemand(),
            discount
        );
        modified.setIngredients(original.getIngredients());
        return modified;
    }

    /**
     * Meslek değişikliği nedeniyle indirimleri temizler ve log'lar.
     */
    public void handleCareerChange(Villager villager) {
        int cleared = clearDiscounts(villager);
        
        if (cleared > 0) {
            logger.info("Meslek değişikliği nedeniyle " + villager.getUniqueId() + 
                       " köylüsünden " + cleared + " senkronize indirim temizlendi.");
        }
    }
}
