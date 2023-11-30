package me.emmiesa.hub.menu;

import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.menu.button.ServerSelectButton;
import me.emmiesa.hub.utils.CC;
import me.emmiesa.hub.utils.menu.Button;
import me.emmiesa.hub.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSelectorMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return CC.translate(HubCore.instance.getConfig("menus.yml").getString("menus.server_selector.title"));
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		ConfigurationSection serversSection = HubCore.instance.getConfig("menus.yml").getConfigurationSection("menus.server_selector.servers");

		if (serversSection != null) {
			for (String serverKey : serversSection.getKeys(false)) {
				ConfigurationSection serverSection = serversSection.getConfigurationSection(serverKey);

				if (serverSection != null) {
					int slot = serverSection.getInt("slot", 0);
					Material materialType = Material.matchMaterial(serverSection.getString("material", "STONE"));
					String name = CC.translate(serverSection.getString("name", "&c&lInvalid Server"));
					List<String> lore = CC.translate(serverSection.getStringList("lore"));
					int data = serverSection.getInt("data", 0);
					Material material = new MaterialData(materialType, (byte) data).toItemStack().getType();

					buttons.put(slot, new ServerSelectButton(material, name, lore, serverSection.getString("command")));
				}
			}
		}

		return buttons;
	}

	@Override
	public int getSize() {
		ConfigurationSection menuSection = HubCore.instance.getConfig("menus.yml").getConfigurationSection("menus.server_selector");

		if (menuSection != null && menuSection.contains("size")) {
			return menuSection.getInt("size", 9 * 3);
		}

		return 9 * 3; // I could have returned null because the menu string would be empty, but instead of running into errors in the console, I decided to choose this just to avoid errors.
	}
}