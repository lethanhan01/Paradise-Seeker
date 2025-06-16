package com.paradise_seeker.game.entity.player.inventory;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.object.item.Item;

public interface Inventory {
	public void addItemToInventory(Item newItem, Rectangle playerBounds);
	public void removeItem(Item item);
	public int getItemCount(String itemId);
	public boolean hasSpaceLeft();
}
