package eu.lukatjee.chestshop;

import eu.lukatjee.chestshop.commands.mainCommand;
import eu.lukatjee.chestshop.events.createShop;
import eu.lukatjee.chestshop.events.deleteShop;
import eu.lukatjee.chestshop.events.hopperPlacement;
import eu.lukatjee.chestshop.interactions.chestshopInteractions;
import eu.lukatjee.chestshop.sql.sqlGetter;
import eu.lukatjee.chestshop.sql.sqlMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class chestShop extends JavaPlugin {

    public static chestShop plugin;
    public sqlMain SQL;
    public sqlGetter DATA;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            System.out.println("No economy plugin found. Disabling vault.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        this.SQL = new sqlMain();
        this.DATA = new sqlGetter(this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new createShop(), this);
        getServer().getPluginManager().registerEvents(new deleteShop(), this);
        getServer().getPluginManager().registerEvents(new hopperPlacement(), this);
        getServer().getPluginManager().registerEvents(new chestshopInteractions(), this);

        getCommand("chestshop").setExecutor(new mainCommand());

        try {

            this.SQL.connect();

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
            Bukkit.getLogger().info("[ZorionTP] Couldn't connect to the database...");

        }

        if (this.SQL.isConnected()) {

            Bukkit.getLogger().info("[ZorionTP] Successfully connected to the database.");
            DATA.createTable();

        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {

        this.SQL.disconnect();

    }

}
