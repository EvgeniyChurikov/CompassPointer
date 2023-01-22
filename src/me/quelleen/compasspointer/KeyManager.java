package me.quelleen.compasspointer;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class KeyManager {

    private final Plugin plugin;

    public KeyManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey getKey(String key) {
        return new NamespacedKey(this.plugin, key);
    }

}
