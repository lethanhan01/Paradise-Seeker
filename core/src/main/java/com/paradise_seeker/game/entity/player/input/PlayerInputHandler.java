package com.paradise_seeker.game.entity.player.input;

import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.screen.GameScreen;

public interface PlayerInputHandler {

    /**
     * Xử lý tất cả input của player
     */
    void handleInput(Player player, float deltaTime, GameMap gameMap);

    /**
     * Xử lý di chuyển
     */
    void handleMovement(Player player, float deltaTime, GameMap gameMap);

    /**
     * Xử lý dash
     */
    void handleDash(Player player, GameMap gameMap);

    /**
     * Xử lý tấn công
     */
    void handleAttack(Player player, GameMap gameMap);

    /**
     * Xử lý kỹ năng
     */
    void handleSkills(Player player);
    /**
     * Xử lý tương tác với NPC
     */
    void handleNPCInteraction(Player player, GameMap gameMap);
    /**
     * Xử lý phím zoom màn hình
     */
    void handleZoomInput(GameScreen gameScreen);
    /**
	 * Xử lý tương tác với sách
	 */
    void handleBook(GameScreen gameScreen, Player player);
    /**
	 * Xử lý tương tác với rương
	 */
    void handleChest(GameScreen gameScreen, Player player);

}
