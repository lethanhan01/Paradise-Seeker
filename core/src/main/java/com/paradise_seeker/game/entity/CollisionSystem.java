package com.paradise_seeker.game.entity;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.object.item.ATKPotion;
import com.paradise_seeker.game.object.item.HPPotion;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.object.item.MPPotion;
import com.paradise_seeker.game.object.item.Skill1Potion;
import com.paradise_seeker.game.object.item.Skill2Potion;
import com.paradise_seeker.game.ui.HUD;

public class CollisionSystem {
    public List<Collidable> collidables;
    public List<HPPotion> hpItems;
    public List<MPPotion> mpItems;
    public List<ATKPotion> atkItems;
    public List<Skill1Potion> skill1Items;
    public List<Skill2Potion> skill2Items;
    public static void checkCollisions(Player player, List<Collidable> collidables) {
        for (Collidable c : collidables) {
            if (player.getBounds().overlaps(c.getBounds())) {
                c.onCollision(player);
            }
        }
    }

    public void checkCollisions(Player player, HUD hud) {
        CollisionSystem.checkCollisions(player, collidables);
        List<List<? extends Item>> allItemLists = Arrays.asList(
            hpItems, mpItems, atkItems, skill1Items, skill2Items
        );
        for (List<? extends Item> itemList : allItemLists) {
            for (Item item : itemList) {
                if (item.isActive() && item.getBounds().overlaps(player.getBounds())) {
                    boolean canStack = false;
                    boolean hasStackWithSpace = false;
                    if (item.isStackable()) {
                        for (Item invItem : player.getInventory()) {
                            if (invItem.canStackWith(item) && invItem.getCount() < invItem.getMaxStackSize()) {
                                hasStackWithSpace = true;
                                break;
                            }
                        }
                        canStack = hasStackWithSpace;
                    }
                    boolean isFull = player.getInventory().size() >= player.getInventorySize();
                    if (!canStack && isFull) {
                        if (hud != null) hud.showNotification("> Inventory is full!");
                    } else {
                        item.onCollision(player);
                        if (hud != null) hud.showNotification("> Picked up: " + item.getName());
                    }
                }
            }
        }
    }

    public boolean isBlocked(Rectangle nextBounds, Collidable self) {
        for (Collidable c : collidables) {
            if (c == self) continue; // tránh kiểm tra chính mình
            if (c.getBounds().overlaps(nextBounds)) return true;
        }
        return false;
    }
    public boolean isBlocked(Rectangle nextBounds) {

        return isBlocked(nextBounds, null);
    }
}
