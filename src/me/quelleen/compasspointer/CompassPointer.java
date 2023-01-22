package me.quelleen.compasspointer;

import me.quelleen.compasspointer.commands.PointCommand;
import me.quelleen.compasspointer.commands.TrailResetCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CompassPointer extends JavaPlugin {

    private Updater updater;

    @Override
    public void onEnable() {

        KeyManager keyManager = new KeyManager(this);

        Objects.requireNonNull(this.getCommand("point")).setExecutor(new PointCommand(keyManager));
        Objects.requireNonNull(this.getCommand("trailreset")).setExecutor(new TrailResetCommand(keyManager));

        this.getServer().getPluginManager().registerEvents(new EventListener(keyManager, this), this);

        this.updater = new Updater(keyManager, this);
        this.updater.start();

    }

    @Override
    public void onDisable() {
        this.updater.stop();
    }
}
