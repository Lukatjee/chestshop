package eu.lukatjee.chestshop;

import eu.lukatjee.chestshop.events.createBuyShop;
import eu.lukatjee.chestshop.events.createSellShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Chestshop extends JavaPlugin {

    public static Chestshop plugin;
    public static FileConfiguration configuration;

    @Override
    public void onEnable() {

        plugin = this;
        configuration = this.getConfig();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new createSellShop(), this);
        getServer().getPluginManager().registerEvents(new createBuyShop(), this);

    }

}
