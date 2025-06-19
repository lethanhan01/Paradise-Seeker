package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.collision.CombatCollisionHandler;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.monster.ai.MonsterAI;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.rendering.animations.MonsterAnimationManager;
import com.paradise_seeker.game.rendering.renderer.monsterrender.MonsterRendererManager;
import com.paradise_seeker.game.rendering.MonsterHPBarManager;

public abstract class Monster extends Character{

    public MonsterStatusManger statusManager = new MonsterStatusManger();

    public MonsterAnimationManager animationManager;
    public CombatCollisionHandler collisionHandler;
    public MonsterRendererManager renderer;
    public MonsterHPBarManager hpBarRenderer;
    public MonsterAI ai;

    public Monster(Rectangle bounds, float hp, float mp, float maxHp, float maxMp, float atk, float speed, float x, float y) {
        super(bounds, hp, mp, maxHp, maxMp, atk, speed, x, y);
        statusManager.setSpawnX(x);
        statusManager.setSpawnY(y);

        // Initialize managers
        this.hpBarRenderer = new MonsterHPBarManager();
        this.animationManager = new MonsterAnimationManager();
        this.renderer = new MonsterRendererManager(animationManager);
        this.collisionHandler = new CombatCollisionHandler();
        this.ai = new MonsterAI(this);

        // Set initial position
        statusManager.getLastPosition().set(x, y);
        this.bounds = new Rectangle(x, y, bounds.width, bounds.height);

        // Load animations (to be implemented by subclasses)
        hasAnimations();
    }

    @Override
    public void act(float deltaTime, GameMap map) {
    	Player player = map.getPlayer();
		this.act(deltaTime, player, map);
	}

    public void act(float deltaTime, Player player, GameMap map) {
        if (player == null || player.statusManager.isDead()) return;

        // Update AI first
        ai.update(deltaTime, player, map.collisionSystem, this);

        // Track movement
        statusManager.setMoving(statusManager.getLastPosition().dst(bounds.x, bounds.y) > 0.0001f);
        statusManager.getLastPosition().set(bounds.x, bounds.y);

        // Update animation state
        animationManager.update(deltaTime, statusManager.isMoving(), statusManager.isDead(), false, player.getBounds().x, this);
        if (statusManager.isDead() && animationManager.isDeathAnimationFinished() && !statusManager.isHasSetBounds()) {
            bounds.set(0, 0, 0, 0); //  Ẩn quái vật sau khi hoạt họa chết hoàn tất
            statusManager.setHasSetBounds(true);
        }
    }

	 public void cleave(Player player) {
	     // Start cleave animation
	     animationManager.startCleaveAnimation();

	     // Set pending cleave hit in collision handler
	     collisionHandler.setPendingCleaveHit(true);

	     // If player is in range, apply damage
	     if (collisionHandler.isPlayerInCleaveRange(player, this)) {
	         collisionHandler.applyCleaveHitToPlayer(player, this);
	     }
	 }

    public boolean isFacingRight() {

        return animationManager.isFacingRight();
    }

    public float getHp() {
        return hp;
    }

    public float getAtk() {
        return atk;
    }

    public float getSpeed() {
        return speed;
    }

    public void takeHit(float damage) {
        if (statusManager.isDead()) return;

        this.hp = Math.max(0, this.hp - damage);

        // Trigger hit animation
        animationManager.startTakeHitAnimation();

        // Check for death
        if (this.hp == 0) {
        	statusManager.setDead(true);
            onDeath();
        }

        // Alert AI that monster was hit
        ai.onAggro();
    }

    @Override
    public void onCollision(Collidable other) {
        collisionHandler.handleCollision(other, this);
    }

    public void onCollision(Player player) {
        collisionHandler.handlePlayerCollision(player, this);
    }

    @Override
    public void onDeath() {
    	statusManager.setDead(true) ;
		bounds.set(0, 0, 0, 0); // Reset position on death
    }

    public abstract void hasAnimations();

    protected void setupAnimations(
            Animation<TextureRegion> idleLeft, Animation<TextureRegion> idleRight,
            Animation<TextureRegion> walkLeft, Animation<TextureRegion> walkRight,
            Animation<TextureRegion> takeHitLeft, Animation<TextureRegion> takeHitRight,
            Animation<TextureRegion> cleaveLeft, Animation<TextureRegion> cleaveRight,
            Animation<TextureRegion> deathLeft, Animation<TextureRegion> deathRight) {

        animationManager.setAnimations(
            idleLeft, idleRight,
            walkLeft, walkRight,
            takeHitLeft, takeHitRight,
            cleaveLeft, cleaveRight,
            deathLeft, deathRight
        );
    }

    public void dispose() {
        if (renderer != null) {
            renderer.dispose();
        }
    }
    @Override
    public boolean isSolid() {
        return true; // Monsters are solid by default
}

    public float getMaxHp() {
        return maxHp;
    }
}
