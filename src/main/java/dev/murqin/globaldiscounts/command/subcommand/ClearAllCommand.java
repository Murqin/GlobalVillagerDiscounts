package dev.murqin.globaldiscounts.command.subcommand;

import dev.murqin.globaldiscounts.service.DiscountService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Tüm köylülerin indirimlerini temizleyen komut.
 */
public class ClearAllCommand implements SubCommand {

    private final DiscountService discountService;

    public ClearAllCommand(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int[] result = discountService.clearAllDiscounts();
        int villagersCleared = result[0];
        int discountsCleared = result[1];
        
        sender.sendMessage(ChatColor.GREEN + String.valueOf(villagersCleared) + " köylüden " + 
                          discountsCleared + " indirim temizlendi.");
    }

    @Override
    public String getName() {
        return "clearall";
    }

    @Override
    public String getDescription() {
        return "TÜM senkronize indirimleri temizle (tüm köylüler)";
    }
}
