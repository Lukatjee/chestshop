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

            preparedStatement = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS chestshop_db (playerUUID VARCHAR(255), shopType VARCHAR(255), containerType VARCHAR(255), container VARCHAR(255), world VARCHAR(255), chestX INT(65), chestY INT(65), chestZ INT(65), signX INT(65), signY INT(65), signZ INT(65), price DECIMAL(65,25), amount INT(65), itemType VARCHAR(255), item VARCHAR(255));");
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public void createShop(String playerUUID, String shopType, String containerType, String container, String world, int chestX, int chestY, int chestZ, int signX, int signY, int signZ, double price, int amount, String itemType, String item) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("INSERT INTO chestshop_db (playerUUID, shopType, containerType, container, world, chestX, chestY, chestZ, signX, signY, signZ, price, amount, itemType, item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, playerUUID);
            preparedStatement.setString(2, shopType);
            preparedStatement.setString(3, containerType);
            preparedStatement.setString(4, container);
            preparedStatement.setString(5, world);
            preparedStatement.setInt(6, chestX);
            preparedStatement.setInt(7, chestY);
            preparedStatement.setInt(8, chestZ);
            preparedStatement.setInt(9, signX);
            preparedStatement.setInt(10, signY);
            preparedStatement.setInt(11, signZ);
            preparedStatement.setDouble(12, price);
            preparedStatement.setInt(13, amount);
            preparedStatement.setString(14, itemType);
            preparedStatement.setString(15, item);

            preparedStatement.execute();


        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public boolean checkShop_sign(String world, int signX, int signY, int signZ) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (signX) AND ? IN (signY) AND ? IN (signZ);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, signX);
            preparedStatement.setInt(3, signY);
            preparedStatement.setInt(4, signZ);

            ResultSet result = preparedStatement.executeQuery();

            if (result != null && result.next()) {

                return true;

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;
    }

    public boolean checkShop_chest(String world, int chestX, int chestY, int chestZ) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (chestX) AND ? IN (chestY) AND ? IN (chestZ);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, chestX);
            preparedStatement.setInt(3, chestY);
            preparedStatement.setInt(4, chestZ);

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
    Location signLocation;
    Location chestLocation;

    public void readShop_sign(String world, int signX, int signY, int signZ) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (signX) AND ? IN (signY) AND ? IN (signZ);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, signX);
            preparedStatement.setInt(3, signY);
            preparedStatement.setInt(4, signZ);

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
                int chestX_result = result.getInt("chestX");
                int chestY_result = result.getInt("chestY");
                int chestZ_result = result.getInt("chestZ");

                int signX_result = result.getInt("signX");
                int signY_result = result.getInt("signY");
                int signZ_result = result.getInt("signZ");

                signLocation = new Location(worldResult, signX_result, signY_result, signZ_result);
                chestLocation = new Location(worldResult, chestX_result, chestY_result, chestZ_result);

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public void readShop_chest(String world, int chestX, int chestY, int chestZ) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN (world) AND ? IN (chestX) AND ? IN (chestY) AND ? IN (chestZ);");
            preparedStatement.setString(1, world);
            preparedStatement.setInt(2, chestX);
            preparedStatement.setInt(3, chestY);
            preparedStatement.setInt(4, chestZ);

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
                int chestX_result = result.getInt("chestX");
                int chestY_result = result.getInt("chestY");
                int chestZ_result = result.getInt("chestZ");

                int signX_result = result.getInt("signX");
                int signY_result = result.getInt("signY");
                int signZ_result = result.getInt("signZ");

                signLocation = new Location(worldResult, signX_result, signY_result, signZ_result);
                chestLocation = new Location(worldResult, chestX_result, chestY_result, chestZ_result);

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

    public Location getSignLocation() {

        return signLocation;

    }

    public Location getChestLocation() {

        return chestLocation;

    }

    public void removeShop(String world, double signX, double signY, double signZ) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("DELETE FROM chestshop_db WHERE ? IN (world) AND ? IN (signX) AND ? IN (signY) AND ? IN (signZ);");
            preparedStatement.setString(1, world);
            preparedStatement.setDouble(2, signX);
            preparedStatement.setDouble(3, signY);
            preparedStatement.setDouble(4, signZ);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

}
