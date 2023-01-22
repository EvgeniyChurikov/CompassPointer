package me.quelleen.compasspointer.tagManagers;

import me.quelleen.compasspointer.KeyManager;
import org.bukkit.persistence.PersistentDataContainer;

public class TagManager {

    private final KeyManager keyManager;
    private final PersistentDataContainer container;

    public TagManager(KeyManager keyManager, PersistentDataContainer container) {
        this.keyManager = keyManager;
        this.container = container;
    }

    protected KeyManager getKeyManager() {
        return this.keyManager;
    }

    protected PersistentDataContainer getContainer() {
        return this.container;
    }

}
