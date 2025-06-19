package com.paradise_seeker.game.entity.player.skill.skillanimation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerSkill1Animation implements SkillAnimationManager {
    private Animation<TextureRegion> skillAnimation;

    public PlayerSkill1Animation(Animation<TextureRegion> skillAnimation) {
        this.skillAnimation = skillAnimation;
    }

    public void loadAnimation(String direction) {
        try {
            String path = "images/Entity/skills/PlayerSkills/Skill1/Skill1_" + direction + ".png";
            Texture sheet = new Texture(path);
            TextureRegion[] frames;
            if (direction.equals("left") || direction.equals("right")) {
                TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 4, sheet.getHeight());
                frames = new TextureRegion[4];
                for (int i = 0; i < 4; i++) frames[i] = tmp[0][i];
            } else {
                TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth(), sheet.getHeight() / 4);
                frames = new TextureRegion[4];
                for (int i = 0; i < 4; i++) frames[i] = tmp[i][0];
            }
            this.skillAnimation = new Animation<>(0.1f, frames);
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