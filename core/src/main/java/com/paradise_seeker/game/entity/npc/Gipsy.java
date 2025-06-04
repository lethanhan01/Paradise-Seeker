package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.map.GameMap;


/**
 * Class Gipsy đại diện cho một NPC có chức năng đặc biệt trong game.
 * Class này thể hiện việc áp dụng nguyên tắc SRP bằng cách phân tách các trách nhiệm:
 * - Animation: giao cho NPCAnimationManager
 * - Trạng thái: giao cho NPCStateManager
 * - Đối thoại: giao cho DialogueManager
 */
public class Gipsy extends NPC implements Collidable {
    public NPCAnimationManager animationManager;

    public NPCStateManager stateManager;

    public  DialogueManager dialogueManager;

    public float spriteWidth = 3f;
    public float spriteHeight = 3f;


    public Gipsy(float x, float y) {
        super(); // Gọi constructor của lớp cha NPC
        this.x = x;
        this.y = y;
        this.spriteWidth = 3f;
        this.spriteHeight = 3f;
        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight);

        // Khởi tạo các manager
        this.animationManager = new NPCAnimationManager();
        this.stateManager = new NPCStateManager();
        this.dialogueManager = new DialogueManager();
    }

    /**
     * Constructor mặc định, đặt vị trí tại (0,0)
     */
    public Gipsy() {
        this(0, 0);
    }

    /**
     * Cập nhật kích thước và vị trí bounds của NPC
     */
    public void updateBounds() {
        if (bounds != null) {
            bounds.setSize(spriteWidth, spriteHeight);
            bounds.setPosition(x, y);
        }
    }

    /**
     * Thực hiện các hành động của NPC trong mỗi frame
     */
    @Override
    public void act(float deltaTime, GameMap map) {
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

    /**
     * Cài đặt trạng thái nói chuyện cho NPC
     *
     * @param talking true nếu NPC đang nói chuyện, false nếu không
     */

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

    /**
     * Mở rương (chức năng đặc biệt của Gipsy)
     */
    public void openChest() {
        if (stateManager.isChestOpened() || stateManager.isOpeningChest()) {
            return;
        }

        animationManager.setOpenChestAnimation();
        stateManager.startChestOpening();
    }

    /**
     * Kiểm tra xem rương đã được mở và hoàn thành animation mở rương chưa
     *
     * @return true nếu rương đã mở và đã hoàn thành animation, false nếu không
     */
    public boolean isChestOpenAndFinished() {
        return stateManager.isChestOpenAndFinished();
    }

    // ---- Delegate methods for DialogueManager ----

    /**
     * Kiểm tra xem NPC có dòng thoại tiếp theo không
     */
    public boolean hasNextLine() {
        return dialogueManager.hasNextLine();
    }

    /**
     * Chuyển sang dòng thoại tiếp theo
     */
    public void nextLine() {
        dialogueManager.nextLine();
    }

    /**
     * Lấy dòng thoại hiện tại
     */
    public String getCurrentLine() {
        return dialogueManager.getCurrentLine();
    }

    /**
     * Đặt lại đối thoại về dòng đầu tiên
     */
    public void resetDialogue() {
        dialogueManager.resetDialogue();
    }

    /**
     * Kiểm tra xem có nên hiển thị các tùy chọn đối thoại không
     */
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
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}
}
