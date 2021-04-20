package eu.lukatjee.chestshop.events;

import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class hopperPlacement implements Listener {

    FileConfiguration configuration = chestShop.plugin.getConfig();

    @EventHandler
    public void hopperPlacement(BlockPlaceEvent event) {

        String noHopperMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("noHopper"));

        if (event.isCancelled()) {

            return;

        }

        sqlGetter getter = new sqlGetter(chestShop.plugin);

        Block blockPlaced = event.getBlockPlaced();
        Player player = event.getPlayer();

        if (blockPlaced.getType() == Material.HOPPER) {

            Location blockPlacedLocation = blockPlaced.getLocation();
            World world = blockPlacedLocation.getWorld();
            int x = blockPlacedLocation.getBlockX();
            int y = blockPlacedLocation.getBlockY() + 1;
            int z = blockPlacedLocation.getBlockZ();

            Location blockOnTopLocation = new Location(world, x, y, z);
            Block blockOnTop = blockOnTopLocation.getBlock();

            if (blockOnTop.getType() == Material.CHEST) {

                String worldName = world.getName();
                boolean isChestShop = getter.checkShop(worldName, x, y, z);

                if (!isChestShop) {

                    return;

                }

                player.sendMessage(noHopperMessage);
                event.setCancelled(true);

            }

        }

    }

}
