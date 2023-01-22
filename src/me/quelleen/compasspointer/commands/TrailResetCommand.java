package me.quelleen.compasspointer.commands;

import me.quelleen.compasspointer.KeyManager;
import me.quelleen.compasspointer.tagManagers.PlayerTagManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TrailResetCommand implements TabExecutor {

    private final KeyManager keyManager;

    public TrailResetCommand(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            for (Player player: sender.getServer().getOnlinePlayers()) {
                PlayerTagManager playerTagManager = new PlayerTagManager(
                        this.keyManager, player.getPersistentDataContainer());
                playerTagManager.resetTrail();
            }
            sender.getServer().broadcastMessage("Trails of all players were reset.");
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Command entered incorrectly.");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }

}
