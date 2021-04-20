package eu.lukatjee.chestshop.utils;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class signFormat {

    String signShopType;
    String signPrice;
    String signPlayerName;

    public void signFormat(String shopType, double price, Player player) {

        if (shopType.equals("sell")) {

            signShopType = "Buy";

        } else if (shopType.equals("buy")) {

            signShopType = "Sell";

        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        if (price < 1000) {

            signPrice = currencyFormat.format(price);

        } else if (price >= 1000 && price < 1000000) {

            signPrice = "$" + decimalFormat.format((price / 1000)) + "K";

        } else if (price >= 1000000 && price < 1000000000) {

            signPrice = "$" + decimalFormat.format((price / 1000000)) + "M";

        } else if (price >= 1000000000) {

            signPrice = "$" + decimalFormat.format((price / 1000000000)) + "B";

        }

        signPlayerName = player.getName();

    }

    public String getSignShopType() {

        return signShopType;

    }

    public String getSignPrice() {

        return signPrice;

    }

    public String getSignPlayerName() {

        return signPlayerName;

    }

}
