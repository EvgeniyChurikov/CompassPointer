package me.quelleen.compasspointer;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.inventory.meta.CompassMeta;

public class CompassManager {

    public static void point(@NotNull CompassMeta meta, @NotNull Location location) {
        if (meta.hasLodestone()) {
            Location oldLocation = meta.getLodestone();
            assert oldLocation != null;
            Location newLocation = location.clone();
            if (oldLocation.getBlockY() == newLocation.getBlockY())
                newLocation.add(0, 1, 0);
            meta.setLodestone(newLocation);
        }
        else {
            meta.setLodestone(location);
        }
        meta.setLodestoneTracked(false);
    }

    public static void clear(@NotNull CompassMeta meta) {
        meta.setLodestone(null);
        meta.setLodestoneTracked(false);
    }

}
