package com.paradise_seeker.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.npc.NPC1;
import com.paradise_seeker.game.entity.object.item.Item;
import com.paradise_seeker.game.entity.skill.*;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.render.PlayerRenderer;
import com.paradise_seeker.game.animation.PlayerAnimationManager;
import com.paradise_seeker.game.animation.PlayerAnimationManagerImpl;
import com.paradise_seeker.game.input.PlayerInputHandler;
import com.paradise_seeker.game.input.PlayerInputHandlerImpl;
import com.paradise_seeker.game.inventory.PlayerInventoryManager;

public class Player extends Character {
    public static final int MAX_HP = 1000;
    public static final int MAX_MP = 100;
    private final float dashCooldown = 0f;
    private final float dashDistance = 2f;

    public PlayerSkill playerSkill1 = new PlayerSkill();
    public PlayerSkill playerSkill2 = new PlayerSkill();

    // Quản lý hiệu ứng smoke
    public SmokeManager smokeManager = new SmokeManager();

    public boolean showInteractMessage = false;

    public float speedMultiplier = 1f;
    private Vector2 lastPosition = new Vector2();

    // Quản lý inventory thông qua PlayerInventoryManager
    public PlayerInventoryManager inventoryManager;

    public float stateTime = 0f;
    public String direction = "down";
    public boolean isMoving = false;
    public boolean isAttacking = false;

    public float dashTimer = 0f;

    public boolean isShielding = false;
    public boolean isPaused = false;

    // New fields for MVC pattern
    public PlayerAnimationManager animationManager;
    public PlayerInputHandler inputHandler;
    public PlayerRenderer playerRenderer;

    // Tracking death state
    public boolean isDead = false;
    public boolean isHit = false;
    public boolean isShieldedHit = true;

    // Invulnerability system
    public boolean isInvulnerable = false;
    public float invulnerabilityTimer = 0f;
    public static final float INVULNERABILITY_DURATION = 0.5f; // Half second of invulnerability after getting hit

    public Player() {
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.hp = MAX_HP;
        this.mp = MAX_MP;
        this.atk = 20;
        this.speed = 5f;
        this.x = 0;
        this.y = 0;

        // Khởi tạo PlayerInventoryManager
        this.inventoryManager = new PlayerInventoryManager();

        // Initialize the dependencies
        this.animationManager = new PlayerAnimationManagerImpl();
        this.animationManager.loadAnimations();
        this.inputHandler = new PlayerInputHandlerImpl();
    }

    public Player(Rectangle bounds, float hp, float mp, float maxHp, float maxMp,  float atk, float speed, float x, float y, PlayerSkill playerSkill1, PlayerSkill playerSkill2) {
        super(bounds, hp, mp, maxHp, maxMp, atk, speed, x, y);
        this.playerSkill1 = playerSkill1;
        this.playerSkill2 = playerSkill2;

        // Khởi tạo PlayerInventoryManager
        this.inventoryManager = new PlayerInventoryManager();

        // Initialize the dependencies
        this.animationManager = new PlayerAnimationManagerImpl();
        this.animationManager.loadAnimations();
        this.inputHandler = new PlayerInputHandlerImpl();
    }

    // Set renderer after creation since it may depend on the player being initialized
    public void setRenderer(PlayerRenderer renderer) {
        this.playerRenderer = renderer;
    }

    public void regenMana(float deltaTime) {
        if (mp < MAX_MP) {
            mp += 45 * deltaTime;
        }
        if (mp > MAX_MP) {
            mp = MAX_MP;
        }
    }

    public void update(float deltaTime, GameMap gameMap) {
        if (isDead) return;

        lastPosition.set(bounds.x, bounds.y);

        // Use InputHandler instead of direct handling
        inputHandler.handleInput(this, deltaTime, gameMap);

        // Update invulnerability timer
        if (isInvulnerable) {
            invulnerabilityTimer -= deltaTime;
            if (invulnerabilityTimer <= 0) {
                isInvulnerable = false;
            }
        }

        regenMana(deltaTime);
        dashTimer -= deltaTime;
        speedMultiplier = 1f;

        if (isHit || isShieldedHit || isMoving || isAttacking) {
            stateTime += deltaTime;
        } else {
            stateTime = 0;
        }

        if (isAttacking) {
            Animation<TextureRegion> currentAttack = animationManager.getAttackAnimation(direction);
            if (currentAttack.isAnimationFinished(stateTime)) {
                isAttacking = false;
                stateTime = 0;
            }
        }

        // Update smoke effects
        smokeManager.update(deltaTime, animationManager);
        updateNpcInteraction(gameMap);
    }

