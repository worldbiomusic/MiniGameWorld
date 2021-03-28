# 미니게임 메이커
미니게임 프레임워크

# 미니게임 제작 주의사항
- 미니게임은 MiniGame 클래스를 상속해야 만든다
- 미니게임에서 변수 세팅은(예. 시간 재는 task) MiniGame 클래스의 initGameSetting() 메소드를 
오버라이드해서 세팅해야 한다.
- 플레이어 예외처리는 미니게임 handleGameException() 에서 후속 처리 가능
- 게임내 버그, 예외처리는 구현하는 미니게임에서 담당해서 처리해야 함
- 이벤트 처리 조건은 단순히 미니게임에 참여중인 플레이어만 확인하므로, 미니게임 환경을 신경써서 잘 조성해야 함
> 예시: 돌과 모래를 번갈아 바뀌며 부수는 미니게임이면 건축요소에 돌과 모래가 한 블럭만 만들게 한다    
- PVP관련에서 플레이어가 죽게 놯두지 말고, 체력이 0이하로 떨어졌을 떄 죽음으로 판정한다

# 사용가능한 이벤트 목록
- PlayerInteractEvent
- BlockBreakEvent
- BlockPlaceEvent
- EntityDamageEvent(EntityDamageByEntityEvent, EntityDamageByBlockEvent)
- 대부분 PlayerEvent 서브 클래스 사용가능(추가예정)
