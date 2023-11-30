package me.emmiesa.hub.listener;

import me.emmiesa.hub.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderButtListener implements Listener {

	private final HubCore hub = HubCore.get();


	//this ItemStack is not really in usage. Though I kept it.
	public ItemStack getEnderButt() {
		ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL, 16);
		ItemMeta enderPearlMeta = enderPearl.getItemMeta();
		enderPearlMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HubCore.instance.getConfig().getString("enderbutt")));
		enderPearl.setItemMeta(enderPearlMeta);
		return enderPearl;
	}

	public void setupEnderpearlRunnable(final Item item) {
		new BukkitRunnable() {
			public void run() {
				if (item.isDead()) {
					cancel();
				}
				if (item.getVelocity().getX() == 0.0D || item.getVelocity().getY() == 0.0D || item.getVelocity().getZ() == 0.0D) {
					Player player = (Player) item.getPassenger();
					item.remove();
					if (player != null) {
						player.teleport(player.getLocation().add(0.0D, 0.5D, 0.0D));
					}
					cancel();
				}
			}
		}.runTaskTimer(this.hub, 2L, 1L);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand();
			if (itemStack.getType() == Material.ENDER_PEARL) {
				event.setCancelled(true);
				event.setUseItemInHand(Event.Result.DENY);
				event.setUseInteractedBlock(Event.Result.DENY);
				if (player.getGameMode() == GameMode.SURVIVAL) {
					Item item = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.ENDER_PEARL, 16));
					item.setPickupDelay(10000);
					item.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5F));
					item.setPassenger((Entity) player);
					setupEnderpearlRunnable(item);
					player.updateInventory();
				}
				if (player.getGameMode() == GameMode.CREATIVE) {
					Item item = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.ENDER_PEARL, 16));
					item.setPickupDelay(10000);
					item.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5F));
					item.setPassenger((Entity) player);
					setupEnderpearlRunnable(item);
					player.updateInventory();
				}
			}
		}
	}
}