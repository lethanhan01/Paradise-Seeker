package com.paradise_seeker.game.entity.player;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface PlayerAnimationManager {
    /**
     * Load tất cả animation cần thiết cho Player
     */
    void loadAnimations();

    /**
     * Lấy animation di chuyển theo hướng
     */
    Animation<TextureRegion> getRunAnimation(String direction);

    /**
     * Lấy animation đứng yên theo hướng
     */
    Animation<TextureRegion> getIdleAnimation(String direction);

    /**
     * Lấy animation tấn công theo hướng
     */
    Animation<TextureRegion> getAttackAnimation(String direction);

    /**
     * Lấy animation leo trèo theo hướng
     */
    Animation<TextureRegion> getClimbAnimation(String direction);

    /**
     * Lấy animation đẩy theo hướng
     */
    Animation<TextureRegion> getPushAnimation(String direction);

    /**
     * Lấy animation bị đánh theo hướng
     */
    Animation<TextureRegion> getHitAnimation(String direction);

    /**
     * Lấy animation bị đánh khi có khiên theo hướng
     */
    Animation<TextureRegion> getShieldedHitAnimation(String direction);

    /**
     * Lấy animation giơ khiên theo hướng
     */
    Animation<TextureRegion> getShieldAnimation(String direction);

    /**
     * Lấy animation chết
     */
    Animation<TextureRegion> getDeathAnimation();

    /**
     * Lấy animation khói
     */
    Animation<TextureRegion> getSmokeAnimation();

    /**
     * Dispose tất cả texture để giải phóng memory
     */
    void dispose();
}
