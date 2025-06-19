package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.screen.GameScreen;

import java.util.List;
import java.util.ArrayList;

public class PlayerSkill1 extends PlayerSkill {
    private static final float MIN_X = 0f;
    private static final float MAX_X = 100f;
    private static final float MIN_Y = 0f;
    private static final float MAX_Y = 100f;

    private float posX, posY;
    private float startX, startY;
    private float speed = 10f;
    private float maxDistance = 20f;
    private float skillDamage;
    private boolean isFlying = false;
    private Rectangle hitbox;
    private String direction;
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

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
            this.posX = x;
            this.posY = y;
            this.startX = x;
            this.startY = y;
            this.direction = direction;
            this.animation = skillAnimations.get(direction);
            this.isFlying = true;
            this.stateTime = 0f;
            this.skillDamage = atk * 2 * damageMultiplier;
            this.hitbox = new Rectangle(posX, posY, 1f, 1f);
            setLastUsedTime(System.currentTimeMillis());
        }
    }

    @Override
    public void castSkill(float atk, Rectangle bounds, String direction) {
        float x = bounds.x + bounds.width / 2f;
        float y = bounds.y + bounds.height / 2f;
        if (direction.equals("up") || direction.equals("down")) {
            x -= 0.5f;
        }
        if (direction.equals("left") || direction.equals("right")) {
            y -= 0.5f;
        }
        castSkill(atk, x, y, direction);
    }

    @Override
    public void updateSkill(float delta, List<Monster> monsters) {
        if (!isFlying) return;
        switch (direction) {
            case "up": posY += speed * delta; break;
            case "down": posY -= speed * delta; break;
            case "left": posX -= speed * delta; break;
            case "right": posX += speed * delta; break;
        }
        stateTime += delta;
        float dx = posX - startX;
        float dy = posY - startY;
        if (Math.sqrt(dx*dx + dy*dy) > maxDistance) {
            isFlying = false;
            return;
        }
        if (hitbox != null) {
            hitbox.setPosition(posX, posY);
        }
        for (Monster monster : monsters) {
            if (isFlying && !monster.isDead() && hitbox != null && monster.getBounds().overlaps(hitbox)) {
                monster.takeHit(skillDamage);
                isFlying = false;
                break;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isFlying) return;
        if (animation != null) {
            TextureRegion frame = animation.getKeyFrame(stateTime, false);
            batch.draw(frame, posX, posY, 1f, 1f);
        }
    }
}