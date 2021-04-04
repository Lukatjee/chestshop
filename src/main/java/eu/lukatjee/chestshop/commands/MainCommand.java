package eu.lukatjee.chestshop.commands;

import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.commandHandlers.Help;
import eu.lukatjee.chestshop.commandHandlers.Reload;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<Object> commandArguments = Arrays.asList("reload", "help");
        FileConfiguration configuration = chestShop.plugin.getConfig();

        if (args.length == 0) {

            Help help = new Help();

        } else {

            if (args[0].equals("reload")) {

                Reload reload = new Reload();

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

            }

        }

        return false;

    }

}
