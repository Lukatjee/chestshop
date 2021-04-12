package eu.lukatjee.chestshop;

import eu.lukatjee.chestshop.commands.mainCommand;
import eu.lukatjee.chestshop.events.createShop;
import eu.lukatjee.chestshop.sql.sqlMain;
import eu.lukatjee.chestshop.sql.sqlGetter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class chestShop extends JavaPlugin {

    public static chestShop plugin;
    public sqlMain SQL;
    public sqlGetter DATA;

    @Override
    public void onEnable() {

        plugin = this;
        this.SQL = new sqlMain();
        this.DATA = new sqlGetter(this);

        /*

            SQL

         */

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

        /*

            Configuration

         */

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        /*

            Events

         */

        getServer().getPluginManager().registerEvents(new createShop(), this);

        /*

            Commands

         */

        getCommand("chestshop").setExecutor(new mainCommand());

    }

    @Override
    public void onDisable() {

        this.SQL.disconnect();

    }

}
