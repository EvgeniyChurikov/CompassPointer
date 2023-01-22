package me.quelleen.compasspointer.commands;

import me.quelleen.compasspointer.KeyManager;
import me.quelleen.compasspointer.tagManagers.CompassTagManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PointCommand implements TabExecutor {

    private final KeyManager keyManager;

    public PointCommand(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                String nickname = args[0];
                Player watchedPlayer = sender.getServer().getPlayer(nickname);
                if (watchedPlayer != null) {

                    Player senderPlayer = (Player) sender;

                    ItemStack compass = new ItemStack(Material.COMPASS);
                    ItemMeta meta = compass.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.WHITE + watchedPlayer.getName());

                    CompassTagManager compassTagManager = new CompassTagManager(
                            this.keyManager,
                            meta.getPersistentDataContainer());
                    compassTagManager.setWatchedPlayer(watchedPlayer.getUniqueId());

                    compass.setItemMeta(meta);

                    senderPlayer.getInventory().addItem(compass);

                    senderPlayer.getServer().broadcastMessage(senderPlayer.getDisplayName()
                            + " got a compass pointing to " + watchedPlayer.getDisplayName());

                }
                else {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                }

                return true;
            }
            else {
                sender.sendMessage(ChatColor.RED + "Command entered incorrectly.");
                return false;
            }
        }
        else {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1)
            return null;
        else
            return  new ArrayList<>();
    }

}
