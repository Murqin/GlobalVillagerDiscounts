package dev.murqin.globaldiscounts.command.subcommand;

import org.bukkit.command.CommandSender;

/**
 * Alt komut arayüzü.
 * Yeni komutlar kolayca eklenebilir (Open/Closed prensibi).
 */
public interface SubCommand {

    /**
     * Komutu çalıştırır.
     * 
     * @param sender Komutu gönderen
     * @param args Komut argümanları
     */
    void execute(CommandSender sender, String[] args);

    /**
     * Komutun adını döndürür.
     */
    String getName();

    /**
     * Yardım metninde gösterilecek açıklamayı döndürür.
     */
    String getDescription();
}
