package eu.lukatjee.chestshop.events;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import eu.lukatjee.chestshop.chestShop;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


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
            String invalidItemMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidItem"));
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

                String createSellId = configuration.getString("createSell");
                String createBuyId = configuration.getString("createBuy");

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

                            boolean wildChestCheck = false;
                            boolean chest = false;

                            com.bgsoftware.wildchests.api.objects.chests.Chest wildChest = WildChestsAPI.getChest(attachedObject.getLocation());
                            ChestType wildChestType = null;
                            Chest vanillaChest = null;

                            ItemStack itemStack;

                            /*
                             *
                             *  Once everything has been checked, the code will continue to gather info on the block
                             *  the sign is attached to, this will be done through the Wildchest API, this is a great
                             *  API that handles a lot of chest related functionalities. Though, the regular chest handling
                             *  is still present aswell as it's necessary for vanilla chests.
                             *
                             *  Though this is a great resource, it will not assign the normal ChestType to a vanilla chest,
                             *  hence a try-statement will collect data and check for vanilla minecraft chests and assign
                             *  the proper value, this may as well be null if no chest can be found.
                             *
                             */

                            try {

                                wildChestType = wildChest.getChestType();

                                wildChestCheck = true;
                                chest = true;

                            } catch (NullPointerException exception) {

                                vanillaChest = (Chest) attachedObject.getState();

                                chest = true;

                            }

                            try {

                                itemStack = vanillaChest.getInventory().getItem(0);

                            } catch (NullPointerException exception) {

                                itemStack = null;

                            }

                            if (chest) {

                                if (itemStack != null) {

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

                                        } catch (NumberFormatException ignored) {
                                        }

                                        try {

                                            shopAmount = Integer.parseInt(dataSecondLine[1]);

                                        } catch (NumberFormatException ignored) {
                                        }

                                        double maximumPrice = configuration.getDouble("maxPrice");
                                        int maximumAmount = configuration.getInt("maxAmount");

                                        if (shopPrice > -1 && shopAmount > 0) {

                                            if (shopPrice <= maximumPrice && shopAmount <= maximumAmount) {

                                                UUID db_playerUUID = player.getUniqueId();
                                                String db_shopTypeID = null;

                                                if (signObjectData[0].equals(createSellId)) {

                                                    db_shopTypeID = "sell";

                                                } else if (signObjectData[0].equals(createBuyId)) {

                                                    db_shopTypeID = "buy";

                                                }

                                                String db_containerType;
                                                String db_item;

                                                if (wildChestCheck) {

                                                    db_containerType = "wildchest";

                                                    if (wildChestType == ChestType.STORAGE_UNIT) {

                                                        db_item = ((StorageChest) wildChest).getItemStack().getType().toString().toLowerCase();

                                                    } else {

                                                        db_item = wildChest.getItem(0).getType().toString().toLowerCase();

                                                    }

                                                } else {

                                                    db_containerType = "vanilla";
                                                    db_item = vanillaChest.getInventory().getItem(0).getType().toString().toLowerCase();

                                                }

                                                String db_container = null;

                                                if (wildChestCheck) {

                                                    if (wildChestType == ChestType.STORAGE_UNIT) {

                                                        db_container = "storage_chest";

                                                    } else if (wildChestType == ChestType.LINKED_CHEST) {

                                                        db_container = "linked_chest";

                                                    } else if (wildChestType == ChestType.CHEST) {

                                                        db_container = "large_chest";

                                                    }

                                                } else {

                                                    int chestSize = vanillaChest.getInventory().getSize();

                                                    if (chestSize == 27) {

                                                        db_container = "single_chest";

                                                    } else if (chestSize == 54) {

                                                        db_container = "double_chest";

                                                    }

                                                }

                                                Location location = attachedObject.getLocation();

                                                String db_world = player.getWorld().getName();
                                                double db_x = location.getBlockX();
                                                double db_y = location.getBlockY();
                                                double db_z = location.getBlockZ();

                                                double db_price = shopPrice;
                                                int db_amount = shopAmount;

                                                String db_itemType = "vanilla";

                                                player.sendMessage(db_playerUUID + "\n" + db_shopTypeID + "\n" + db_containerType + "\n" + db_container + "\n" + db_world + "\n" + db_x + "\n" + db_y + "\n" + db_z + "\n" + db_price + "\n" + db_amount + "\n" + db_itemType + "\n" + db_item);

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

                                    player.sendMessage(invalidItemMessage);

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