package eu.lukatjee.chestshop.commands;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class mainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<Object> commandArguments = Arrays.asList("reload", "help");
        FileConfiguration configuration = chestShop.plugin.getConfig();

        if (args.length == 0) {

            for (String text : configuration.getStringList("helpText")) {

                String message = ChatColor.translateAlternateColorCodes('&', text);
                sender.sendMessage(message);

            }

        } else {

            if (args[0].equals("reload")) {

                reloadCommand reload = new reloadCommand();
                if (!(sender instanceof Player)) {

                    reload.ReloadCommand(sender);
                    return false;

                }

                String reloadPermission = configuration.getString("reloadPermission");
                if (sender.hasPermission(reloadPermission)) {

                    reload.ReloadCommand(sender);

                } else {

                    String noPermission = configuration.getString("noPermission");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));

                }

            } else {

                String unknownCommand = configuration.getString("unknownCommand");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', unknownCommand));

            }

        }

        return false;

    }

}
