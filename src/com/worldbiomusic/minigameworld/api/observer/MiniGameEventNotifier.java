package com.worldbiomusic.minigameworld.api.observer;

import com.worldbiomusic.minigameworld.minigameframes.MiniGame;

/**
 * Event notifier with minigame phaze(MiniGameEvent)<br>
 * Already implemented in "MiniGame" class<br>
 * <br>
 * 
 * [IMPORTANT] Consider to change to use custom event <br>
 * 
 * 
 */
public interface MiniGameEventNotifier {
	/**
	 * MiniGame phaze
	 */
	public enum MiniGameEvent {
		/**
		 * When minigame started
		 */
		START,
		/**
		 * Before minigame finished 
		 */
		BEFORE_FINISH,
		/**
		 * When minigame finished
		 */
		FINISH,

		/**
		 * When minigame gets exception
		 */
		EXCEPTION,

		/**
		 * When event passed to the minigame<br>
		 * But minigame still process event in last<br>
		 */
		EVENT_PASS,
		
		/**
		 * When minigame is registered to MiniGameWolrd plugin
		 */
		REGISTRATION,
		
		/**
		 * When minigame is unregistered from MiniGameWorld plugin
		 */
		UNREGISTRATION;
	}

	/**
	 * Reigsters observer
	 * 
	 * @param observer Observer to register
	 */
	public void registerObserver(MiniGameObserver observer);

	/**
	 * Unregisters observer
	 * 
	 * @param observer Observer to unregister
	 */
	public void unregisterObserver(MiniGameObserver observer);

	/**
	 * Notify phaze(MiniGameEvent) to observers
	 * 
	 * @param minigame Minigame to notify
	 * @param event Minigame Event
	 */
	public void notifyObservers(MiniGame minigame, MiniGameEvent event);
}