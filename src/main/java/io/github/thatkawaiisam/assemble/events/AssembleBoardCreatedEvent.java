package io.github.thatkawaiisam.assemble.events;

import io.github.thatkawaiisam.assemble.board.AssembleBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class AssembleBoardCreatedEvent extends Event {

	@Getter
	public static HandlerList handlerList = new HandlerList();
	private final AssembleBoard board;
	private boolean cancelled = false;

	public AssembleBoardCreatedEvent(AssembleBoard board) {
		this.board = board;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
