package eu.lukatjee.chestshop.events;

import eu.lukatjee.chestshop.chestShop;
import eu.lukatjee.chestshop.sql.sqlGetter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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

        } else if (blockObject.equals("sign")) {

            getter.readShop_sign(locationWorldName, x, y, z);

            if (getter.getPlayerUUID().equals(player.getUniqueId())) {

                player.sendMessage("Sorry, can't purchase from your own shop.");

            } else {

                player.sendMessage("Ehh, be patient, you can buy items soon.");

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
