package com.paradise_seeker.game.object;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.player.Player;

public class Book extends GameObject {
	public Book(float x, float y) {
		super(x, y, 1f, 1f, "images/objects/book/book.png");
		this.bounds = new Rectangle(x, y, 1f, 1f); // Kích thước của sách
	}

	@Override
	public void onCollision(Collidable other) {
		if (other instanceof Player) {
			
		}
	}

	@Override
	public boolean isSolid() {
		// TODO Auto-generated method stub
		return false;
	}

}
