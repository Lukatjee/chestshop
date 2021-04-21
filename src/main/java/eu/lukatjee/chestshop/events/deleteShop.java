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

        if (!event.isCancelled()) {

            Block brokenBlock = event.getBlock();
            String blockObject = null;

            Location location = brokenBlock.getLocation();

            String locationWorldName = location.getWorld().getName();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            if (brokenBlock.getBlockData() instanceof WallSign) {

                boolean isChestShop = getter.checkShop_sign(locationWorldName, x, y, z);
                if (isChestShop) { blockObject = "sign"; }

            } else if (brokenBlock.getType().equals(Material.CHEST)) {

                boolean isChestShop = getter.checkShop_chest(locationWorldName, x, y, z);
                if (isChestShop) { blockObject = "chest"; }

            }

            if (!player.hasPermission(deletePermission)) {

                player.sendMessage(noPermissionMessage);
                return;

            }

            if (blockObject == null) { return; }
            switch (blockObject) {

                case "sign":

                    getter.readShop_sign(locationWorldName, x, y, z);
                    break;

                case "chest":

                    getter.readShop_chest(locationWorldName, x, y, z);
                    break;

            }

            if (getter.getPlayerUUID().equals(player.getUniqueId()) || player.hasPermission(adminDeletePermission)) {

                getter.removeShop(locationWorldName, x, y, z);
                player.sendMessage(shopRemovedMessage);

            } else {

                player.sendMessage(shopOwnershipMessage);
                event.setCancelled(true);

            }

        }

    }

}
