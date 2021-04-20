package eu.lukatjee.chestshop.events;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import eu.lukatjee.chestshop.utils.signFormat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;


public class createShop implements Listener {

    FileConfiguration configuration = chestShop.plugin.getConfig();

    @EventHandler
    public void create(SignChangeEvent event) {

        Player player = event.getPlayer();
        String createPermission = configuration.getString("createSellPermission");

        if (!event.isCancelled()) {

            String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("noPermission"));
            String invalidPriceMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidPrice"));
            String invalidAmountMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidAmount"));
            String invalidArgumentsMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidArguments"));
            String invalidItemMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("invalidItem"));
            String shopExistsMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopExists"));
            String shopCreatedMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopCreated"));

            String createSellId = configuration.getString("createSell");
            String createBuyId = configuration.getString("createBuy");

            sqlGetter getter = new sqlGetter(chestShop.plugin);

            if (player.hasPermission(createPermission)) {

                /*
                 *
                 *  First I'll check if the sign is a wall sign, after that I'll check
                 *  if the block it's attached to equals the chest material type.
                 *
                 */

                Block signObject = event.getBlock();

                if (signObject.getBlockData() instanceof WallSign) {

                    WallSign wallSign = (WallSign) signObject.getBlockData();

                    if (signObject.getRelative(wallSign.getFacing().getOppositeFace()).getType() == Material.CHEST) {

                        String[] signObjectData = event.getLines();

                        if (signObjectData[0].equals(createSellId) || signObjectData[0].equals(createBuyId)) {

                            if (signObjectData.length >= 2) {

                                Block chestObject = signObject.getRelative(wallSign.getFacing().getOppositeFace());

                                /*
                                 *
                                 *  This plugin will check what kind of chest we're dealing
                                 *  with as this plugin offers compatibility for wildchests.
                                 *
                                 */

                                com.bgsoftware.wildchests.api.objects.chests.Chest wildChest = WildChestsAPI.getChest(chestObject.getLocation());
                                boolean wildChestBoolean = false;
                                ChestType wildChestType = null;
                                ItemStack itemStack = null;
                                Chest vanillaChest = null;

                                try {

                                    wildChestType = wildChest.getChestType();
                                    wildChestBoolean = true;

                                    if (wildChestType == ChestType.STORAGE_UNIT) {

                                        if (((StorageChest) wildChest).getItemStack().getType() != Material.AIR) {

                                            itemStack = ((StorageChest) wildChest).getItemStack();

                                        }

                                    } else {

                                        if (wildChest.getItem(0).getType() != Material.AIR) {

                                            itemStack = wildChest.getItem(0);

                                        }

                                    }

                                } catch (NullPointerException exception) {

                                    vanillaChest = (Chest) chestObject.getState();
                                    itemStack = vanillaChest.getInventory().getItem(0);

                                }

                                if (itemStack == null) {

                                    player.sendMessage(invalidItemMessage);
                                    return;

                                }

                                String[] dataSecondLine = signObjectData[1].split(" ");

                                /*
                                 *
                                 *  First, the plugin will check if the given arguments are correct to proceed
                                 *  gathering and entering data in the database if the shop does not exist yet.
                                 *
                                 */

                                if (dataSecondLine.length == 2) {

                                    double shopPrice = -1;
                                    int shopAmount = 0;
                                    double maximumPrice = configuration.getDouble("maxPrice");
                                    int maximumAmount = configuration.getInt("maxAmount");

                                    try {

                                        shopPrice = Double.parseDouble(dataSecondLine[0]);
                                        shopAmount = Integer.parseInt(dataSecondLine[1]);

                                    } catch (NumberFormatException exception) {

                                        // Pyro tells you to get over it

                                    }

                                    if ((shopPrice > -1 && shopAmount > 0) && (shopPrice <= maximumPrice && shopAmount <= maximumAmount)) {

                                        String db_playerUUID = player.getUniqueId().toString();
                                        String db_shopType = null;
                                        String db_containerType;
                                        String db_item = itemStack.getType().toString().toLowerCase();
                                        String db_container = null;
                                        Location location = chestObject.getLocation();
                                        String db_world = player.getWorld().getName();
                                        int db_x = location.getBlockX();
                                        int db_y = location.getBlockY();
                                        int db_z = location.getBlockZ();
                                        double db_price = shopPrice;
                                        int db_amount = shopAmount;
                                        String db_itemType = "vanilla";
                                        boolean shopExists = getter.checkShop(db_world, db_x, db_y, db_z);

                                        if (signObjectData[0].equals(createSellId)) {

                                            db_shopType = "sell";

                                        } else if (signObjectData[0].equals(createBuyId)) {

                                            db_shopType = "buy";

                                        }

                                        if (wildChestBoolean) {

                                            db_containerType = "wildchest";

                                            switch (wildChestType) {

                                                case STORAGE_UNIT:

                                                    db_container = "storage_chest";
                                                    break;

                                                case LINKED_CHEST:

                                                    db_container = "linked_chest";
                                                    break;

                                                case CHEST:

                                                    db_container = "large_chest";

                                            }

                                        } else {

                                            db_containerType = "vanilla";
                                            int chestSize = vanillaChest.getInventory().getSize();

                                            switch (chestSize) {

                                                case 27:

                                                    db_container = "single_chest";
                                                    break;

                                                case 54:

                                                    db_container = "double_chest";

                                            }

                                        }

                                        if (shopExists) {

                                            player.sendMessage(shopExistsMessage);
                                            return;

                                        }

                                        getter.createShop(

                                                db_playerUUID, db_shopType, db_containerType, db_container, db_world, db_x, db_y, db_z, db_price, db_amount, db_itemType, db_item

                                        );

                                        signFormat signFormat = new signFormat();
                                        signFormat.signFormat(db_shopType, db_price, event.getPlayer());

                                        String[] signLines = configuration.getStringList("signFormat").toArray(new String[0]);

                                        event.setLine(0, ChatColor.translateAlternateColorCodes('&', signLines[0].replace("{0}", signFormat.getSignShopType())));
                                        event.setLine(1, ChatColor.translateAlternateColorCodes('&', signLines[1].replace("{0}", signFormat.getSignPrice()).replace("{1}", String.valueOf(db_amount))));
                                        event.setLine(2, ChatColor.translateAlternateColorCodes('&', signLines[2].replace("{0}", db_item)));
                                        event.setLine(3, ChatColor.translateAlternateColorCodes('&', signLines[3].replace("{0}", signFormat.getSignPlayerName())));

                                        player.sendMessage(shopCreatedMessage);

                                    } else if ((shopPrice > maximumPrice) || (shopPrice <= -1)) {

                                        player.sendMessage(invalidPriceMessage);

                                    } else if ((shopAmount > maximumAmount) || (shopAmount <= 0)) {

                                        player.sendMessage(invalidAmountMessage);

                                    }

                                } else {

                                    player.sendMessage(invalidArgumentsMessage);

                                }

                            } else {

                                player.sendMessage(invalidArgumentsMessage);

                            }

                        }

                    }

                }

            } else {

                player.sendMessage(noPermissionMessage);

            }

        }

    }

}