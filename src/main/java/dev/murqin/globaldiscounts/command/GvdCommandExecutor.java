package dev.murqin.globaldiscounts.command;

import dev.murqin.globaldiscounts.command.subcommand.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Ana komut yönlendiricisi.
 * Alt komutları kaydeder ve yönlendirir.
 */
public class GvdCommandExecutor implements CommandExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    /**
     * Alt komut kaydeder.
     */
    public void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("gvd")) {
            return false;
        }

        if (!sender.hasPermission("gvd.admin")) {
            sender.sendMessage(ChatColor.RED + "Bu komutu kullanma izniniz yok.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand != null) {
            // Alt argümanları ayarla (ilk argümanı çıkar)
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            
            subCommand.execute(sender, subArgs);
        } else {
            sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "=== GlobalVillagerDiscounts Admin ===");
        
        for (SubCommand subCommand : subCommands.values()) {
            sender.sendMessage(ChatColor.YELLOW + "/gvd " + subCommand.getName() + " " + 
                              ChatColor.GRAY + "- " + subCommand.getDescription());
        }
    }
}
