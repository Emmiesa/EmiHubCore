package me.emmiesa.hub.commands;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TikTokCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = "tiktok";

        boolean enableCommand = HubCore.instance.getConfig().getBoolean("features.links." + commandName + ".enabled", true);
        String disabledMessageTemplate = HubCore.instance.getConfig().getString("features.links." + commandName + ".disabled_message");

        if (!enableCommand) {
            String disabledMessage = disabledMessageTemplate.replace("%link%", commandName);

            sender.sendMessage(CC.translate(disabledMessage));
            return true;
        } else {
            String link = HubCore.getPlugin(HubCore.class).getConfig().getString("features.links." + commandName + ".enabled_message");
            sender.sendMessage(CC.translate(link));
            return false;
        }
    }
}