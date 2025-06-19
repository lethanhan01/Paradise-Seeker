package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.skill.skillanimation.PlayerSkill1Animation;
import com.paradise_seeker.game.entity.player.skill.skillrender.Skill1Renderer;

import java.util.List;

public class PlayerSkill1 extends PlayerSkill {
    public static final float MIN_X = 0f;
    public static final float MAX_X = 100f;
    public static final float MIN_Y = 0f;
    public static final float MAX_Y = 100f;

    private float posX, posY;
    private float startX, startY;
    private float speed = 10f;
    private float maxDistance = 20f;
    private float skillDamage;
    private boolean isFlying = false;
    private Rectangle hitbox;
    private String direction;
    private float stateTime = 0f;
    private PlayerSkill1Animation playerSkill1Animation;
    private Skill1Renderer skill1Renderer;


    public PlayerSkill1() {
        super(10, 500); // mana, cooldown
        this.playerSkill1Animation = new PlayerSkill1Animation(null);
        this.skill1Renderer = new Skill1Renderer();
    }

    @Override
    public void castSkill(float atk, float x, float y, String direction) {
        if (canUse(System.currentTimeMillis())) {
            this.posX = x;
            this.posY = y;
            this.startX = x;
            this.startY = y;
            this.direction = direction;
            this.playerSkill1Animation.loadAnimation(direction);
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
        if (isFlying && hitbox != null) {
            for (Monster monster : monsters) {
                if (!monster.statusManager.isDead() && monster.getBounds().overlaps(hitbox)) {
                    monster.takeHit(skillDamage);
                    isFlying = false;
                    break;
                }
            }
        }
    }
    public boolean isFlying() {
        return isFlying;
    }
    public PlayerSkill1Animation getPlayerSkill1Animation() {
        return playerSkill1Animation;
    }
    public Skill1Renderer getSkill1render(){
        return skill1Renderer;
    }
    public String getDirection() {
        return direction;
    }
    public float getStateTime() {
        return stateTime;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
}