package eu.lukatjee.chestshop.utils;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class messageGetter {

    FileConfiguration configuration = chestShop.plugin.getConfig();
    String noPermissionMessage;
    String invalidPriceMessage;
    String invalidAmountMessage;
    String invalidArgumentsMessage;
    String invalidItemMessage;
    String shopExistsMessage;
    String shopCreatedMessage;

    public void message() {

        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("noPermission"));
        invalidPriceMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidPrice"));
        invalidAmountMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidAmount"));
        invalidArgumentsMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidArguments"));
        invalidItemMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidItem"));
        shopExistsMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopExists"));
        shopCreatedMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopCreated"));

    }

    public String getNoPermissionMessage() {

        return noPermissionMessage;

    }

    public String getInvalidPriceMessage() {

        return invalidPriceMessage;

    }

    public String getInvalidAmountMessage() {

        return invalidAmountMessage;

    }

    public String getInvalidArgumentsMessage() {

        return invalidArgumentsMessage;

    }

    public String getInvalidItemMessage() {

        return invalidItemMessage;

    }

    public String getShopExistsMessage() {

        return shopExistsMessage;

    }

    public String getShopCreatedMessage() {

        return shopCreatedMessage;

    }

}
