package dev.murqin.globaldiscounts.command.subcommand;

import dev.murqin.globaldiscounts.service.DiscountService;
import dev.murqin.globaldiscounts.util.VillagerTargeter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

/**
 * Köylü için senkronizasyonu etkinleştiren komut.
 */
public class EnableCommand implements SubCommand {

    private final DiscountService discountService;
    private final VillagerTargeter targeter;

    public EnableCommand(DiscountService discountService, VillagerTargeter targeter) {
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

        discountService.enableSync(villager);
        sender.sendMessage(ChatColor.GREEN + "Bu köylü için indirim senkronizasyonu ETKİNLEŞTİRİLDİ.");
    }

    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String getDescription() {
        return "Köylü için senkronizasyonu etkinleştir";
    }
}
