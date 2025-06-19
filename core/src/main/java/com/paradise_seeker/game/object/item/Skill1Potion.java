package com.paradise_seeker.game.object.item;

import com.paradise_seeker.game.entity.player.Player;

public class Skill1Potion extends Item {
    private float damageMultiplier;

    public Skill1Potion(float x, float y, float size, String texturePath) {
        super(x, y, size, texturePath);
        this.damageMultiplier = 0.25f;
        this.name = "Skill 1 Potion";
        this.description = "Skill 1's DMG + 0.25x.";
        this.stackable = true;
        this.maxStackSize = 5; // Giới hạn số lượng tối đa trong một stack
    }

    
    @Override
    public void isUsed(Player player) {
    	player.playerSkill1.setDamageMultiplier(this.damageMultiplier); // Nhân 2 damage skill1
	}


    public boolean canStackWith(Item other) {
		if (!(other instanceof Skill1Potion)) return false;
		Skill1Potion otherSkill = (Skill1Potion) other;
		return super.canStackWith(other) && this.name.equals(otherSkill.name);
	}

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
