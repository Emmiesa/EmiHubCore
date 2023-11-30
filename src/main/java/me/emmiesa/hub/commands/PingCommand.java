package me.emmiesa.hub.commands;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        boolean enablePing = HubCore.instance.getConfig("features.yml").getBoolean("ping.enabled", true);

        if (!enablePing) {
            player.sendMessage(CC.translate(HubCore.instance.getConfig("features.yml").getString("ping.disabled_message")));
            return true;
        }

        boolean requirePingPermission = HubCore.instance.getConfig("features.yml").getBoolean("ping.require_permission", true);

        if (enablePing && requirePingPermission && !player.hasPermission(HubCore.instance.getConfig("features.yml").getString("ping.permission"))) {
            player.sendMessage(CC.translate(HubCore.instance.getConfig("messages.yml").getString("messages.no-permission")));
            return true;
        }

        if (label.equalsIgnoreCase("ping")) {
            if (args.length == 0) {
                int ping = getPing(player);
                String pingMessage = getConfigMessage("messages.ping.ping_self")
                        .replace("%player%", player.getName())
                        .replace("%ping%", String.valueOf(ping));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pingMessage));
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    int ping = getPing(target);
                    String pingMessage = getConfigMessage("messages.ping.ping_other")
                            .replace("%player%", target.getName())
                            .replace("%ping%", String.valueOf(ping));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pingMessage));
                } else {
                    String notFoundMessage = getConfigMessage("messages.ping.ping_target_not_found")
                            .replace("%player%", args[0]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notFoundMessage));
                }
            } else {
                player.sendMessage(CC.translate("&cIncorrect usage: /ping (player)"));
            }
            return true;
        }

        return false;
    }

    private int getPing(Player player) {
        return ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().ping;
    }

    private String getConfigMessage(String path) {
        return HubCore.instance.getConfig("messages.yml").getString(path, "");
    }
}