package dev.murqin.globaldiscounts.command.subcommand;

import dev.murqin.globaldiscounts.service.DiscountService;
import dev.murqin.globaldiscounts.util.VillagerTargeter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

/**
 * Tek köylünün indirimlerini temizleyen komut.
 */
public class ClearCommand implements SubCommand {

    private final DiscountService discountService;
    private final VillagerTargeter targeter;

    public ClearCommand(DiscountService discountService, VillagerTargeter targeter) {
        this.discountService = discountService;
        this.targeter = targeter;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut bir oyuncu tarafından kullanılmalıdır.");
            return;
        }

        Villager villager = targeter.getTargetVillager(player).orElse(null);
        if (villager == null) {
            sender.sendMessage(ChatColor.RED + "Bu komutu kullanmak için bir köylüye bakın.");
            return;
        }

        int cleared = discountService.clearDiscounts(villager);
        sender.sendMessage(ChatColor.GREEN + String.valueOf(cleared) + " senkronize indirim temizlendi.");
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Senkronize indirimleri temizle (tek köylü)";
    }
}
