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

    public boolean readShop(String world, double x, double y, double z) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM chestshop_db WHERE ? IN world AND ? IN x AND ? IN y AND ? IN z;");
            preparedStatement.setString(1, world);
            preparedStatement.setDouble(2, x);
            preparedStatement.setDouble(3, y);
            preparedStatement.setDouble(4, z);

            ResultSet result = preparedStatement.executeQuery();

            if (result != null && result.next()) {

                UUID uuidResult = UUID.fromString(result.getString("uuid"));
                String type = result.getString("type");
                double price = result.getDouble("price");
                Integer amount = result.getInt("amount");
                String item = result.getString("item");

                World worldResult = Bukkit.getServer().getWorld(result.getString("world"));
                double xResult = result.getDouble("x");
                double yResult = result.getDouble("y");
                double zResult = result.getDouble("z");

                Location location = new Location(worldResult, xResult, yResult, zResult);

            } else {

                return false;

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;
    }

    public void removeWarp(String world, double x, double y, double z) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("DELETE FROM zoriontp_db WHERE ? IN world AND ? IN x AND ? IN y AND ? IN z;");
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
