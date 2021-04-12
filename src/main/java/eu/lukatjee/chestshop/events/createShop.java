package eu.lukatjee.chestshop.events;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.Chest;
import com.bgsoftware.wildchests.api.objects.chests.LinkedChest;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


public class createShop implements Listener {

    FileConfiguration configuration = chestShop.plugin.getConfig();

    @EventHandler
    public void create(SignChangeEvent event) {

        Player player = event.getPlayer();
        String createPermission = configuration.getString("createSellPermission");

        if (!event.isCancelled()) {

            String invalidPriceMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidPrice"));
            String invalidAmountMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidAmount"));
            String invalidArgumentsMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidArguments"));
            String invalidContainerMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidContainer"));

            if (player.hasPermission(createPermission)) {

                /*
                 *
                 *   Get the sign object and it's data, though there is one small problem.
                 *   I cannot directly check for a wall sign object, thus it will still
                 *   gather data, even if the sign is not attached to a block.
                 *
                 *   Please don't worry though, this little problem is solved later on
                 *   throughout the code.
                 *
                 */

                Sign signObject = (Sign) event.getBlock().getState();
                String[] signObjectData = event.getLines();

                String createSellId = configuration.getString("createBuy");
                String createBuyId = configuration.getString("createSell");

                /*
                 *
                 *  Check if the sign that's being created contains the shop type identifiers
                 *  If this is the case, the plugin will check if there is a Directional variable
                 *  that can be gathered.
                 *
                 *  If there's a ClassCastException, meaning the sign is not a wall sign, the value
                 *  will be nullified, hence rendering the plugin unable to continue due to a specific
                 *  null-check.
                 *
                 */

                if (signObjectData[0].equals(createSellId) || signObjectData[0].equals(createBuyId)) {

                    if (signObjectData.length >= 2) {

                        Block attachedObject;

                        try {

                            attachedObject = signObject.getBlock().getRelative(((Directional) signObject.getBlockData()).getFacing().getOppositeFace());

                        } catch (ClassCastException exception) {

                            attachedObject = null;

                        }

                        if (attachedObject != null) {

                            Chest chest = WildChestsAPI.getChest(attachedObject.getLocation());
                            ChestType chestType;

                            /*
                             *
                             *  Once everything has been checked, the code will continue to gather info on the block
                             *  the sign is attached to, this will be done through the Wildchest API, this is a great
                             *  API that handles a lot of chest related functionalities.
                             *
                             *  Though this is a great resource, it will not assign the normal ChestType to a vanilla chest,
                             *  hence a try-statement will collect data and check for vanilla minecraft chests and assign
                             *  the proper value, this may as well be null if no chest can be found.
                             *
                             */

                            try {

                                chestType = chest.getChestType();

                            } catch (NullPointerException exception) {

                                if (attachedObject.getType() == Material.CHEST) {

                                    chestType = ChestType.CHEST;

                                } else {

                                    chestType = null;

                                }

                            }

                            if (chestType != null) {

                                String[] dataSecondLine = signObjectData[1].split(" ");
                                double shopPrice = -1;
                                int shopAmount = 0;

                                if (dataSecondLine.length == 2) {

                                    /*
                                     *
                                     *  Once it's been confirmed that all required arguments have been given (and nothing
                                     *  more), the plugin will continue to gather the data and check if it's valid. After
                                     *  that it'll check what to do regarding different chest types.
                                     *
                                     */

                                    try {

                                        shopPrice = Double.parseDouble(dataSecondLine[0]);

                                    } catch (NumberFormatException ignored) { }

                                    try {

                                        shopAmount = Integer.parseInt(dataSecondLine[1]);

                                    } catch (NumberFormatException ignored) { }

                                    double maximumPrice = configuration.getDouble("maxPrice");
                                    int maximumAmount = configuration.getInt("maxAmount");

                                    if (shopPrice > -1 && shopAmount > 0) {

                                        if (shopPrice <= maximumPrice && shopAmount <= maximumAmount) {

                                            if (chestType == ChestType.STORAGE_UNIT) {

                                                StorageChest storageChest = (StorageChest) chest;

                                            } else if (chestType == ChestType.LINKED_CHEST) {

                                                LinkedChest linkedChest = (LinkedChest) chest;

                                            }

                                        } else if (shopPrice > maximumPrice) {

                                            player.sendMessage(invalidPriceMessage);

                                        } else if (shopAmount > maximumAmount) {

                                            player.sendMessage(invalidAmountMessage);

                                        }

                                    } else if (shopPrice <= -1 || shopPrice > maximumPrice) {

                                        player.sendMessage(invalidPriceMessage);

                                    } else if (shopAmount <= 0 || shopAmount > maximumAmount) {

                                        player.sendMessage(invalidAmountMessage);

                                    }

                                } else if (dataSecondLine.length < 2) {

                                    player.sendMessage(invalidArgumentsMessage);

                                } else if (dataSecondLine.length > 2) {

                                    player.sendMessage(invalidArgumentsMessage);

                                }

                            } else {

                                player.sendMessage(invalidContainerMessage);

                            }

                        }

                    }

                }

            }

        }

    }

}