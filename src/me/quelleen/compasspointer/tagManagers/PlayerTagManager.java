package me.quelleen.compasspointer.tagManagers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.quelleen.compasspointer.KeyManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerTagManager extends TagManager {

    private final NamespacedKey trailKey;

    public PlayerTagManager(KeyManager keyManager, PersistentDataContainer container) {
        super(keyManager, container);
        this.trailKey = this.getKeyManager().getKey("Trail");
    }

    public boolean hasTrail() {
        return this.getContainer().has(this.trailKey, PersistentDataType.TAG_CONTAINER);
    }

    public void createTrail() {
        PersistentDataContainer emptyContainer = this.getContainer().getAdapterContext().newPersistentDataContainer();
        this.getContainer().set(this.trailKey, PersistentDataType.TAG_CONTAINER, emptyContainer);
    }

    public void resetTrail() {
        PersistentDataContainer emptyContainer = this.getContainer().getAdapterContext().newPersistentDataContainer();
        this.getContainer().set(this.trailKey, PersistentDataType.TAG_CONTAINER, emptyContainer);
    }

    @Nullable
    public Location getLocation(World world) {

        PersistentDataContainer trailContainer = this.getContainer()
                .get(this.trailKey, PersistentDataType.TAG_CONTAINER);
        assert trailContainer != null;

        NamespacedKey key = this.getKeyManager().getKey(world.getName());

        if (trailContainer.has(key, PersistentDataType.TAG_CONTAINER)) {

            PersistentDataContainer coordsContainer = trailContainer.get(key, PersistentDataType.TAG_CONTAINER);
            assert coordsContainer != null;

            Integer x, y, z;
            x = coordsContainer.get(this.getKeyManager().getKey("X"), PersistentDataType.INTEGER);
            y = coordsContainer.get(this.getKeyManager().getKey("Y"), PersistentDataType.INTEGER);
            z = coordsContainer.get(this.getKeyManager().getKey("Z"), PersistentDataType.INTEGER);
            assert x!=null && y!=null && z!= null;

            return new Location(world, x, y, z);

        }
        else {
            return null;
        }
    }

    public void setLocation(@NotNull Location location) {

        PersistentDataContainer trailContainer = this.getContainer()
                .get(this.trailKey, PersistentDataType.TAG_CONTAINER);
        World world = location.getWorld();
        assert trailContainer != null;
        assert world != null;

        NamespacedKey key = this.getKeyManager().getKey(world.getName());

        PersistentDataContainer coordsContainer = this.getContainer().getAdapterContext().newPersistentDataContainer();

        coordsContainer.set(this.getKeyManager().getKey("X"), PersistentDataType.INTEGER, location.getBlockX());
        coordsContainer.set(this.getKeyManager().getKey("Y"), PersistentDataType.INTEGER, location.getBlockY());
        coordsContainer.set(this.getKeyManager().getKey("Z"), PersistentDataType.INTEGER, location.getBlockZ());

        trailContainer.set(key, PersistentDataType.TAG_CONTAINER, coordsContainer);
        this.getContainer().set(this.trailKey, PersistentDataType.TAG_CONTAINER, trailContainer);

    }

}
