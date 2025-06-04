package com.paradise_seeker.game.entity.player;

import com.paradise_seeker.game.map.GameMap;

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
     * Xử lý khiên
     */
    void handleShield(Player player);

    /**
     * Xử lý kỹ năng
     */
    void handleSkills(Player player);

    /**
     * Xử lý tương tác với NPC
     */
    void handleNPCInteraction(Player player, GameMap gameMap);
    /**
     * Xử lý phím cheat (R - hồi máu mana)
     */

}
