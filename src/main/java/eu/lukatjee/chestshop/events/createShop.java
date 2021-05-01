package eu.lukatjee.chestshop.events;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import eu.lukatjee.chestshop.utils.messageGetter;
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
    sqlGetter getter = new sqlGetter(chestShop.plugin);

    messageGetter msgGetter = new messageGetter();

    @EventHandler
    public void create(SignChangeEvent event) {

        msgGetter.message();

        Player player = event.getPlayer();
        String createPermission = configuration.getString("createSellPermission");

        if (event.isCancelled()) { return; }

        if (!player.hasPermission(createPermission)) {

            player.sendMessage(msgGetter.getNoPermissionMessage());
            return;

        }

        Block signObject = event.getBlock();
        if (!(signObject.getBlockData() instanceof WallSign)) { return; }

        String sellId = configuration.getString("createSell");
        String buyId = configuration.getString("createBuy");
        String[] signObjectData = event.getLines();
        if (!(signObjectData[0].equals(sellId) || signObjectData[0].equals(buyId)) && !(signObjectData.length >= 2)) {

            player.sendMessage(msgGetter.getInvalidArgumentsMessage());
            return;

        }

        WallSign wallSign = (WallSign) signObject.getBlockData();
        if (!(signObject.getRelative(wallSign.getFacing().getOppositeFace()).getType() == Material.CHEST)) { return; }

        Block chestObject = signObject.getRelative(wallSign.getFacing().getOppositeFace());

        com.bgsoftware.wildchests.api.objects.chests.Chest wildChest = WildChestsAPI.getChest(chestObject.getLocation());
        Chest vanillaChest = (Chest) chestObject.getState();

        ItemStack itemStack = null; boolean isWildChest = false;

        try {

            ChestType tryChest = wildChest.getChestType();
            if (tryChest != null) { isWildChest = true; }

        } catch (NullPointerException ignored) { }

        if (isWildChest && wildChest.getChestType() != ChestType.STORAGE_UNIT) {

            if (wildChest.getItem(0).getType() != Material.AIR) { itemStack = wildChest.getItem(0); isWildChest = true;}

        } else if (isWildChest) {

            if (((StorageChest) wildChest).getItemStack().getType() != Material.AIR) { itemStack = ((StorageChest) wildChest).getItemStack(); isWildChest = true;}

        } else {

            itemStack = vanillaChest.getInventory().getItem(0);

        }

        if (itemStack == null) {
            player.sendMessage(msgGetter.getInvalidItemMessage());
            return;
        }

        String[] dataSecondLine = signObjectData[1].split(" ");
        if (!(dataSecondLine.length >= 2)) {
            player.sendMessage(msgGetter.getInvalidArgumentsMessage());
            return;
        }

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
            String db_shopType = null; String db_containerType; String db_container = null;

            Location containerLocation = chestObject.getLocation();
            Location signLocation = signObject.getLocation(); String db_world = player.getWorld().getName();
            int db_chestX = containerLocation.getBlockX(); int db_chestY = containerLocation.getBlockY(); int db_chestZ = containerLocation.getBlockZ();
            int db_signX = signLocation.getBlockX(); int db_signY = signLocation.getBlockY(); int db_signZ = signLocation.getBlockZ();

            double db_price = shopPrice; int db_amount = shopAmount;
            String db_itemType = "vanilla"; String db_item = itemStack.getType().toString().toLowerCase();

            boolean shopExists = getter.checkShop_chest(db_world, db_chestX, db_chestY, db_chestZ);

            if (signObjectData[0].equals(sellId)) {

                db_shopType = "sell";

            } else if (signObjectData[0].equals(buyId)) {

                db_shopType = "buy";

            }

            if (isWildChest) {

                db_containerType = "wildchest";
                db_container = wildChest.getChestType().toString();

            } else {

                db_containerType = "vanilla";
                int chestSize = vanillaChest.getInventory().getSize();

                switch (chestSize) {

                    case 27:

                        db_container = "SINGLE_CHEST";
                        break;

                    case 54:

                        db_container = "DOUBLE_CHEST";

                }

            }

            if (shopExists) {

                player.sendMessage(msgGetter.getShopExistsMessage());
                return;

            }

            getter.createShop(

                    db_playerUUID, db_shopType, db_containerType, db_container, db_world, db_chestX, db_chestY, db_chestZ, db_signX, db_signY, db_signZ, db_price, db_amount, db_itemType, db_item

            );

            signFormat signFormat = new signFormat();
            signFormat.signFormat(db_shopType, db_price, event.getPlayer());

            String[] signLines = configuration.getStringList("signFormat").toArray(new String[0]);

            event.setLine(0, ChatColor.translateAlternateColorCodes('&', signLines[0].replace("{0}", signFormat.getSignShopType())));
            event.setLine(1, ChatColor.translateAlternateColorCodes('&', signLines[1].replace("{0}", signFormat.getSignPrice()).replace("{1}", String.valueOf(db_amount))));
            event.setLine(2, ChatColor.translateAlternateColorCodes('&', signLines[2].replace("{0}", db_item)));
            event.setLine(3, ChatColor.translateAlternateColorCodes('&', signLines[3].replace("{0}", signFormat.getSignPlayerName())));

            player.sendMessage(msgGetter.getShopCreatedMessage());

        } else if ((shopPrice > maximumPrice) || (shopPrice <= -1)) {

            player.sendMessage(msgGetter.getInvalidPriceMessage());

        } else if ((shopAmount > maximumAmount) || (shopAmount <= 0)) {

            player.sendMessage(msgGetter.getInvalidAmountMessage());

        }

    }

}
