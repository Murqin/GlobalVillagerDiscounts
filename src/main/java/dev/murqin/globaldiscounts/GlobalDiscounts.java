package dev.murqin.globaldiscounts;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalVillagerDiscounts - Synchronizes villager trade discounts across all players.
 * 
 * <p>Stores discounts by recipe hash (result + ingredient) for accurate matching.
 * Clears discounts on career change. Separate from vanilla gossip.</p>
 * 
 * @author murqin
 * @version 1.2.0
 */
public final class GlobalDiscounts extends JavaPlugin implements Listener {

    private static final String DISCOUNT_KEY_PREFIX = "d_";
    private NamespacedKey enabledKey;

    @Override
    public void onEnable() {
        enabledKey = new NamespacedKey(this, "sync_enabled");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("GlobalVillagerDiscounts enabled! Use /gvd for admin commands.");
    }

    @Override
    public void onDisable() {
        getLogger().info("GlobalVillagerDiscounts disabled.");
    }

    /**
     * Creates a unique key for a recipe based on result and first ingredient.
     */
    private NamespacedKey getRecipeKey(MerchantRecipe recipe) {
        ItemStack result = recipe.getResult();
        ItemStack ingredient = recipe.getIngredients().get(0);
        
        // Create hash from result type + ingredient type + ingredient amount
        String hash = result.getType().name() + "_" + ingredient.getType().name() + "_" + ingredient.getAmount();
        return new NamespacedKey(this, DISCOUNT_KEY_PREFIX + hash.hashCode());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("gvd")) {
            return false;
        }

        if (!sender.hasPermission("gvd.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info" -> handleInfo(sender);
            case "clear" -> handleClear(sender);
            case "clearall" -> handleClearAll(sender);
            case "disable" -> handleDisable(sender);
            case "enable" -> handleEnable(sender);
            default -> sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "=== GlobalVillagerDiscounts Admin ===");
        sender.sendMessage(ChatColor.YELLOW + "/gvd info " + ChatColor.GRAY + "- Show synced discount info");
        sender.sendMessage(ChatColor.YELLOW + "/gvd clear " + ChatColor.GRAY + "- Clear synced discounts (one villager)");
        sender.sendMessage(ChatColor.YELLOW + "/gvd clearall " + ChatColor.GRAY + "- Clear ALL synced discounts (all villagers)");
        sender.sendMessage(ChatColor.YELLOW + "/gvd disable " + ChatColor.GRAY + "- Disable sync for villager");
        sender.sendMessage(ChatColor.YELLOW + "/gvd enable " + ChatColor.GRAY + "- Enable sync for villager");
    }

    private Villager getTargetVillager(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command must be used by a player.");
            return null;
        }

        // Manual raycast for Spigot compatibility
        Villager target = null;
        double closestDistance = 5.0;
        
