package com.paradise_seeker.game.entity.player.skill.skillanimation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public class PlayerSkill2Animation implements SkillAnimationManager {
    private Animation<TextureRegion> skillAnimation;
    public String[] frames;

    public PlayerSkill2Animation(Animation<TextureRegion> skillAnimation) {
        this.skillAnimation = skillAnimation;
    }

    public void loadAnimation(String direction) {
        try {
            String basePath = "images/Entity/skills/PlayerSkills/Skill2/";
            frames = new String[4];
            if (direction.equals("up")) {
                basePath += "len/lightning_skill1_frame";
            } else if (direction.equals("down")) {
                basePath += "xuong/lightning_skill1_frame";
            } else if (direction.equals("left")) {
                basePath += "trai/lightning_skill1_frame";
            } else if (direction.equals("right")) {
                basePath += "phai/lightning_skill1_frame";
            } else {
                return;
            }
            TextureRegion[] texFrames = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                texFrames[i] = new TextureRegion(new Texture(Gdx.files.internal(basePath + (i+1) + ".png")));
            }
            this.skillAnimation = new Animation<>(0.07f, texFrames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Animation<TextureRegion> getSkillAnimation(String direction) {
        return skillAnimation;
    }

    @Override
    public void dispose() {
    }
}