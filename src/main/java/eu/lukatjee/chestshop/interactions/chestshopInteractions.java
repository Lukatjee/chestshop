package eu.lukatjee.chestshop.interactions;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.ChestType;
import com.bgsoftware.wildchests.api.objects.chests.LinkedChest;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.math.BigInteger;
import java.util.UUID;

public class chestshopInteractions implements Listener {

    sqlGetter getter = new sqlGetter(chestShop.plugin);

    @EventHandler
    public void chestshopInteraction(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player player = event.getPlayer();

        if (!(action == Action.RIGHT_CLICK_BLOCK)) {

            return;

        }

        Block clickedBlock = event.getClickedBlock();
        String blockObject = null;

        Location location = clickedBlock.getLocation();

        String locationWorldName = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        if (clickedBlock.getBlockData() instanceof WallSign) {

            boolean isChestShop = getter.checkShop_sign(locationWorldName, x, y, z);
            if (isChestShop) { blockObject = "sign"; }

        } else if (clickedBlock.getType().equals(Material.CHEST)) {

            boolean isChestShop = getter.checkShop_chest(locationWorldName, x, y, z);
            if (isChestShop) { blockObject = "chest"; }

        }

        if (blockObject == null) {

            return;

        }

        if (blockObject.equals("sign")) {

            getter.readShop_sign(locationWorldName, x, y, z);

            if (getter.getPlayerUUID().equals(player.getUniqueId())) {

                player.sendMessage("You are actually shtewpid");

            } else {

                Economy economy = chestShop.getEconomy();
                double price = getter.getPrice();

                UUID vendorUUID = getter.getPlayerUUID();
                Player vendor = Bukkit.getPlayer(vendorUUID);

                if (economy.has(player, price)) {

                    String container = getter.getContainer();
                    String containerType = getter.getContainerType();

                    Location locationChest = getter.getChestLocation();
                    int amount = getter.getAmount();

                    if (containerType.equals("vanilla")) {

                        Chest chestObject = (Chest) locationChest.getBlock().getState();
                        Inventory chestInventory = chestObject.getInventory();

                    } else if (containerType.equals("wildchest")) {

                        com.bgsoftware.wildchests.api.objects.chests.Chest wildChest = WildChestsAPI.getChest(getter.getChestLocation());
                        ChestType wildChestType = wildChest.getChestType();

                        if (wildChestType == ChestType.STORAGE_UNIT) {

                            StorageChest storageChest = (StorageChest) wildChest;
                            BigInteger wildChestAmount = storageChest.getAmount();

                        } else if (wildChestType == ChestType.LINKED_CHEST) {

                            LinkedChest linkedChest = (LinkedChest) wildChest;

                        }

                    }

                    economy.withdrawPlayer(player, price);
                    economy.depositPlayer(vendor, price);

                }

            }

        } else if (blockObject.equals("chest")) {

            getter.readShop_chest(locationWorldName, x, y, z);

            if (!getter.getPlayerUUID().equals(player.getUniqueId())) {

                player.sendMessage("Sorry, you have no access to this chestshop.");
                event.setCancelled(true);

            }

        }

    }

}