        for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
            if (entity instanceof Villager villager) {
                org.bukkit.util.Vector toEntity = villager.getEyeLocation().toVector()
                    .subtract(player.getEyeLocation().toVector());
                org.bukkit.util.Vector direction = player.getLocation().getDirection();
                
                double angle = toEntity.angle(direction);
                double distance = player.getLocation().distance(villager.getLocation());
                
                if (angle < 0.26 && distance < closestDistance) {
                    closestDistance = distance;
                    target = villager;
                }
            }
        }
        
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Look at a villager to use this command.");
            return null;
        }

        return target;
    }

    private void handleInfo(CommandSender sender) {
        Villager villager = getTargetVillager(sender);
        if (villager == null) return;

        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        boolean syncEnabled = !pdc.has(enabledKey) || pdc.getOrDefault(enabledKey, PersistentDataType.BYTE, (byte) 1) == 1;
        
        sender.sendMessage(ChatColor.GREEN + "=== Synced Discount Info ===");
        sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.WHITE + villager.getUniqueId());
        sender.sendMessage(ChatColor.YELLOW + "Profession: " + ChatColor.WHITE + villager.getProfession());
        sender.sendMessage(ChatColor.YELLOW + "Sync Enabled: " + (syncEnabled ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
        
        List<MerchantRecipe> recipes = villager.getRecipes();
        int storedCount = 0;
        for (MerchantRecipe recipe : recipes) {
            NamespacedKey key = getRecipeKey(recipe);
            Integer stored = pdc.get(key, PersistentDataType.INTEGER);
            if (stored != null && stored < 0) {
                storedCount++;
                sender.sendMessage(ChatColor.GRAY + "  " + recipe.getResult().getType().name() + 
                                  ": " + ChatColor.AQUA + stored + " emeralds");
            }
        }
        
        if (storedCount == 0) {
            sender.sendMessage(ChatColor.GRAY + "  No synced discounts stored.");
        }
    }

    private void handleClear(CommandSender sender) {
        Villager villager = getTargetVillager(sender);
        if (villager == null) return;

        int cleared = clearVillagerDiscounts(villager);
        sender.sendMessage(ChatColor.GREEN + "Cleared " + cleared + " synced discounts.");
    }

    private void handleClearAll(CommandSender sender) {
        int villagersCleared = 0;
        int discountsCleared = 0;
        
        for (org.bukkit.World world : getServer().getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                int cleared = clearVillagerDiscounts(villager);
                if (cleared > 0) {
                    discountsCleared += cleared;
                    villagersCleared++;
                }
            }
        }
        
        sender.sendMessage(ChatColor.GREEN + "Cleared " + discountsCleared + " discounts from " + villagersCleared + " villagers.");
    }

    /**
     * Clears all synced discounts from a villager.
     * @return Number of discounts cleared
     */
    private int clearVillagerDiscounts(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> recipes = villager.getRecipes();
        
        int cleared = 0;
        for (MerchantRecipe recipe : recipes) {
            NamespacedKey key = getRecipeKey(recipe);
            if (pdc.has(key)) {
                pdc.remove(key);
                cleared++;
            }
        }
        return cleared;
    }

    private void handleDisable(CommandSender sender) {
        Villager villager = getTargetVillager(sender);
        if (villager == null) return;

        villager.getPersistentDataContainer().set(enabledKey, PersistentDataType.BYTE, (byte) 0);
        sender.sendMessage(ChatColor.YELLOW + "Discount sync DISABLED for this villager.");
    }

    private void handleEnable(CommandSender sender) {
        Villager villager = getTargetVillager(sender);
        if (villager == null) return;

        villager.getPersistentDataContainer().set(enabledKey, PersistentDataType.BYTE, (byte) 1);
        sender.sendMessage(ChatColor.GREEN + "Discount sync ENABLED for this villager.");
    }

    /**
     * Clears synced discounts when villager changes profession.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCareerChange(VillagerCareerChangeEvent event) {
        Villager villager = event.getEntity();
        int cleared = clearVillagerDiscounts(villager);
        
        if (cleared > 0) {
            getLogger().info("Cleared " + cleared + " synced discounts from villager " + 
                           villager.getUniqueId() + " due to career change.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory merchantInventory)) {
            return;
        }

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        Merchant merchant = merchantInventory.getMerchant();
        
        if (!(merchantInventory.getHolder() instanceof Villager villager)) {
            return;
        }

        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        
        // Check if sync is disabled
        if (pdc.has(enabledKey) && pdc.getOrDefault(enabledKey, PersistentDataType.BYTE, (byte) 1) == 0) {
            return;
        }

        // Skip Hero of the Village
        if (player.hasPotionEffect(org.bukkit.potion.PotionEffectType.HERO_OF_THE_VILLAGE)) {
            return;
        }

        captureDiscounts(villager, merchant);
        applyStoredDiscounts(villager, merchant);
    }

    private void captureDiscounts(Villager villager, Merchant merchant) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> recipes = merchant.getRecipes();

        for (MerchantRecipe recipe : recipes) {
            int currentDiscount = recipe.getSpecialPrice();
            
            if (currentDiscount < 0) {
                NamespacedKey key = getRecipeKey(recipe);
                Integer storedDiscount = pdc.get(key, PersistentDataType.INTEGER);
                
                if (storedDiscount == null || currentDiscount < storedDiscount) {
                    pdc.set(key, PersistentDataType.INTEGER, currentDiscount);
                }
            }
        }
    }

    private void applyStoredDiscounts(Villager villager, Merchant merchant) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        List<MerchantRecipe> originalRecipes = merchant.getRecipes();
        List<MerchantRecipe> modifiedRecipes = new ArrayList<>();

        boolean hasChanges = false;
        
        for (MerchantRecipe original : originalRecipes) {
            NamespacedKey key = getRecipeKey(original);
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
}
