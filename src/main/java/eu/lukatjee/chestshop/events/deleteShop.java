package eu.lukatjee.chestshop.events;

import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class deleteShop implements Listener {

    FileConfiguration configuration = chestShop.plugin.getConfig();

    @EventHandler
    public void delete(BlockBreakEvent event) {

        Player player = event.getPlayer();
        String deletePermission = configuration.getString("deletePermission");
        String adminDeletePermission = configuration.getString("adminDeletePermission");

        String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("noPermission"));
        String shopOwnershipMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopOwnership"));
        String shopRemovedMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("shopRemoved"));

        sqlGetter getter = new sqlGetter(chestShop.plugin);

        /*
         *
         *  Get the sign object and the object it is attached to, if the attached object
         *  is a chest the plugin will continue the process to create a shop.
         *
         */

        if (!event.isCancelled()) {

            Block signObject = event.getBlock();

            if (signObject.getBlockData() instanceof WallSign) {

                WallSign wallSign = (WallSign) signObject.getBlockData();

                if (signObject.getRelative(wallSign.getFacing().getOppositeFace()).getType() == Material.CHEST) {

                    /*
                     *
                     *  Once everything has been checked, the code will continue to gather info on
                     *  the block and check if it's a chestshop so it can be removed if the player
                     *  is allowed to do so.
                     *
                     */

                    Block chestObject = signObject.getRelative(wallSign.getFacing().getOppositeFace());
                    Location chestObjectLocation = chestObject.getLocation();

                    String chestObjectWorld = player.getWorld().getName();
                    int chestObjectX = chestObjectLocation.getBlockX();
                    int chestObjectY = chestObjectLocation.getBlockY();
                    int chestObjectZ = chestObjectLocation.getBlockZ();

                    boolean isChestShop = getter.checkShop(chestObjectWorld, chestObjectX, chestObjectY, chestObjectZ);

                    if (isChestShop) {

                        if (player.hasPermission(deletePermission)) {

                            getter.readShop(chestObjectWorld, chestObjectX, chestObjectY, chestObjectZ);
                            UUID db_playerUUID = getter.getPlayerUUID();

                            if (db_playerUUID.equals(player.getUniqueId()) || player.hasPermission(adminDeletePermission)) {

                                getter.removeShop(chestObjectWorld, chestObjectX, chestObjectY, chestObjectZ);
                                player.sendMessage(shopRemovedMessage);

                            } else {

                                player.sendMessage(shopOwnershipMessage);
                                event.setCancelled(true);

                            }

                        } else {

                            player.sendMessage(noPermissionMessage);
                            event.setCancelled(true);

                        }

                    }

                }

            }

        }

    }

}
