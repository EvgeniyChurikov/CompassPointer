package me.quelleen.compasspointer;

import me.quelleen.compasspointer.tagManagers.CompassTagManager;
import me.quelleen.compasspointer.tagManagers.PlayerTagManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    private final KeyManager keyManager;
    private final Plugin plugin;

    public EventListener(KeyManager keyManager, Plugin plugin) {
        this.keyManager = keyManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
        PlayerTagManager playerTagManager = new PlayerTagManager(this.keyManager, container);
        if (!playerTagManager.hasTrail())
            playerTagManager.createTrail();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> compasses = new ArrayList<>();
        for (ItemStack item: event.getDrops()) {
            if (item.getType() == Material.COMPASS) {
                CompassMeta meta = (CompassMeta) item.getItemMeta();
                assert meta != null;
                CompassTagManager tagManager = new CompassTagManager(
                        this.keyManager, meta.getPersistentDataContainer());
                if (tagManager.hasWatchedPlayer()) {
                    compasses.add(item);
                    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        event.getEntity().getInventory().addItem(item);
                    });
                }
            }
        }
        event.getDrops().removeAll(compasses);
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.COMPASS
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            CompassMeta meta = (CompassMeta) item.getItemMeta();
            assert meta != null;
            CompassTagManager tagManager = new CompassTagManager(
                    this.keyManager, meta.getPersistentDataContainer());
            if (tagManager.hasWatchedPlayer()) {
                OfflinePlayer watchedPlayer = event.getPlayer().getServer().getOfflinePlayer(tagManager.getWatchedPlayer());
                if (watchedPlayer.isOnline()) {
                    PersistentDataContainer playerContainer = ((Player) watchedPlayer).getPersistentDataContainer();
                    PlayerTagManager playerTagManager = new PlayerTagManager(this.keyManager, playerContainer);
                    Location watchedPlayerLocation = playerTagManager.getLocation(event.getPlayer().getWorld());
                    if (watchedPlayerLocation != null) {
                        int distance = (int) event.getPlayer().getLocation().distance(watchedPlayerLocation);
                        event.getPlayer().sendMessage("Watched player: " + watchedPlayer.getName()
                                + ". Distance to him: " + distance);
                    }
                    else {
                        event.getPlayer().sendMessage("Watched player: " + watchedPlayer.getName()
                                + ". Distance to him is undefined.");
                    }
                }
                else {
                    event.getPlayer().sendMessage("Watched player: " + watchedPlayer.getName()
                            + ". Player is offline. The compass points to the player only when he is online.");
                }
            }
        }
    }

}
