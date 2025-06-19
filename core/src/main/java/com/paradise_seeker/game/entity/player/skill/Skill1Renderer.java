package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Skill1Renderer implements SkillRenderer {
    @Override
    public void render(SpriteBatch batch, PlayerSkill skill) {
        if (!(skill instanceof PlayerSkill1)) return;
        PlayerSkill1 s = (PlayerSkill1) skill;
        if (!s.isFlying()) return;
        if (s.getPlayerSkill1Animation() != null) {
            Animation<TextureRegion> anim = s.getPlayerSkill1Animation().getSkillAnimation(s.getDirection());
            if (anim != null) {
                TextureRegion frame = anim.getKeyFrame(s.getStateTime(), false);
                batch.draw(frame, s.getPosX(), s.getPosY(), 1f, 1f);
            }
        }
    }
} 