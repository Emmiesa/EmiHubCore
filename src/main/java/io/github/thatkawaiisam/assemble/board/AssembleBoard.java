package io.github.thatkawaiisam.assemble.board;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class AssembleBoard {

	private final Assemble assemble;

	private final List<AssembleBoardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();

	private final UUID uuid;

	public AssembleBoard(Player player, Assemble assemble) {
		this.uuid = player.getUniqueId();
		this.assemble = assemble;
		this.setup(player);
	}

	public Scoreboard getScoreboard() {
		Player player = Bukkit.getPlayer(getUuid());
		if (getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			return player.getScoreboard();
		} else {
			return Bukkit.getScoreboardManager().getNewScoreboard();
		}
	}

	public Objective getObjective() {
		Scoreboard scoreboard = getScoreboard();
		if (scoreboard.getObjective("Assemble") == null) {
			Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(getAssemble().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
			return objective;
		} else {
			return scoreboard.getObjective("Assemble");
		}
	}

	private void setup(Player player) {
		Scoreboard scoreboard = getScoreboard();
		player.setScoreboard(scoreboard);
		getObjective();

		if (assemble.isCallEvents()) {
			AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
			Bukkit.getPluginManager().callEvent(createdEvent);
		}
	}

	public AssembleBoardEntry getEntryAtPosition(int pos) {
		return pos >= this.entries.size() ? null : this.entries.get(pos);
	}

	public String getUniqueIdentifier(int position) {
		String identifier = getRandomChatColor(position) + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;
		}

		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(position);
		}

		this.identifiers.add(identifier);

		return identifier;
	}

	private String getRandomChatColor(int position) {
		return assemble.getChatColorCache()[position].toString();
	}
}
