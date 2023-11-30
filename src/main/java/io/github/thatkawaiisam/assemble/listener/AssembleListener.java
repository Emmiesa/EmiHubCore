package io.github.thatkawaiisam.assemble.listener;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.board.AssembleBoard;
import io.github.thatkawaiisam.assemble.events.AssembleBoardCreateEvent;
import io.github.thatkawaiisam.assemble.events.AssembleBoardDestroyEvent;
import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

@Getter
public class AssembleListener implements Listener {

	private final Assemble assemble;

	public AssembleListener(Assemble assemble) {
		this.assemble = assemble;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (assemble.isCallEvents()) {
			AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);
			Bukkit.getPluginManager().callEvent(createEvent);
			if (createEvent.isCancelled()) {
				return;
			}
		}

		getAssemble().getBoards().put(player.getUniqueId(), new AssembleBoard(player, getAssemble()));

		Scoreboard board = player.getScoreboard();
		Objective belowNameHealth = board.getObjective("nameHealth");
		if (belowNameHealth == null) {
			belowNameHealth = board.registerNewObjective("nameHealth", "health");
			belowNameHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
			belowNameHealth.setDisplayName(ChatColor.DARK_RED + StringEscapeUtils.unescapeJava("\u2764"));
			belowNameHealth.getScore(player.getName()).setScore((int) Math.floor(player.getHealth()));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (assemble.isCallEvents()) {
			AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(event.getPlayer());
			Bukkit.getPluginManager().callEvent(destroyEvent);
			if (destroyEvent.isCancelled()) {
				return;
			}
		}

		getAssemble().getBoards().remove(event.getPlayer().getUniqueId());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
}
