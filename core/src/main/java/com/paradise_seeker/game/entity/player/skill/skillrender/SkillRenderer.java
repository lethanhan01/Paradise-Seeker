package com.paradise_seeker.game.entity.player.skill.skillrender;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.skill.PlayerSkill;

public interface SkillRenderer {
    void render(SpriteBatch batch, PlayerSkill skill);
} 