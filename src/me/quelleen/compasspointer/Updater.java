package me.quelleen.compasspointer;

import me.quelleen.compasspointer.tagManagers.CompassTagManager;
import me.quelleen.compasspointer.tagManagers.PlayerTagManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class Updater {

    private final KeyManager keyManager;
    private final Plugin plugin;
    private int taskId;

    public Updater(KeyManager keyManager, Plugin plugin) {
        this.keyManager = keyManager;
        this.plugin = plugin;
        this.taskId = -1;
    }

    public void start() {
        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            this.updateTrails();
            this.updateCompasses();
        }, 0L, 20L);
    }

    public void stop() {
        this.plugin.getServer().getScheduler().cancelTask(this.taskId);
        this.taskId = -1;
    }

    private void updateTrails() {
        for (Player player: this.plugin.getServer().getOnlinePlayers()) {
            PlayerTagManager tagManager = new PlayerTagManager(this.keyManager, player.getPersistentDataContainer());
            tagManager.setLocation(player.getLocation());
        }
    }

    private void updateCompasses() {
        for (Player player: this.plugin.getServer().getOnlinePlayers()) {
            for (ItemStack item: player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.COMPASS) {
                    CompassMeta meta = (CompassMeta) item.getItemMeta();
                    assert meta != null;
                    PersistentDataContainer compassContainer = meta.getPersistentDataContainer();
                    CompassTagManager compassTagManager = new CompassTagManager(this.keyManager, compassContainer);
                    if (compassTagManager.hasWatchedPlayer()) {
                        Player watchedPlayer = this.plugin.getServer().getPlayer(compassTagManager.getWatchedPlayer());
                        if (watchedPlayer == null) {
                            CompassManager.clear(meta);
                        }
                        else {
                            PersistentDataContainer playerContainer = watchedPlayer.getPersistentDataContainer();
                            PlayerTagManager playerTagManager = new PlayerTagManager(this.keyManager, playerContainer);
                            Location location = playerTagManager.getLocation(player.getWorld());
                            if (location != null)
                                CompassManager.point(meta, location);
                            else
                                CompassManager.clear(meta);
                        }
                        item.setItemMeta(meta);
                    }
                }
            }
        }
    }

}
