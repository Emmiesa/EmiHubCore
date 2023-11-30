package me.emmiesa.hub.listener;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.menu.ServerSelectorMenu;
import me.emmiesa.hub.utils.CC;
import me.emmiesa.hub.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	private final HubCore plugin = HubCore.get();

	private final ItemStack enderbutt;
	private final ItemStack selector;

	public PlayerListener() {
		this.enderbutt = new ItemBuilder(Material.ENDER_PEARL).name(HubCore.instance.getConfig("hotbar.yml").getString("items.enderbutt.name")).build();
		this.selector = new ItemBuilder(Material.COMPASS).name(HubCore.instance.getConfig("hotbar.yml").getString("items.server_selector.name")).build();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.getInventory().clear();

		player.setGameMode(GameMode.SURVIVAL);

		if (plugin.getConfig("hotbar.yml").getBoolean("items.enderbutt.enabled", true)) {
			player.getInventory().setItem(plugin.getConfig("hotbar.yml").getInt("items.enderbutt.slot"), enderbutt);
		}

		if (plugin.getConfig("hotbar.yml").getBoolean("items.server_selector.enabled", true)) {
			player.getInventory().setItem(plugin.getConfig("hotbar.yml").getInt("items.server_selector.slot"), selector);
		}

		// dynamic / custom added items getting it from the string with the following name.
		ConfigurationSection dynamicItemsConfig = plugin.getConfig("hotbar.yml").getConfigurationSection("dynamic_items");
		if (dynamicItemsConfig != null) {
			for (String itemName : dynamicItemsConfig.getKeys(false)) {
				if (dynamicItemsConfig.getBoolean(itemName + ".enabled", true)) {
					ItemStack hotBarItem = new ItemBuilder(Material.valueOf(dynamicItemsConfig.getString(itemName + ".material")))
							.name(dynamicItemsConfig.getString(itemName + ".name"))
							.build();
					player.getInventory().setItem(dynamicItemsConfig.getInt(itemName + ".slot"), hotBarItem);
				}
			}
		}

		if (plugin.getConfig("features.yml").getBoolean("welcome_message.enabled", true)) {
			for (String message : plugin.getConfig("features.yml").getStringList("welcome_message.message"))
				player.sendMessage(CC.translate(message));
		}

		Location spawnLocation = plugin.getSpawnLocation();

		if (spawnLocation != null) {
			event.getPlayer().teleport(spawnLocation);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (item != null && item.equals(selector)) {
				boolean commandEnabled = plugin.getConfig("hotbar.yml").getBoolean("items.server_selector.execute_command.enabled", true);

				if (commandEnabled) {
					String command = plugin.getConfig("hotbar.yml").getString("items.server_selector.execute_command.command");
					player.performCommand(command);
				} else {
					new ServerSelectorMenu().openMenu(player);
				}

				event.setCancelled(true);
			}
		}

		for (String itemName : plugin.getConfig("hotbar.yml").getConfigurationSection("dynamic_items").getKeys(false)) {
			if (item != null && item.isSimilar(getHotBarItem(itemName))) {
				boolean commandEnabled = plugin.getConfig("hotbar.yml").getBoolean("dynamic_items." + itemName + ".execute_command.enabled", true);

				if (commandEnabled) {
					String command = plugin.getConfig("hotbar.yml").getString("dynamic_items." + itemName + ".execute_command.command");
					player.performCommand(command);
				} else {
					//I did not want to return null or send a message because the hotbar item may have another purpose. But here it'd be the message which would get sent if the item is not handled.
					}

					event.setCancelled(true);
					break;
				}
			}

		if (player.getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (player.getGameMode() == GameMode.SURVIVAL) {
				event.setCancelled(true);
				player.setFoodLevel(20);
			}
		}
	}

	@EventHandler
	public void onMoveItem(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {

			if (e.getWhoClicked().getGameMode() == GameMode.SURVIVAL) {
				e.setCancelled(true);
			}
		}
	}

	private ItemStack getHotBarItem(String itemName) {
		ConfigurationSection config = plugin.getConfig("hotbar.yml").getConfigurationSection("dynamic_items." + itemName);
		if (config != null && config.getBoolean("enabled", true)) {
			return new ItemBuilder(Material.valueOf(config.getString("material")))
					.name(config.getString("name"))
					.build();
		}
		return null;
	}
}