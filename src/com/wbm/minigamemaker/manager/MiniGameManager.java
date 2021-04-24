package com.wbm.minigamemaker.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wbm.minigamemaker.games.FitTool;
import com.wbm.plugin.util.BroadcastTool;

public class MiniGameManager {
	// API 용 클래스
	// Singleton 사용

	// Singleton 객체
	private static MiniGameManager instance = new MiniGameManager();

	// 미니게임 관리 리스트
	private List<MiniGame> minigames;

	List<Class<? extends Event>> possibleEventList;

	// getInstance() 로 접근해서 사용
	private MiniGameManager() {
		// 미니게임 리스트 초기화
		this.minigames = new ArrayList<>();

		// 처리 가능한 이벤트 목록 초기화
		this.possibleEventList = new ArrayList<>();
		// 처리가능한 이벤트 목록들 (Player 확인가능한 이벤트)
		// PlayerEvent 서브 클래스들은 대부분 가능(Chat, OpenChest, CommandSend 등등)
		this.possibleEventList.add(PlayerInteractEvent.class);
		this.possibleEventList.add(BlockBreakEvent.class);
		this.possibleEventList.add(BlockPlaceEvent.class);
		this.possibleEventList.add(EntityDamageEvent.class);

		// 예시 미니게임
		this.registerMiniGame(new FitTool());
	}

	public static MiniGameManager getInstance() {
		return instance;
	}

	public void joinGame(Player p, String title) {
		/*
		 * 플레이어가 미니게임에 참여하는 메소드
		 */
		MiniGame game = this.getMiniGame(title);
		game.joinGame(p);
	}

	public boolean isPossibleEvent(Event event) {
		// 미니게임에서 처리가능한 이벤트인지 체크
		return this.possibleEventList.contains(event.getClass());
	}

	public boolean processEvent(Event e) {
		// 허용되는 이벤트만 처리
		if (this.possibleEventList.contains(e.getClass())) {
			// 이벤트에서 플레이어 가져와서 플레이중인 미니게임에 이벤트 넘기기
			Player player = this.getPlayerFromEvent(e);
			if (player == null) {
				return false;
			}

			MiniGame playingGame = this.getPlayingGame(player);
			// 미니게임 플레이 중인 플레이어 일때
			if (playingGame == null) {
				return false;
			}
			playingGame.passEvent(e);
		}
		return true;
	}

	public void processException(Player p) {
		MiniGame playingGame = this.getPlayingGame(p);
		if (playingGame != null) {
			playingGame.handleException(p);
		}
	}

	private MiniGame getMiniGame(String title) {
		for (MiniGame game : this.minigames) {
			if (game.getTitle().equalsIgnoreCase(title)) {
				return game;
			}
		}
		return null;
	}

	private MiniGame getPlayingGame(Player p) {
		for (MiniGame game : this.minigames) {
			if (game.containsPlayer(p)) {
				return game;
			}
		}
		return null;
	}

	private Player getPlayerFromEvent(Event e) {
		if (e instanceof PlayerInteractEvent) {
			return ((PlayerInteractEvent) e).getPlayer();
		} else if (e instanceof BlockBreakEvent) {
			return ((BlockBreakEvent) e).getPlayer();
		} else if (e instanceof BlockPlaceEvent) {
			return ((BlockPlaceEvent) e).getPlayer();
		}
//		else if(e instanceof EntityDamageEvent) {
//			return (Player)((EntityDamageEvent) e).getEntity();
//		}
		return null;
	}

	public List<MiniGame> getMiniGameList() {
		return this.minigames;
	}

	public boolean registerMiniGame(MiniGame newGame) {
		// 같은 title이 있으면 등록 실패
		for (MiniGame game : this.minigames) {
			String newGameTitle = newGame.getTitle();
			if (game.getTitle().equalsIgnoreCase(newGameTitle)) {
				BroadcastTool.warn(newGameTitle + " minigame is not registered");
				BroadcastTool.warn("Cause: " + newGameTitle + " title is already exists");
				return false;
			}
		}

		// 게임 등록
		this.minigames.add(newGame);
		BroadcastTool.info(newGame.getTitle() + " minigame is registered");
		return true;
	}

	public boolean removeMiniGame(String title) {
		// 등록된 미니게임 삭제
		for (MiniGame game : this.minigames) {
			if (game.getTitle().equalsIgnoreCase(title)) {
				BroadcastTool.info(title + " minigame is removed");
				return this.minigames.remove(game);
			}
		}
		return false;
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
