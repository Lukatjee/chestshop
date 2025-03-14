package eu.lukatjee.chestshop.sql;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class sqlGetter {

    private chestShop plugin;

    public sqlGetter(chestShop plugin) {

        this.plugin = plugin;

    }

    public void createTable() {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS chestshop_db (playerUUID VARCHAR(255), shopType VARCHAR(255), containerType VARCHAR(255), container VARCHAR(255), world VARCHAR(255), x INT(65), y INT(65), z INT(65), price DECIMAL(65,25), amount INT(65), itemType VARCHAR(255), item VARCHAR(255));");
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public void createShop(String playerUUID, String shopType, String containerType, String container, String world, int x, int y, int z, double price, int amount, String itemType, String item) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("INSERT INTO chestshop_db (playerUUID, shopType, containerType, container, world, x, y, z, price, amount, itemType, item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, shopType);
            preparedStatement.setString(3, containerType);
            preparedStatement.setString(4, container);
            preparedStatement.setString(5, world);
            preparedStatement.setInt(6, x);
            preparedStatement.setInt(7, y);
            preparedStatement.setInt(8, z);
            preparedStatement.setDouble(9, price);
            preparedStatement.setInt(10, amount);
            preparedStatement.setString(11, itemType);
            preparedStatement.setString(12, item);

            preparedStatement.execute();


        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public boolean checkShop(String world, int x, int y, int z) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (x) AND ? IN (y) AND ? IN (z);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, z);

            ResultSet result = preparedStatement.executeQuery();

            if (result != null && result.next()) {

                return true;

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;
    }

    UUID playerUUID;
    String shopType;
    String containerType;
    String container;
    double price;
    int amount;
    String itemType;
    String item;
    Location location;

    public void readShop(String world, int x, int y, int z) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (x) AND ? IN (y) AND ? IN (z);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, z);

            ResultSet result = preparedStatement.executeQuery();

            if (result != null && result.next()) {

                playerUUID = UUID.fromString(result.getString("playerUUID"));
                shopType = result.getString("shopType");
                containerType = result.getString("containerType");
                container = result.getString("container");
                price = result.getDouble("price");
                amount = result.getInt("amount");
                itemType = result.getString("itemType");
                item = result.getString("item");

                World worldResult = Bukkit.getServer().getWorld(result.getString("world"));
                double xResult = result.getDouble("x");
                double yResult = result.getDouble("y");
                double zResult = result.getDouble("z");

                location = new Location(worldResult, xResult, yResult, zResult);

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public UUID getPlayerUUID() {

        return playerUUID;

    }

    public String getShopType() {

        return shopType;

    }

    public String getContainerType() {

        return containerType;

    }

    public String getContainer() {

        return container;

    }

    public double getPrice() {

        return price;

    }

    public int getAmount() {

        return amount;

    }

    public String getItemType() {

        return itemType;

    }

    public String getItem() {

        return item;

    }

    public Location getLocation() {

        return location;

    }

    public void removeShop(String world, double x, double y, double z) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("DELETE FROM chestshop_db WHERE ? IN (world) AND ? IN (x) AND ? IN (y) AND ? IN (z);");
            preparedStatement.setString(1, world);
            preparedStatement.setDouble(2, x);
            preparedStatement.setDouble(3, y);
            preparedStatement.setDouble(4, z);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

}
