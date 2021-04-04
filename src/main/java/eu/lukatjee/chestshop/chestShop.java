package eu.lukatjee.chestshop;

import eu.lukatjee.chestshop.commands.MainCommand;
import eu.lukatjee.chestshop.events.createBuyShop;
import eu.lukatjee.chestshop.events.createSellShop;
import org.bukkit.plugin.java.JavaPlugin;

public final class chestShop extends JavaPlugin {

    public static chestShop plugin;

    @Override
    public void onEnable() {

        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new createSellShop(), this);
        getServer().getPluginManager().registerEvents(new createBuyShop(), this);
        getCommand("chestshop").setExecutor(new MainCommand());

    }

}
