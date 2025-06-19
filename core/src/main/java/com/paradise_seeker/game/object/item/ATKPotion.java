package com.paradise_seeker.game.object.item;

import com.paradise_seeker.game.entity.player.Player;

public class ATKPotion extends Item {
    private int atkBoost;

    public ATKPotion(float x, float y, float size, String texturePath, int atkBoost) {
        super(x, y, size, texturePath);
        this.atkBoost = atkBoost;
        this.name = "Attack Boost";
        this.description = "Attack + " + atkBoost + ".";
        this.stackable = true;
        this.maxStackSize = 5; // Giới hạn số lượng tối đa trong một stack
    }


    @Override
    public void isUsed(Player player) {
		player.atk += atkBoost;

	}

	public boolean canStackWith(Item other) {
		if (!(other instanceof ATKPotion)) return false;
		ATKPotion otherATK = (ATKPotion) other;
		return super.canStackWith(other) && this.atkBoost == otherATK.atkBoost;
	}

	public void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}