    private void updateNpcInteraction(GameMap gameMap) {
        if (gameMap != null) {
            showInteractMessage = false;
            for (NPC1 npc : gameMap.getNPCs()) {
                float distance = Vector2.dst(
                    bounds.x + bounds.width / 2, bounds.y + bounds.height / 2,
                    npc.getBounds().x + npc.getBounds().width / 2, npc.getBounds().y + npc.getBounds().height / 2
                );
                if (distance <= 2.5f) {
                    npc.setTalking(true);
                    showInteractMessage = true;
                } else {
                    npc.setTalking(false);
                }
            }
        }
    }

    // Khi cần thêm smoke:
    public void addSmoke(float x, float y) {
        smokeManager.addSmoke(x, y);
    }

    // Render method now delegates to PlayerRenderer
    @Override
    public void render(SpriteBatch batch) {
        if (playerRenderer != null) {
            playerRenderer.render(this, batch);
        }
        smokeManager.render(batch, animationManager);
    }

    public void takeDamage(float damage) {
        // If player is invulnerable, don't take damage
        if (isInvulnerable) return;

        if (isShielding) {
            damage /= 2;
        }

        hp = Math.max(0, hp - damage);

        if (hp == 0 && !isDead) {
            onDeath();
        } else {
            isHit = true;
            stateTime = 0;

            // Set invulnerability after taking damage
            isInvulnerable = true;
            invulnerabilityTimer = INVULNERABILITY_DURATION;
        }
    }

    public void blockMovement() {
        bounds.x = lastPosition.x;
        bounds.y = lastPosition.y;
    }

    // Phương thức chuyển tiếp để thêm vật phẩm vào kho đồ
    public void addItemToInventory(Item newItem) {
        inventoryManager.addItemToInventory(newItem, this.bounds);
    }

    // Getters and setters needed by interfaces

    public boolean isDead() {
        return isDead;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void resetStateTime() {
        this.stateTime = 0;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean isShielding() {
        return isShielding;
    }

    public void setShielding(boolean shielding) {
        this.isShielding = shielding;
    }

    public boolean isShieldedHit() {
        return isShieldedHit;
    }

    public void setShieldedHit(boolean shieldedHit) {
        this.isShieldedHit = shieldedHit;
    }

    public PlayerSkill getPlayerSkill1() {
        return playerSkill1;
    }

    public PlayerSkill getPlayerSkill2() {
        return playerSkill2;
    }

    public float getDashTimer() {
        return dashTimer;
    }

    public void setDashTimer(float timer) {
        this.dashTimer = timer;
    }

    public float getDashCooldown() {
        return dashCooldown;
    }

    public float getDashDistance() {
        return dashDistance;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    // Cập nhật các phương thức làm việc với inventory thông qua inventoryManager
    public int[] getCollectAllFragments() {
        return inventoryManager.getCollectAllFragments();
    }

    public ArrayList<Item> getInventory() {
        return inventoryManager.getInventory();
    }

    public int getInventorySize() {
        return inventoryManager.getInventorySize();
    }

    public PlayerInventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public PlayerAnimationManager getAnimationManager() {
        return animationManager;
    }

    // Getter and setter for atk
    public float getAtk() {
        return this.atk;
    }

    // Getter and setter for mp
    public float getMp() {
        return this.mp;
    }

    public void setMp(float mp) {
        this.mp = Math.max(0, Math.min(mp, MAX_MP));
    }

    @Override
    public void onDeath() {
        isDead = true;
        stateTime = 0;
    }

    @Override
    public void onCollision(Collidable other) {
        if (isShielding) {
            isShieldedHit = true;
        } else {
            isHit = true;
            stateTime = 0;
        }
    }

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return this.bounds;
	}

	public boolean isInvulnerable() {
		// TODO Auto-generated method stub
		return this.isInvulnerable;
	}

}
