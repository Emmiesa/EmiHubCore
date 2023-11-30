package me.emmiesa.hub.commands;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements CommandExecutor {

    private final HubCore plugin = HubCore.get();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean enableInfo = HubCore.instance.getConfig("messages.yml").getBoolean("command_replys.info.enabled", true);

        if (!enableInfo) {
            sender.sendMessage(CC.translate(HubCore.instance.getConfig("messages.yml").getString("command_replys.info.disabled_message")));
            return true;
        }

        if (enableInfo) {
            for (String message : plugin.getConfig("messages.yml").getStringList("command_replys.info.message")) {
                sender.sendMessage(CC.translate(message));
            }
        }
        return false;
    }
}