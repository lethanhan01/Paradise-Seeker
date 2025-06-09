package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public abstract class NPC extends Character  {
    public String dialogue;
    public boolean isTalking ;
    public boolean hasTalked ;

    public NPC() {
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.atk = 0;
        this.speed = 2f;
        this.x = 0;
        this.y = 0;
        this.dialogue = "";
        this.isTalking = false;
        this.hasTalked = false;
    }

    @Override
    public void takeHit(float damage) {
        // NPC không thể bị thương, có thể để trống hoặc ghi đè nếu cần
    }
    @Override
    public Rectangle getBounds() {

        return bounds;
    }
    @Override
    public void onCollision(Collidable other) {
        //
    }
    @Override
    public void act(float deltaTime, GameMap map) {
        // Cập nhật trạng thái NPC nếu cần
        // Ví dụ: di chuyển, thay đổi lời thoại, v.v.
    }
    public void interact(Player player) {
        if (dialogue != null && !dialogue.isEmpty()) {
            // Hiển thị lời thoại cho người chơi
            System.out.println("NPC says: " + dialogue);
        } else {
            System.out.println("NPC has nothing to say.");
        }
    }

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}
}
