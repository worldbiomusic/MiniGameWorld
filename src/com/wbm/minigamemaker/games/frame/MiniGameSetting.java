package com.wbm.minigamemaker.games.frame;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class MiniGameSetting {

	/*
	 * 미니게임 설정값 관리 클래스
	 * 
	 * 파일 관리: minigames.json파일에 속성값이 있는지 여부
	 * 
	 * 기본값: 미니게임의 기본 세팅값
	 */
	// 파일 관리 o
	// 기본값: 없음
	private String title;
	// 파일 관리 o
	// 기본값: new Location(Bukkit.getWorld("world"), 0, 4, 0)
	private Location location;
	// 파일 관리 o
	private int maxPlayerCount;
	// 파일 관리 o
	private int waitingTime;
	// 파일 관리 o
	private int timeLimit;
	// 파일 관리 o
	// 기본값: true
	private boolean active;
	// 파일 관리 x
	// 기본값: false
	private boolean settingFixed;
	// 파일 관리 x
	// 기본값: false
	private boolean scoreNotifying;
	// 파일 관리 x
	// 기본값: false
	// 설명: maxPlayerCount값이 강제 인원수로 설정됨
	private boolean forcePlayerCount;

	public MiniGameSetting(String title, Location location, int maxPlayerCount, int timeLimit, int waitingTime) {
		this.title = title;
		this.location = location;
		this.maxPlayerCount = maxPlayerCount;
		this.waitingTime = waitingTime;
		this.timeLimit = timeLimit;

		this.active = true;
		this.settingFixed = false;
		this.scoreNotifying = false;
		this.forcePlayerCount = false;
	}

	public void setSettingFixed(boolean settingFixed) {
		this.settingFixed = settingFixed;
	}

	public void setScoreNotifying(boolean scoreNotifying) {
		this.scoreNotifying = scoreNotifying;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setMaxPlayerCount(int maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setForcePlayerCount(boolean forcePlayerCount) {
		this.forcePlayerCount = forcePlayerCount;
	}

	// getter
	public String getTitle() {
		return title;
	}

	public Location getLocation() {
		return location;
	}

	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isSettingFixed() {
		return settingFixed;
	}

	public boolean isScoreNotifying() {
		return scoreNotifying;
	}

	public boolean isForcePlayerCount() {
		return forcePlayerCount;
	}

	public Map<String, Object> getFileSetting() {
		// return settings that exist in minigames.yml 
		Map<String, Object> setting = new HashMap<String, Object>();

		setting.put("title", this.getTitle());
		setting.put("location", this.getLocation());
		setting.put("maxPlayerCount", this.getMaxPlayerCount());
		setting.put("waitingTime", this.getWaitingTime());
		setting.put("timeLimit", this.getTimeLimit());
		setting.put("active", this.isActive());

		return setting;
	}

	public void setFileSetting(Map<String, Object> setting) {
		/*
		 * apply "maxPlayerCount", "waitingTime", "timeLimit" when "settingFixed" is false
		 */

		// title
		String title = (String) setting.get("title");
		this.setTitle(title);

		// location
		Location location = (Location) setting.get("location");
		this.setLocation(location);

		// when settingFixed is false: apply maxPlayerCount, timeLimit, waitingTime 
		if (!isSettingFixed()) {
			// maxPlayerCount
			int maxPlayerCount = (int) setting.get("maxPlayerCount");
			this.setMaxPlayerCount(maxPlayerCount);

			// waitingTime
			int waitingTime = (int) setting.get("waitingTime");
			this.setWaitingTime(waitingTime);

			// timeLimit
			int timeLimit = (int) setting.get("timeLimit");
			this.setTimeLimit(timeLimit);
		}

		// active
		boolean active = (boolean) setting.get("active");
		this.setActive(active);

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
