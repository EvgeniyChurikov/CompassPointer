package me.quelleen.compasspointer.tagManagers;

import me.quelleen.compasspointer.KeyManager;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CompassTagManager extends TagManager {

    private final NamespacedKey watchedPlayerKey;

    public CompassTagManager(KeyManager keyManager, PersistentDataContainer container) {
        super(keyManager, container);
        this.watchedPlayerKey = this.getKeyManager().getKey("WatchedPlayer");
    }

    public boolean hasWatchedPlayer() {
        return this.getContainer().has(this.watchedPlayerKey, PersistentDataType.STRING);
    }

    public UUID getWatchedPlayer() {
        String uuid = this.getContainer().get(this.watchedPlayerKey, PersistentDataType.STRING);
        if (uuid == null)
            return null;
        else
            return UUID.fromString(uuid);
    }

    public void setWatchedPlayer(UUID uuid) {
        this.getContainer().set(this.watchedPlayerKey, PersistentDataType.STRING, uuid.toString());
    }

}
