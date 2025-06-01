package com.paradise_seeker.game.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;

/**
 * Interface render Player
 * Tuân thủ nguyên tắc SRP - chỉ chịu trách nhiệm về rendering
 */
public interface PlayerRenderer {

    /**
     * Render player chính
     */
    void render(Player player, SpriteBatch batch);

    /**
     * Render khi player đang di chuyển
     */
    void renderMovement(Player player, SpriteBatch batch);

    /**
     * Render khi player đứng yên
     */
    void renderIdle(Player player, SpriteBatch batch);

    /**
     * Render khi player tấn công
     */
    void renderAttack(Player player, SpriteBatch batch);

    /**
     * Render khi player giơ khiên
     */
    void renderShield(Player player, SpriteBatch batch);

    /**
     * Render khi player bị đánh
     */
    void renderHit(Player player, SpriteBatch batch);

    /**
     * Render khi player bị đánh nhưng có khiên
     */
    void renderShieldedHit(Player player, SpriteBatch batch);

    /**
     * Render khi player chết
     */
    void renderDeath(Player player, SpriteBatch batch);

    /**
     * Render hiệu ứng khói
     */
    void renderSmoke(Player player, SpriteBatch batch);
}
