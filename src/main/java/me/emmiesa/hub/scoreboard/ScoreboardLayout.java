package me.emmiesa.hub.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.emmiesa.hub.HubCore;
import me.emmiesa.hub.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elb1to
 * @since 8/22/2023
 * © 2023 HungerGames from FrozedClub
 */
public class ScoreboardLayout implements AssembleAdapter {

	private final HubCore plugin = HubCore.get();

	private int animationIndex = 0;

	@Override
	public String getTitle(Player player) {
		boolean enableAnimation = plugin.getConfig("scoreboard.yml").getBoolean("scoreboard.animated", true);

		if (enableAnimation) {
			int tickSpeed = plugin.getConfig("scoreboard.yml").getInt("scoreboard.ticks", 10);
			List<String> animationFrames = plugin.getConfig("scoreboard.yml").getStringList("scoreboard.animation");

			int currentFrame = (int) ((System.currentTimeMillis() / (tickSpeed * 50)) % animationFrames.size());
			return CC.translate(animationFrames.get(currentFrame));
		} else {
			return CC.translate(plugin.getConfig("scoreboard.yml").getString("scoreboard.title", "&d&lHUB"));
		}
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> lines = new ArrayList<>();

		List<String> originalLines = plugin.getConfig("scoreboard.yml").getStringList("scoreboard.lines");
		originalLines.forEach(line -> {
			lines.add(line
					.replace("%online_players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
					.replace("%bars%", plugin.getConfig("scoreboard.yml").getString("scoreboard.bars.bars-format", "&7&m--------------------"))
			);
		});

		String footer = plugin.getConfig("scoreboard.yml").getString("scoreboard.footer.text", "&dminemen.club");
		boolean enableFooterAnimation = plugin.getConfig("scoreboard.yml").getBoolean("scoreboard.footer.animated", true);

		if (enableFooterAnimation) {
			int tickSpeedFooter = plugin.getConfig("scoreboard.yml").getInt("scoreboard.footer.ticks", 10);
			List<String> animationFramesFooter = plugin.getConfig("scoreboard.yml").getStringList("scoreboard.footer.animation");

			int currentFrameFooter = (int) ((System.currentTimeMillis() / (tickSpeedFooter * 50)) % animationFramesFooter.size());
			footer = CC.translate(animationFramesFooter.get(currentFrameFooter));
		}

		List<String> translatedLines = lines.stream()
				.map(line -> CC.translate(PlaceholderAPI.setPlaceholders(player, line)))
				.collect(Collectors.toList());

		int footerIndex = originalLines.indexOf("%footer%");
		if (footerIndex >= 0 && footerIndex < translatedLines.size()) {
			translatedLines.set(footerIndex, footer);
		}

		return translatedLines;
	}

	private String getAnimatedTitle() {
		List<String> animation = plugin.getConfig("scoreboard.yml").getStringList("scoreboard.animation");

		if (plugin.getConfig("scoreboard.yml").getBoolean("scoreboard.animated")) {
			if (animationIndex >= animation.size()) {
				animationIndex = 0;
			}
			return animation.get(animationIndex++);
		} else {
			return animation.get(0);
		}
	}
}
