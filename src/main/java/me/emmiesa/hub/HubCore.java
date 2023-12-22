package me.emmiesa.hub;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import me.emmiesa.hub.commands.*;
import me.emmiesa.hub.commands.staff.ServerSelectorCommand;
import me.emmiesa.hub.commands.staff.SetSpawnCommand;
import me.emmiesa.hub.commands.tabcomplete.TabCompleter;
import me.emmiesa.hub.listener.EnderButtListener;
import me.emmiesa.hub.listener.PlayerListener;
import me.emmiesa.hub.listener.SneakRocketListener;
import me.emmiesa.hub.scoreboard.ScoreboardLayout;
import me.emmiesa.hub.utils.CC;
import me.emmiesa.hub.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HubCore extends JavaPlugin {

	public static me.emmiesa.hub.HubCore instance;
	private Location spawnLocation;
	public FileConfiguration hotbarConfig;
	public FileConfiguration featuresConfig;
	public FileConfiguration scoreboardConfig;
	public FileConfiguration menusConfig;
	public FileConfiguration messagesConfig;
	public FileConfiguration tablistConfig;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		saveResource("hotbar.yml", false);
		hotbarConfig = getConfig("hotbar.yml");
		saveResource("features.yml", false);
		featuresConfig = getConfig("features.yml");
		saveResource("scoreboard.yml", false);
		scoreboardConfig = getConfig("scoreboard.yml");
		saveResource("menus.yml", false);
		menusConfig = getConfig("menus.yml");
		saveResource("messages.yml", false);
		messagesConfig = getConfig("messages.yml");
		saveResource("tablist.yml", false);
		tablistConfig = getConfig("tablist.yml");

		loadSpawnLocation();
		loadConfigurations();

		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new EnderButtListener(), this);
		getServer().getPluginManager().registerEvents(new SneakRocketListener(this), this);

		getCommand("emihubcore-lite").setTabCompleter(new TabCompleter());

		getCommand("ping").setExecutor(new PingCommand());
		getCommand("info").setExecutor(new InfoCommand());
		getCommand("store").setExecutor(new StoreCommand());
		getCommand("tiktok").setExecutor(new TikTokCommand());
		getCommand("emihubcore-lite").setExecutor(new HubCoreCommand());
		getCommand("discord").setExecutor(new DiscordCommand());
		getCommand("website").setExecutor(new WebsiteCommand());
		getCommand("twitter").setExecutor(new TwitterCommand());
		getCommand("youtube").setExecutor(new YouTubeCommand());
		getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
		getCommand("serverselector").setExecutor(new ServerSelectorCommand());

		Assemble assemble = new Assemble(this, new ScoreboardLayout());
		assemble.setAssembleStyle(AssembleStyle.MODERN);
		assemble.setTicks(2);

		if(!me.emmiesa.hub.HubCore.get().getDescription().getAuthors().contains("Emmiesa")) {
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info("Why did you change the author name?");
			getLogger().info("How dare you?");
			getLogger().info("");
			getLogger().info("Change it back to default!");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (!HubCore.get().getDescription().getName().contains("EmiHubCore-Lite")) {
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info("Why did you change the plugin name?");
			getLogger().info("How dare you?");
			getLogger().info("");
			getLogger().info("Change it back to default!");
			getLogger().info(" ");
			getLogger().info(" ");
			getLogger().info(" ");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		getLogger().info(" ");
		getLogger().info("-------------------------------------------------------------------------------------");
		getLogger().info(" Free trial version of Emmiesa's HubCore!");
		getLogger().info(" ● Version: " + getDescription().getVersion());
		getLogger().info(" ● Link: " + getDescription().getWebsite());
		getLogger().info("-------------------------------------------------------------------------------------");
		getLogger().info(" ");
	}

	@Override
	public void onDisable() {
		getLogger().info(" ");
		getLogger().info("● Disabled HubCore.");
		getLogger().info(" ");
	}

	public static HubCore get() {
		return instance;
	}

	private void loadSpawnLocation() {
		FileConfiguration config = getConfig();
		boolean enableSpawnTeleport = config.getBoolean("enableSpawnTeleport", true);

		if (enableSpawnTeleport && config.contains("spawnLocation.world")) {
			World world = Bukkit.getWorld(config.getString("spawnLocation.world"));
			double x = config.getDouble("spawnLocation.x");
			double y = config.getDouble("spawnLocation.y");
			double z = config.getDouble("spawnLocation.z");
			float yaw = (float) config.getDouble("spawnLocation.yaw");
			float pitch = (float) config.getDouble("spawnLocation.pitch");

			spawnLocation = new Location(world, x, y, z, yaw, pitch);
		}
	}

	private void loadConfigurations() {
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location location) {
		this.spawnLocation = location;

		getConfig().set("spawnLocation.world", location.getWorld().getName());
		getConfig().set("spawnLocation.x", location.getX());
		getConfig().set("spawnLocation.y", location.getY());
		getConfig().set("spawnLocation.z", location.getZ());
		getConfig().set("spawnLocation.yaw", location.getYaw());
		getConfig().set("spawnLocation.pitch", location.getPitch());

		saveConfig();
	}

	public void reloadAllConfigs() { // Here I added all the .yml's. This is used at me.emmiesa.commands.HubCoreCommand in the "reload" case. Just a note :)
		hotbarConfig = getConfig("hotbar.yml");
		featuresConfig = getConfig("features.yml");
		menusConfig = getConfig("menus.yml");
		scoreboardConfig = getConfig("scoreboard.yml");
		messagesConfig = getConfig("messages.yml");
		hotbarConfig = getConfig("hotbar.yml");
	}

	public FileConfiguration getConfig(String fileName) {
		File configFile = new File(getDataFolder(), fileName);
		return YamlConfiguration.loadConfiguration(configFile);
	}
}
