package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.map.GameMap;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;
// Add this import at the top
import com.paradise_seeker.game.rendering.renderer.NPCRenderer;
import com.paradise_seeker.game.rendering.renderer.NPCRendererImpl;

import java.util.ArrayList;
import java.util.List;

public class Gipsy extends NPC {
    public NPCAnimationManager animationManager;
    public NPCStateManager stateManager;
    public DialogueManager dialogueManager;

    // Add this field
    private NPCRenderer renderer;

    public float spriteWidth = 1.9f;
    public float spriteHeight = 1.8f;

    public Gipsy(float x, float y) {
        super(); // Gọi constructor của lớp cha NPC
        this.x = x;
        this.y = y;
        this.spriteWidth = 1.9f;
        this.spriteHeight = 1.8f;
        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight);

        // Khởi tạo các manager
        this.animationManager = new NPCAnimationManager();
        this.stateManager = new NPCStateManager();
        this.dialogueManager = new DialogueManager();

        // Initialize the renderer
        this.renderer = new NPCRendererImpl(this.animationManager);

        // Thiết lập câu thoại mặc định cho Gipsy
        List<String> defaultDialogue = new ArrayList<>();
        defaultDialogue.add("Hello, traveler!");
        defaultDialogue.add("I am Gipsy, a wandering merchant.");
        defaultDialogue.add("Would you like to open a chest?");
        dialogueManager.setDialogue(defaultDialogue);
    }

    public Gipsy() {
        this(0, 0);
    }

    public void updateBounds() {
        if (bounds != null) {
            bounds.setSize(spriteWidth, spriteHeight);
            bounds.setPosition(x, y);
        }
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void act(float deltaTime, GameMap map) {
        // Update renderer state time
        if (renderer instanceof NPCRendererImpl) {
            ((NPCRendererImpl) renderer).update(deltaTime);
        }

        // Cập nhật animation dựa trên trạng thái hiện tại
        animationManager.update(deltaTime, stateManager.isOpeningChest(), stateManager.isChestOpened());

        // Xử lý hoàn thành animation mở rương
        if (stateManager.isOpeningChest() && animationManager.isAnimationFinished()) {
            stateManager.completeChestOpening();
            animationManager.setChestOpenedAnimation();
        }

        // Cập nhật trạng thái NPC từ lớp cha
        super.isTalking = stateManager.isTalking();
        super.hasTalked = stateManager.hasTalked();
        updateBounds();
    }

    @Override
    public void setTalking(boolean talking) {
        if (super.isTalking != talking) {
            super.isTalking = talking;
            stateManager.setTalking(talking);

            // Không thay đổi animation nếu đang mở rương
            if (!stateManager.isOpeningChest()) {
                if (talking) {
                    animationManager.setTalkingAnimation();
                } else if (stateManager.isChestOpened()) {
                    animationManager.setChestOpenedAnimation();
                } else {
                    animationManager.setIdleAnimation();
                }
            }
        }
    }

    public void openChest() {
        if (stateManager.isChestOpened() || stateManager.isOpeningChest()) {
            return;
        }

        animationManager.setOpenChestAnimation();
        stateManager.startChestOpening();
    }

    public boolean isChestOpenAndFinished() {
        return stateManager.isChestOpenAndFinished();
    }

    public boolean hasNextLine() {
        return dialogueManager.hasNextLine();
    }

    public void nextLine() {
        dialogueManager.nextLine();
    }

    public String getCurrentLine() {
        return dialogueManager.getCurrentLine();
    }

    public void resetDialogue() {
        dialogueManager.resetDialogue();
    }

    public boolean shouldShowOptions() {
        // Giả định rằng các tùy chọn nên hiển thị khi đã đến dòng đối thoại cuối cùng
        // và NPC đang trong trạng thái nói chuyện và chưa mở rương
        return !hasNextLine() && stateManager.isTalking() && !stateManager.isChestOpened();
    }

    /**
     * Kiểm tra xem rương đã được mở chưa
     */
    public boolean isChestOpened() {
        return stateManager.isChestOpened();
    }

    public boolean hasTalked() {
    	return stateManager.hasTalked();
	}

	@Override
	protected void loadTexture() {
		// Load default texture for Gipsy
		texture = new Texture("images/Entity/characters/NPCs/npc1/act3/npc120.png");
	}

	@Override
	public void render(SpriteBatch batch) {
		// Use the renderer instead of calling animationManager.render()
		if (renderer != null) {
			renderer.render(this, batch);
		} else if (texture != null) {
			// Fallback to texture if renderer is not available
			batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
}