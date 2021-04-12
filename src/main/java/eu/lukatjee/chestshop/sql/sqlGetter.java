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

            preparedStatement = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS chestshop_db (uuid VARCHAR(45), type VARCHAR(4), world VARCHAR(45), x DECIMAL(65,5), y DECIMAL(65,5), z DECIMAL(65,5), price DECIMAL(65,2), amount INT(45), item VARCHAR(55));");
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

    }

    public void createShop(String uuid, String type, String world, double x, double y, double z, double price, Integer amount, String item) {

        PreparedStatement preparedStatement;

        try {

            preparedStatement = plugin.SQL.getConnection().prepareStatement("INSERT INTO chestshop_db (uuid, type, world, x, y, z, price, amount, item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, world);
            preparedStatement.setDouble(4, x);
            preparedStatement.setDouble(5, y);
            preparedStatement.setDouble(6, z);
            preparedStatement.setDouble(7, price);
            preparedStatement.setInt(8, amount);
            preparedStatement.setString(9, item);

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
