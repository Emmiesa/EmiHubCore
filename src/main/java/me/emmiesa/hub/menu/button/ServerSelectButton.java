package me.emmiesa.hub.menu.button;

import me.emmiesa.hub.utils.ItemBuilder;
import me.emmiesa.hub.utils.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ServerSelectButton extends Button {

	private Material icon;
	private String displayName;
	private List<String> lore;
	private String command;

	public ServerSelectButton(Material icon, String displayName, List<String> lore, String command) {
		this.icon = icon;
		this.displayName = displayName;
		this.lore = lore;
		this.command = command;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(icon).name(displayName).lore(lore).build();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
		executeCommand(player);
	}

	private void executeCommand(Player player) {
		if (command != null && !command.isEmpty()) {
			Bukkit.getServer().dispatchCommand(player, command);
		} else {
			player.sendMessage("Clicked slot #");
			playSuccess(player);
		}
	}
}