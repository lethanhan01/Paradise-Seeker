package com.paradise_seeker.game.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.object.item.ATKPotion;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.object.item.Skill1Potion;
import com.paradise_seeker.game.object.item.Skill2Potion;
import com.badlogic.gdx.Gdx;

public class Chest extends GameObject{
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 1;
    private static final float FRAME_DURATION = 0.1f; // Thời gian mỗi frame
   // Vùng hiển thị
    public Rectangle innerBounds;

    private float stateTime = 0f;
    private boolean isOpened = false;
    private boolean animationFinished = false;

    private Texture chestSheet;
    private Animation<TextureRegion> chestOpenAnimation;
    private Array<Item> items;

    public Chest (float x, float y) {
        super(x, y, 3f, 3f, "images/objects/chest/chest hit animation.png");
        this.innerBounds = new Rectangle(x, y, 1f, 1f); // Vùng trigger nhỏ hơn
        items = Array.with(
        	    new Skill1Potion(x, y, 1f, "items/buff/potion12.png"), // Thêm các item vào kho
        	    new Skill2Potion(x, y, 1f, "items/buff/potion13.png"),
        	    new ATKPotion(x, y, 1f, "items/atkbuff_potion/potion15.png", 15)
        	);
        loadAnimation();
    }
    public void addItem(Item item) {
        items.add(item);
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    private void loadAnimation() {
        chestSheet = new Texture(Gdx.files.internal("images/objects/chest/chest hit animation.png"));
        TextureRegion[][] tmp = TextureRegion.split(
            chestSheet,
            chestSheet.getWidth() / FRAME_COLS,
            chestSheet.getHeight() / FRAME_ROWS
        );

        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        chestOpenAnimation = new Animation<>(FRAME_DURATION, frames);
    }

    public void open() {
        if (!isOpened) {
            isOpened = true;
            stateTime = 0f;
            animationFinished = false;
        }
    }

    public void update(float deltaTime) {
        if (isOpened && !animationFinished) {
            stateTime += deltaTime;
            if (chestOpenAnimation.isAnimationFinished(stateTime)) {
                animationFinished = true;
            }
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        if (isOpened) {
            if (animationFinished) {
                currentFrame = chestOpenAnimation.getKeyFrames()[FRAME_COLS - 1];
            } else {
                currentFrame = chestOpenAnimation.getKeyFrame(stateTime, false);
            }
        } else {
            currentFrame = chestOpenAnimation.getKeyFrame(0);
        }
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void dispose() {
        chestSheet.dispose();
    }

	@Override
	public Rectangle getBounds() {
		return innerBounds;  // Sử dụng vùng trigger thật khi kiểm tra va chạm
	}

	public void onPlayerCollision(Collidable other) {
		// Handle player collision with the chest
		((Player) other).blockMovement();
		if (isOpened || animationFinished) {
			return; // Chest is already opened or animation is finished
		}
		for (Item item : items) {
	        ((Player) other).addItemToInventory(item);
	        item.setActive(false);
	    }
		if (!isOpened) {
			open();
		}
	}

	public Array<Item> getItems() {
		return items;
	}
}
