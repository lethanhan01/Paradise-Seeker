package com.paradise_seeker.game.entity.player;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.player.input.PlayerInputHandlerManager;
import com.paradise_seeker.game.entity.player.inventory.PlayerInventoryManager;
import com.paradise_seeker.game.entity.player.skill.*;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.rendering.animations.PlayerAnimationManager;
import com.paradise_seeker.game.rendering.effects.DashTrailManager;
import com.paradise_seeker.game.rendering.renderer.PlayerRendererManager;
import com.paradise_seeker.game.entity.player.status.PlayerStatusManager;

public class Player extends Character {
    public static final float MAX_HP = 1000;
    public static final float MAX_MP = 100;

    public PlayerStatusManager statusManager = new PlayerStatusManager();
    public PlayerInventoryManager inventoryManager;
    public PlayerAnimationManager animationManager;
    public PlayerInputHandlerManager inputHandler;
    public PlayerRendererManager playerRenderer;
    public DashTrailManager smokeManager = new DashTrailManager();
    public PlayerSkill1 playerSkill1;
    public PlayerSkill2 playerSkill2;

    public Player() {
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.hp = MAX_HP;
        this.mp = MAX_MP;
        this.maxHp = MAX_HP;
        this.maxMp = MAX_MP;
        this.atk = 25;
        this.speed = 5f;
        this.x = 0;
        this.y = 0;

        this.inventoryManager = new PlayerInventoryManager();
        this.animationManager = new PlayerAnimationManager();
        this.animationManager.setAnimations();
        this.inputHandler = new PlayerInputHandlerManager();
        this.playerRenderer = new PlayerRendererManager(this.animationManager);
        this.playerSkill1 = new PlayerSkill1();
        this.playerSkill2 = new PlayerSkill2();
    }

    public Player(Rectangle bounds, float hp, float mp, float maxHp, float maxMp, float atk, float speed, float x, float y, PlayerSkill1 playerSkill1, PlayerSkill2 playerSkill2) {
        super(bounds, hp, mp, maxHp, maxMp, atk, speed, x, y);
        this.playerSkill1 = playerSkill1;
        this.playerSkill2 = playerSkill2;
        this.inventoryManager = new PlayerInventoryManager();
        this.animationManager = new PlayerAnimationManager();
        this.animationManager.setAnimations();
        this.inputHandler = new PlayerInputHandlerManager();
        this.smokeManager = new DashTrailManager();
        this.statusManager = new PlayerStatusManager();
        this.playerRenderer = new PlayerRendererManager(this.animationManager);
    }

    public void regenMana(float deltaTime) {
        if (mp < MAX_MP) {
            mp += (float) 0.5 * deltaTime;
        }
        if (mp > MAX_MP) {
            mp = MAX_MP;
        }
    }

    @Override
    public void act(float deltaTime, GameMap gameMap) {
        if (statusManager.isDead()) return;
        Player player = gameMap.getPlayer();
        statusManager.setLastPosition(bounds.x, bounds.y);

        inputHandler.handleInput(this, deltaTime, gameMap);

        if (statusManager.isInvulnerable()) {
            statusManager.setInvulnerabilityTimer(statusManager.getInvulnerabilityTimer() - deltaTime);
            if (statusManager.getInvulnerabilityTimer() <= 0) {
                statusManager.setInvulnerable(false);
            }
        }

        regenMana(deltaTime);
        smokeManager.dashTimer -= deltaTime;
        smokeManager.speedMultiplier = 1f;

        if (statusManager.isHit() || statusManager.isMoving() || statusManager.isAttacking()) {
            statusManager.addStateTime(deltaTime);
        } else {
            statusManager.resetStateTime();
        }

        // Reset trạng thái hit sau 0.3 giây
        if (statusManager.isHit() && statusManager.getStateTime() > 0.1f) {
            statusManager.setHit(false);
            statusManager.resetStateTime();
        }

        if (statusManager.isAttacking()) {
            Animation<TextureRegion> currentAttack = animationManager.getAttackAnimation(statusManager.getDirection());
            if (currentAttack.isAnimationFinished(statusManager.getStateTime())) {
                statusManager.setAttacking(false);
                statusManager.resetStateTime();
            }
        }

        smokeManager.update(deltaTime, animationManager);
        inputHandler.handleNPCInteraction(player, gameMap);
    }

    public void addSmoke(float x, float y) {
        smokeManager.addSmoke(x, y);
    }

    @Override
    public void takeHit(float damage) {
        if (statusManager.isInvulnerable()) return;

        hp = Math.max(0, hp - damage);

        if (hp == 0) {
            if (!statusManager.isDead()) {
                onDeath();
            }
        } else {
            statusManager.setHit(true);
            statusManager.setMoving(true);
            statusManager.resetStateTime();
            statusManager.setInvulnerable(true);
            statusManager.setInvulnerabilityTimer(PlayerStatusManager.INVULNERABILITY_DURATION);
        }
    }

    public void blockMovement() {
        bounds.x = statusManager.getLastPosition().x;
        bounds.y = statusManager.getLastPosition().y;
    }

    public void addItemToInventory(Item newItem) {
        inventoryManager.addItemToInventory(newItem, this.bounds);
    }

    public PlayerSkill getPlayerSkill1() {
        return playerSkill1;
    }

    public PlayerSkill getPlayerSkill2() {
        return playerSkill2;
    }

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

    public float getAtk() {
        return atk;
    }

    public float getMp() {
        return mp;
    }

    public void setMp(float mp) {
        this.mp = mp;
    }

    @Override
    public void onDeath() {
        //isDead = true;
        //isInvulnerable = true;
        //invulnerabilityTimer = Float.MAX_VALUE;
    	hp=MAX_HP;
    }

}
