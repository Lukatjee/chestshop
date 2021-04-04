package eu.lukatjee.chestshop.commandHandlers;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Reload {

    public void ReloadCommand(CommandSender sender) {

        FileConfiguration configuration = chestShop.plugin.getConfig();
        chestShop plugin = chestShop.plugin;

        if (sender instanceof Player) {

            String player = sender.getName();

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configuration.getString("reloadMessage")));
            System.out.println("[Chestshop] Plugin was reloaded by " + player + ".");

        } else {

            System.out.println("[Chestshop] Plugin was reloaded by console.");

        }

        plugin.reloadConfig();

    }

}
