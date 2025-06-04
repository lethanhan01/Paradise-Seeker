package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.main.GameScreen;
import com.paradise_seeker.game.entity.monster.Monster;

import java.util.List;

public class PlayerSkill1 extends PlayerSkill {
    public PlayerSkill1() {
        super(10, 500); // mana, cooldown
        loadSkillAnimations();
    }

    protected void loadSkillAnimations() {
        String[] directions = {"up", "down", "left", "right"};
        for (String dir : directions) {
            try {
                String path = "images/Entity/skills/PlayerSkills/Skill1/Skill1_" + dir + ".png";
                Texture sheet = new Texture(path);
                TextureRegion[] frames;

                if (dir.equals("left") || dir.equals("right")) {
                    TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 4, sheet.getHeight());
                    frames = new TextureRegion[4];
                    for (int i = 0; i < 4; i++) frames[i] = tmp[0][i];
                } else {
                    TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth(), sheet.getHeight() / 4);
                    frames = new TextureRegion[4];
                    for (int i = 0; i < 4; i++) frames[i] = tmp[i][0];
                }

                skillAnimations.put(dir, new Animation<>(0.1f, frames));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void castSkill(float atk, float x, float y, String direction) {
        if (canUse(System.currentTimeMillis())) {
            Animation<TextureRegion> anim = skillAnimations.get(direction);
            LaserBeam laser = new LaserBeam(x, y, atk * 2 * damageMultiplier, direction, anim);
            GameScreen.activeProjectiles.add(laser);
            setLastUsedTime(System.currentTimeMillis());
        }
    }

    @Override
    public void castSkill(float atk, Rectangle bounds, String direction) {
        float x = bounds.x + bounds.width / 2f;
        float y = bounds.y + bounds.height / 2f;
        castSkill(atk, x, y, direction);
    }

    public void updateSkill(float delta, List<Monster> monsters) {
        // PlayerSkill1 không cần logic update riêng, để trống
    }

    @Override
    public void render(SpriteBatch batch) {
        // PlayerSkill1 không cần render riêng, để trống
    }
}
