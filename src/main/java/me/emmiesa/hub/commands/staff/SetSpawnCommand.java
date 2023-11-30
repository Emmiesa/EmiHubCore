package me.emmiesa.hub.commands.staff;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final HubCore plugin;

    public SetSpawnCommand(HubCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean enableSpawnTeleport = plugin.getConfig().getBoolean("enableSpawnTeleport", true);
            plugin.setSpawnLocation(player.getLocation());

            player.sendMessage(CC.translate(HubCore.instance.getConfig("messages.yml").getString("messages.spawn-set")));

        if (!enableSpawnTeleport) {
            player.sendMessage(CC.translate(HubCore.instance.getConfig("messages.yml").getString("messages.spawn-disabled")));
            System.out.println(" ");
            System.out.println(HubCore.instance.getConfig("messages.yml").getString("messages.spawn-disabled-console"));
            System.out.println(" ");
        }

            return true;
        } else {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
    }
}