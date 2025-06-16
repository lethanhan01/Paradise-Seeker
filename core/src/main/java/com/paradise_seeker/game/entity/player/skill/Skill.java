package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.math.Rectangle;

public interface Skill {
    boolean canUse(long now);
    void update(long now);
    void castSkill(float atk, float x, float y, String direction);  // Bắn theo hướng
    void castSkill(float atk, Rectangle playerBounds, String direction); // Bắn theo hướng từ player
}
