package me.emmiesa.hub.commands;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HubCoreCommand implements CommandExecutor {

    private final HubCore plugin = HubCore.get();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(CC.translate("§2§m-----------------------------------"));
            sender.sendMessage(CC.translate("§aThis version is a free trial. More info at"));
            sender.sendMessage(CC.translate(" §fhttps://dsc.gg/emmiesa&a!"));
            sender.sendMessage(CC.translate(" "));
            sender.sendMessage(CC.translate(" §aEmmiesa's HubCore §f(v1.0-LITE)"));
            sender.sendMessage(CC.translate(" §a● Authors: §fEmmiesa"));
            sender.sendMessage(CC.translate(" §a● Last Changes: §f30/11/2023"));
            sender.sendMessage(CC.translate("§2§m-----------------------------------"));
            sender.sendMessage(" ");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("hubcore.admin")) {
                sender.sendMessage(CC.translate(plugin.getConfig("messages.yml").getString("messages.no-permission")));
                return true;
            }

            for (String message : plugin.getConfig("messages.yml").getStringList("command_replys.hubcore-help")) {
                sender.sendMessage(CC.translate(message));
            }
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("hubcore.admin")) {
                sender.sendMessage(CC.translate(plugin.getConfig("messages.yml").getString("messages.no-permission")));
                return true;
            }

            for (String message : plugin.getConfig("messages.yml").getStringList("messages.reload")) {
                sender.sendMessage(CC.translate(message));
            }

            plugin.reloadAllConfigs();

            for (String message : plugin.getConfig("messages.yml").getStringList("messages.reload-finish")) {
                sender.sendMessage(CC.translate(message));
            }
        }
        return false;
    }
}