package com.minigameworld.customevents.minigame;

import org.bukkit.event.Cancellable;

import com.minigameworld.minigameframes.MiniGame;

/**
 * Called when a minigame starts
 *
 */
public class MiniGameStartEvent extends MiniGameEvent implements Cancellable {

	private boolean cancelled;

	public MiniGameStartEvent(MiniGame minigame) {
		super(minigame);
		this.cancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
