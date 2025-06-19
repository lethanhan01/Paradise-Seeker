package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Skill2Renderer implements SkillRenderer {
    @Override
    public void render(SpriteBatch batch, PlayerSkill skill) {
        if (!(skill instanceof PlayerSkill2)) return;
        PlayerSkill2 s = (PlayerSkill2) skill;
        if (!s.isCasting()) return;
        if (s.getPlayerSkill2Animation() != null) {
            Animation<TextureRegion> anim = s.getPlayerSkill2Animation().getSkillAnimation(s.getDirection());
            if (anim != null) {
                TextureRegion frame = anim.getKeyFrame(s.getStateTime(), false);
                float realWidth = frame.getRegionWidth() * s.getScale();
                float realHeight = frame.getRegionHeight() * s.getScale();
                float drawX = s.getPosX() - realWidth / 2f;
                float drawY = s.getPosY() - realHeight / 2f;
                batch.draw(frame, drawX, drawY, realWidth, realHeight);
            }
        }
    }
} 