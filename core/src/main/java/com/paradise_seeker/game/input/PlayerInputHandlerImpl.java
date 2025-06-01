package com.paradise_seeker.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.npc.NPC1;
import com.paradise_seeker.game.map.GameMap;

/**
 * Implementation của PlayerInputHandler
 * Chuyên trách xử lý tất cả input của Player
 */
public class PlayerInputHandlerImpl implements PlayerInputHandler {

    @Override
    public void handleInput(Player player, float deltaTime, GameMap gameMap) {
        if (player.isPaused() || player.isAttacking || player.isDead) return;

        handleMovement(player, deltaTime, gameMap);
        handleDash(player, gameMap);
        handleAttack(player, gameMap);
        handleShield(player);
        handleSkills(player);
        handleNPCInteraction(player);
        handleCheat(player);
    }

    @Override
    public void handleMovement(Player player, float deltaTime, GameMap gameMap) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        player.setMoving(len > 0);

        // Always update direction if input is held
        if (len > 0) {
            if (Math.abs(dx) > Math.abs(dy)) {
                player.setDirection(dx > 0 ? "right" : "left");
            } else if (Math.abs(dy) > 0) {
                player.setDirection(dy > 0 ? "up" : "down");
            }
        }

        // Only move if not blocked
        if (player.isMoving()) {
            float moveX = (dx / len) * player.getSpeed() * player.getSpeedMultiplier() * deltaTime;
            float moveY = (dy / len) * player.getSpeed() * player.getSpeedMultiplier() * deltaTime;

            float nextX = player.getBounds().x + moveX;
            float nextY = player.getBounds().y + moveY;
            Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);

            if (gameMap == null || !gameMap.isBlocked(nextBounds)) {
                player.getBounds().x = nextX;
                player.getBounds().y = nextY;
            }
            clampToMapBounds(player, gameMap);
        }
    }

    @Override
    public void handleDash(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && player.getDashTimer() <= 0 && player.isMoving()) {
            float dx = 0, dy = 0;

            // Xác định hướng dash
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

            float len = (float) Math.sqrt(dx * dx + dy * dy);

            if (len > 0) {
                float stepSize = 0.1f;
                float totalDash = 0f;
                float maxDash = player.getDashDistance();
                float prevX = player.getBounds().x;
                float prevY = player.getBounds().y;

                // Try to move in increments until hit something or finished full dash
                while (totalDash < maxDash) {
                    float nextX = player.getBounds().x + (dx / len) * stepSize;
                    float nextY = player.getBounds().y + (dy / len) * stepSize;
                    Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);

                    if (gameMap == null || !gameMap.isBlocked(nextBounds)) {
                        player.getBounds().x = nextX;
                        player.getBounds().y = nextY;
                        totalDash += stepSize;
                    } else {
                        break;
                    }
                }
                player.setDashTimer(player.getDashCooldown());
                player.addSmoke(prevX, prevY);
            }
            clampToMapBounds(player, gameMap);
        }
    }

    @Override
    public void handleAttack(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.setAttacking(true);
            player.resetStateTime();

            if (gameMap != null) {
                float centerX = player.getBounds().x + player.getBounds().width / 2;
                float centerY = player.getBounds().y + player.getBounds().height / 2;
                gameMap.damageMonstersInRange(centerX, centerY, 5f, player.getAtk());
            }
        }
    }

    @Override
    public void handleShield(Player player) {
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if (!player.isShielding()) {
                player.setShielding(true);
            }
        } else {
            player.setShielding(false);
            player.setShieldedHit(false);
        }
    }

    @Override
    public void handleSkills(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (player.getMp() >= 2) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill1().castSkill(player.getAtk(), player.getBounds(), player.getDirection());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (player.getMp() >= 2) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill2().castSkill(player.getAtk(), player.getBounds(), player.getDirection());
            }
        }
    }

    @Override
    public void handleNPCInteraction(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && player.getNearestNPC() != null) {
            player.getNearestNPC().openChest();
        }
    }

    @Override
    public void handleCheat(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            player.setHp(Player.MAX_HP);
            player.setMp(Player.MAX_MP);
        }
    }

    private void clampToMapBounds(Player player, GameMap gameMap) {
        if (gameMap == null) return;

        float minX = 0;
        float minY = 0;
        float maxX = gameMap.getMapWidth() - player.getBounds().width;
        float maxY = gameMap.getMapHeight() - player.getBounds().height;

        player.getBounds().x = Math.max(minX, Math.min(player.getBounds().x, maxX));
        player.getBounds().y = Math.max(minY, Math.min(player.getBounds().y, maxY));
    }
}
