package com.worldbiomusic.minigameworld.listeners;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;

import com.worldbiomusic.minigameworld.MiniGameWorldMain;
import com.worldbiomusic.minigameworld.managers.MiniGameManager;
import com.worldbiomusic.minigameworld.minigameframes.MiniGame;
import com.worldbiomusic.minigameworld.util.Setting;
import com.worldbiomusic.minigameworld.util.Utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

public class CommonEventListener implements Listener {
	/*
	 * Set event handler to classes that MiniGame can use with classgraph library
	 */
	private MiniGameManager minigameManager;

	public CommonEventListener(MiniGameManager minigameManager) {
		this.minigameManager = minigameManager;

		this.registerAllEventListener();
	}

	/**
	 * Register all events<br>
	 * - Spigot doesn't need other setup<be>
	 * - Paper needs additional setup like below<br>
	 * 
	 */
	private void registerAllEventListener() {
//		Utils.info("[ Register EventHandler ]");
//		Utils.info("wait for all EventHandler registration...");
//		Utils.info("Event class name: " + Event.class.getName());
//		long startTime = System.currentTimeMillis();

		boolean isPaper = false;
		if (Utils.getServerFile("cache").exists()) {
			isPaper = true;
		}

		// set cache directory can be found
		List<URL> cacheDirJarURLs = new ArrayList<>();
		if (isPaper) {
			for (String cpEntry : System.getProperty("java.class.path").split(File.pathSeparator)) {
				File cpFile = new File(cpEntry);
				if (cpFile.canRead()) {
					File cachedDir = new File(cpFile.isDirectory() ? cpFile : cpFile.getParentFile(), "cache");
					if (cachedDir.canRead() && cachedDir.isDirectory()) {
						try {
							for (File cachedDirJar : cachedDir.listFiles()) {
								if (cachedDirJar.getName().toLowerCase().endsWith(".jar")) {
									cacheDirJarURLs.add(cachedDirJar.toURI().toURL());
								}
							}
						} catch (MalformedURLException e) {
							// Ignore
						}
					}
				}
			}
		}
		
		
		ClassGraph classGraph = new ClassGraph();
		if (!cacheDirJarURLs.isEmpty()) {
			classGraph.addClassLoader(new URLClassLoader(cacheDirJarURLs.toArray(new URL[0])));
		}

		// find classes
		ClassInfoList events = classGraph.enableClassInfo().scan() // you should use try-catch-resources instead
				.getClassInfo(Event.class.getName()).getSubclasses().filter(info -> !info.isAbstract());

		Listener listener = new Listener() {
		};
		EventExecutor executor = (ignored, event) -> onEvent(event);

		try {
			for (ClassInfo event : events) {
				// noinspection unchecked
				@SuppressWarnings("unchecked")
				Class<? extends Event> eventClass = (Class<? extends Event>) event.loadClass(); // Class.forName(event.getName());

				if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(
						method -> method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
					// We could do this further filtering on the ClassInfoList instance instead,
					// but that would mean that we have to enable method info scanning.
					// I believe the overhead of initializing ~20 more classes
					// is better than that alternative.

					Bukkit.getPluginManager().registerEvent(eventClass, listener, EventPriority.HIGHEST, executor,
							MiniGameWorldMain.getInstance());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
//			throw new AssertionError("Scanned class wasn't found", e);
		}

		// String[] eventNames = events.stream()
		// .map(info -> info.getName().substring(info.getName().lastIndexOf('.') + 1))
		// .toArray(String[]::new);
		//
		// Bukkit.getLogger().info("List of events: " + String.join(", ", eventNames));
		// Bukkit.getLogger().info("registered EventHandler: " + eventCount);

//		Utils.info("Events found: " + events.size());
//		Utils.info("HandlerList size: " + HandlerList.getHandlerLists().size());
//		long takenTime = System.currentTimeMillis() - startTime;
//		Utils.info("Duration: " + takenTime / 1000 + " secs");
	}

	private Object onEvent(Event event) {
		/*
		 * pass event
		 */
		this.minigameManager.passEvent(event);
		return null;
	}

	/*
	 * Join / Quit
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.minigameManager.processPlayerJoinWorks(e.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		this.minigameManager.processPlayerQuitWorks(p);

		// minigame exception
		MiniGame.Exception exception = MiniGame.Exception.PLAYER_QUIT_SERVER;
		exception.setDetailedObj(e);

		this.minigameManager.createException(p, exception);
	}

	/*
	 * Inventory
	 */
	@EventHandler
	private void checkPlayerClickMiniGameGUI(InventoryClickEvent e) {
		this.minigameManager.getMiniGameMenuManager().processInventoryEvent(e);
	}

	@EventHandler
	private void checkPlayerCloseMiniGameGUI(InventoryCloseEvent e) {
		this.minigameManager.getMiniGameMenuManager().processInventoryEvent(e);
	}

	/*
	 * MiniGame Sign
	 */
	@EventHandler
	private void checkPlayerTryingMiniGameSign(PlayerInteractEvent e) {
		// check player is trying to join or leaving with sign
		Player p = e.getPlayer();
		// process
		Block block = e.getClickedBlock();
		if (block == null) {
			return;
		}
		
		if(block.getState() instanceof Sign) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Sign sign = (Sign) block.getState();
				String[] lines = sign.getLines();
				String minigame = lines[0];
				String title = lines[1];

				// check minigameSign option
				boolean minigameSign = (boolean) this.minigameManager.getSettings().get(Setting.SETTINGS_MINIGAME_SIGN);

				if (minigame.equals("[MiniGame]") || minigame.equals("[Leave MiniGame]")) {
					if (!minigameSign) {
						Utils.sendMsg(p, Setting.SETTINGS_MINIGAME_SIGN + " option is false");
						return;
					}
					// check sign
					if (minigame.equals("[MiniGame]")) {
						// join
						this.minigameManager.joinGame(p, title);
					} else if (minigame.equals("[Leave MiniGame]")) {
						// leave
						this.minigameManager.leaveGame(p);
					}

				}
			}
		}
	}

}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
