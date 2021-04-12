package eu.lukatjee.chestshop.sql;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlMain {

    public static sqlMain SQL;

    FileConfiguration configuration = chestShop.plugin.getConfig();

    private String host = configuration.getString("host");
    private String port = configuration.getString("port");
    private String database = configuration.getString("database");
    private String username = configuration.getString("username");
    private String password = configuration.getString("password");

    private String arguments = configuration.getString("arguments");

    private Connection connection;

    public boolean isConnected() {

        return (connection != null);

    }

    public void connect() throws ClassNotFoundException, SQLException {

        if (!isConnected()) {

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?" + arguments, username, password);

        }

    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

    }

    public Connection getConnection() {

        return connection;

    }

}
