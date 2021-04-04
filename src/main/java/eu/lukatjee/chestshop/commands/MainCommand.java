package eu.lukatjee.chestshop.commands;

import eu.lukatjee.chestshop.commandHandlers.Help;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<Object> commandArguments = Arrays.asList("create", "remove", "''", "\"\"");

        if (args.length == 0) {

            Help help = new Help();

        } else {

            if (args[0].equals("")) {


            }

        }

        return false;

    }

}
