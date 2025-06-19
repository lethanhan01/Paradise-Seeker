package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface SkillAnimationManager {
    Animation<TextureRegion> getSkillAnimation(String direction);
    void dispose();
} 