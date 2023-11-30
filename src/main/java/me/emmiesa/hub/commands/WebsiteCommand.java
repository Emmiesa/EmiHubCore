package me.emmiesa.hub.commands;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WebsiteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = "website";
        String configPath = "features.links." + commandName + ".";

        boolean enableCommand = HubCore.instance.getConfig().getBoolean(configPath + "enabled", true);

        if (!enableCommand) {
            String disabledMessage = CC.translate(HubCore.instance.getConfig().getString(configPath + "disabled_message", ""))
                    .replace("%link%", commandName);

            sender.sendMessage(disabledMessage);
        } else {
            String link = CC.translate(HubCore.instance.getConfig().getString(configPath + "enabled_message", ""))
                    .replace("%link%", commandName);

            sender.sendMessage(link);
        }

        return true;
    }
}