package com.paradise_seeker.game.object.item;

import com.paradise_seeker.game.entity.player.Player;

public class Skill2Potion extends Item  {
    private float damageMultiplier ;

    public Skill2Potion(float x, float y, float size, String texturePath) {
        super(x, y, size, texturePath);
        this.damageMultiplier = 0.25f;
        this.name = "Skill 2 Potion";
        this.description = "Skill 2's DMG +0.25x.";
        this.stackable = true;
        this.maxStackSize = 5; // Giới hạn số lượng tối đa trong một stack
    }

    @Override
    public void onCollision(Player player) {
        if (active) {
            player.addItemToInventory(this);
			active = false;
        }
    }

    public void isUsed(Player player) {
		player.playerSkill2.setDamageMultiplier(this.damageMultiplier); // Nhân 2 damage skill2
	}
    public boolean canStackWith(Item other) {
		if (!(other instanceof Skill2Potion)) return false;
		Skill2Potion otherSkill = (Skill2Potion) other;
		return super.canStackWith(other) && this.name.equals(otherSkill.name);
	}
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
