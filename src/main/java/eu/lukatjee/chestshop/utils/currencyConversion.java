package eu.lukatjee.chestshop.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class currencyConversion {

    public String currencyConverter(double price) {

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String priceCurrency = null;

        if (price < 1000) {

            priceCurrency = currencyFormat.format(price);

        } else if (price >= 1000 && price < 1000000) {

            priceCurrency = "$" + decimalFormat.format((price / 1000)) + "K";

        } else if (price >= 1000000 && price < 1000000000) {

            priceCurrency = "$" + decimalFormat.format((price / 1000000)) + "M";

        } else if (price >= 1000000000) {

            priceCurrency = "$" + decimalFormat.format((price / 1000000000)) + "B";

        }

        return priceCurrency;

    }

}
