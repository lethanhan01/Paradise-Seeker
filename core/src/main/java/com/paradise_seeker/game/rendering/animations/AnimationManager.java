package com.paradise_seeker.game.rendering.animations;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface AnimationManager {

    Animation<TextureRegion> getRunAnimation(String direction);

    Animation<TextureRegion> getIdleAnimation(String direction);

    Animation<TextureRegion> getAttackAnimation(String direction);

    Animation<TextureRegion> getHitAnimation(String direction);

    Animation<TextureRegion> getDeathAnimation(String direction);

    void dispose();

}
