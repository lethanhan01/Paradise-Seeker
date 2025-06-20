package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.*;

public abstract class PlayerSkill implements Skill{
    protected float manaCost;
    protected long cooldown;
    protected long lastUsedTime;
    protected float damageMultiplier = 1.0f;

    public PlayerSkill(float manaCost, long cooldown) {
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.lastUsedTime = 0;
    }

    public boolean canUse(long now) {
        return (now - lastUsedTime) >= cooldown;
    }

    public void setLastUsedTime(long time) {
        this.lastUsedTime = time;
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier += multiplier;
    }

    public float getdamageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public abstract void castSkill(float atk, float x, float y, String direction);
    @Override
    public abstract void castSkill(float atk, Rectangle bounds, String direction);

    @Override
    public void update(long now) {
    }

    public void updatePosition(Player player) {
    }

    public void updateSkill(float delta, List<Monster> monsters) {
    }
}
