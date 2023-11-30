package me.emmiesa.hub.commands.staff;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.menu.ServerSelectorMenu;
import me.emmiesa.hub.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerSelectorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        boolean enableServerSelector = HubCore.instance.getConfig("hotbar.yml").getBoolean("selector-menu_command.enabled", true);

        if (enableServerSelector) {
            new ServerSelectorMenu().openMenu(player);
            String openedMessage = HubCore.instance.getConfig("hotbar.yml").getString("selector-menu_command.opened_message");
            if (openedMessage != null) {
                player.sendMessage(CC.translate(openedMessage));
            }
        } else {
            String disabledMessage = HubCore.instance.getConfig("hotbar.yml").getString("selector-menu_command.disabled_message");
            if (disabledMessage != null) {
                player.sendMessage(CC.translate(disabledMessage));
            }
        }

        return true;
    }
}