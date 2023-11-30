package me.emmiesa.hub.commands.tabcomplete;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().startsWith("emihubcore-lite")) {
            if (sender instanceof Player) {

                ArrayList<String> list = new ArrayList<String>();

                list.add("reload");
                list.add("help");

                return list;
            }
        }
        return null;
    }
}