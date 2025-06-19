package com.paradise_seeker.game.object.item;

import com.paradise_seeker.game.entity.player.Player;

public class HPPotion extends Item {
    private int healAmount;

    public HPPotion(float x, float y, float size, String texturePath, int healAmount) {
        super(x, y, size, texturePath);
        this.healAmount = healAmount;
        this.name = "Health Potion";
        this.description = "Restores " + healAmount + " HP.";
        this.stackable = true;
        this.maxStackSize = 5; // Giới hạn số lượng tối đa trong một stack
    }
    @Override
    public void isUsed(Player player) {
        player.hp = Math.min(Player.MAX_HP, player.hp + healAmount);
    }
    public boolean canStackWith(Item other) {
        if (!(other instanceof HPPotion)) return false;
        HPPotion otherHP = (HPPotion) other;
        return super.canStackWith(other) && this.healAmount == otherHP.healAmount;
    }
    public void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}
