package dev.murqin.globaldiscounts.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;

/**
 * Tarif anahtarı oluşturucu.
 * Her tarif için benzersiz NamespacedKey üretir.
 */
public class RecipeKeyGenerator {

    private static final String DISCOUNT_KEY_PREFIX = "d_";
    private final Plugin plugin;

    public RecipeKeyGenerator(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sonuç ve ilk malzemeye göre tarif için benzersiz bir anahtar oluşturur.
     * 
     * @param recipe Ticaret tarifi
     * @return Benzersiz NamespacedKey
     */
    public NamespacedKey generate(MerchantRecipe recipe) {
        ItemStack result = recipe.getResult();
        ItemStack ingredient = recipe.getIngredients().get(0);
        
        // Sonuç türü + malzeme türü + malzeme miktarından hash oluştur
        String hash = result.getType().name() + "_" + ingredient.getType().name() + "_" + ingredient.getAmount();
        return new NamespacedKey(plugin, DISCOUNT_KEY_PREFIX + hash.hashCode());
    }
}
