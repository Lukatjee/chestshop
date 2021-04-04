package eu.lukatjee.chestshop.events;

import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class createSellShop implements Listener {

    @EventHandler
    public void onCreateSign(SignChangeEvent event) {

        FileConfiguration configuration = chestShop.plugin.getConfig();

        if (!event.isCancelled()) {

            Player player = event.getPlayer();
            String createSellPermission = configuration.getString("createSellPermission");

            if (player.hasPermission(createSellPermission)) {

                /*

                    Initialize the sign so it can be used to get the block it's attached to,
                    it then checks whether the block is a chest to be able to continue.

                 */

                Sign sign = (Sign) event.getBlock().getState();
                Block attachedBlock = sign.getBlock().getRelative(((Directional) sign.getBlockData()).getFacing().getOppositeFace());

                if (attachedBlock.getState() instanceof Chest) {

                    Chest chest = (Chest) attachedBlock.getState();
                    String createSell = configuration.getString("createSell");

                    if (event.getLine(0).equals(createSell)) {

                        String[] shopData = event.getLine(1).split(" ");

                        if (shopData.length == 2) {

                            double price;
                            int amount;

                            /*

                                Try to assign a number to the variables declared before, if they
                                are not numbers, they will be set to values that the plugin will
                                ignore and cancel the creation of a chestshop with a message.

                            */

                            try {

                                price = Double.parseDouble(shopData[0]);

                            } catch (NumberFormatException except) {

                                price = -1;

                            }

                            try {

                                amount = Integer.parseInt(shopData[1]);

                            } catch (NumberFormatException except) {

                                amount = 0;

                            }

                            /*

                                Checks whether the price and amount are smaller than a certain amount
                                set in the configuration of the plugin, or if they are invalid values.

                            */

                            double maxPrice = configuration.getInt("maxPrice");
                            int maxAmount = configuration.getInt("maxAmount");

                            if (price <= maxPrice && price != -1) {

                                if (amount <= maxAmount && amount != 0) {

                                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    String priceCurrency = null;

                                    /*

                                        Convert price format to a value with K, M or B to avoid large numbers.

                                     */

                                    if (price < 1000) {

                                        priceCurrency = currencyFormat.format(price);

                                    } else if (price >= 1000 && price < 1000000) {

                                        priceCurrency = "$" + decimalFormat.format((price / 1000)) + "K";

                                    } else if (price >= 1000000 && price < 1000000000) {

                                        priceCurrency = "$" + decimalFormat.format((price / 1000000)) + "M";

                                    } else if (price >= 1000000000) {

                                        priceCurrency = "$" + decimalFormat.format((price / 1000000000)) + "B";

                                    }

                                    String item;

                                    /*

                                        Try to get the item in the first slot of the chestshop,
                                        if this value is invalid it will assign a null value.

                                     */

                                    try {

                                        item = chest.getInventory().getItem(0).getType().toString().toLowerCase().replace("_", " ");

                                    } catch (NullPointerException except) {

                                        item = null;

                                    }

                                    if (item != null) {

                                        String[] signLines = configuration.getStringList("signFormat").toArray(new String[0]);

                                        String shopType = configuration.getString("sell");
                                        String playerName = player.getName();

                                        event.setLine(0, ChatColor.translateAlternateColorCodes('&', signLines[0].replace("{0}", shopType)));
                                        event.setLine(1, ChatColor.translateAlternateColorCodes('&', signLines[1].replace("{0}", priceCurrency).replace("{1}", String.valueOf(amount))));
                                        event.setLine(2, ChatColor.translateAlternateColorCodes('&', signLines[2].replace("{0}", item)));
                                        event.setLine(3, ChatColor.translateAlternateColorCodes('&', signLines[3].replace("{0}", playerName)));

                                    /*

                                        Every message regarding an incomplete or invalid player input.
                                        These can be customized in the configuration of this plugin.

                                     */

                                    } else {

                                        String invalidItem = configuration.getString("invalidItem");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidItem));

                                    }

                                } else {

                                    String invalidAmount = configuration.getString("invalidAmount");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidAmount));

                                }

                            } else {

                                String invalidPrice = configuration.getString("invalidPrice");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidPrice));

                            }

                        } else {

                            String missingArguments = configuration.getString("missingArguments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', missingArguments));

                        }

                    }

                }

            }

        }

    }

}