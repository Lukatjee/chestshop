package eu.lukatjee.chestshop.commands;

import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.commandHandlers.helpHandler;
import eu.lukatjee.chestshop.commandHandlers.reloadHandler;
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

        /*

            Check if there are arguments given, if not it'll display the help menu
            In any other case, it'll check what the argument is and provide a response/action.

         */

        if (args.length == 0) {

            helpHandler help = new helpHandler();

        } else {

            if (args[0].equals("reload")) {

                reloadHandler reload = new reloadHandler();

                if (sender instanceof Player) {

                    String reloadPermission = configuration.getString("reloadPermission");

                    if (sender.hasPermission(reloadPermission)) {

                        reload.ReloadCommand(sender);

                    } else {

                        String noPermission = configuration.getString("noPermission");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));

                    }

                } else {

                    reload.ReloadCommand(sender);

                }

            } else {

                String unknownCommand = configuration.getString("unknownCommand");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', unknownCommand));

            }

        }

        return false;

    }

}
