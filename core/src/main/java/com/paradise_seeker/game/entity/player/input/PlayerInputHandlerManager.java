package com.paradise_seeker.game.entity.player.input;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.npc.gipsy.Gipsy;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.map.GameMapManager;
import com.paradise_seeker.game.object.Book;
import com.paradise_seeker.game.object.Chest;
import com.paradise_seeker.game.object.item.ATKPotion;
import com.paradise_seeker.game.object.item.HPPotion;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.object.item.MPPotion;
import com.paradise_seeker.game.screen.ControlScreen;
import com.paradise_seeker.game.screen.GameScreen;

public class PlayerInputHandlerManager implements PlayerInputHandler {
    public boolean showInteractMessage = false;
    public String pendingPotionToDrop = null;
    public boolean showDialogueOptions = false;
    public final String[] options = {"HP potion", "MP potion", "ATK potion"};
    public int selectedOptionIndex = 0;

    public boolean isShowInteractMessage() {
        return showInteractMessage;
    }

    @Override
    public void handleInput(Player player, float deltaTime, GameMap gameMap) {
        if (player.statusManager.isDoNothing() || player.statusManager.isAttacking() || player.statusManager.isDead()) return;

        // Check for interaction opportunities and set showInteractMessage
        checkForInteractions(player, gameMap);
        handleMovement(player, deltaTime, gameMap);
        handleDash(player, gameMap);
        handleAttack(player, gameMap);
        handleSkills(player);
        handleNPCInteraction(player, gameMap);
    }
    public void checkForInteractions(Player player, GameMap gameMap) {
      //  showInteractMessage = false; // Reset first

        if (gameMap == null) return;

        // Check for NPCs
        for (Gipsy npc : gameMap.getNPCs()) {
            float distance = calculateDistance(player, npc);
            if (distance <= 2.5f) {
                showInteractMessage = true;
                if ( Gdx.input.isKeyJustPressed(Input.Keys.F) && !npc.isTalking) {
					showDialogueOptions = true; // Reset options when interacting
				}else {
					showDialogueOptions = false; // No options available
				}
                return; // Found an interaction, no need to check further
            }else {
				showInteractMessage = false; // No interaction available
			}
        }

        // Check for books
        Book book = gameMap.getBook();
        if (book != null && book.isPlayerInRange(player) && !book.isOpened()) {
        		showInteractMessage = true;
        }else {
			showInteractMessage = false; // No interaction available
		}
    }
    @Override
    public void handleMovement(Player player, float deltaTime, GameMap gameMap) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        player.statusManager.setMoving(len > 0);
        if (len > 0) {
            dx /= len;
            dy /= len;
            float moveX = dx * player.speed * deltaTime;
            float moveY = dy * player.speed * deltaTime;
            float nextX = player.getBounds().x + moveX;
            float nextY = player.getBounds().y + moveY;
            Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);
            boolean blocked = false;
            if (gameMap != null && gameMap.collidables != null) {
                for (Collidable c : gameMap.collidables) {
                    if (c != player && nextBounds.overlaps(c.getBounds())) {
                    	 c.onCollision(player);  // Gọi để xử lý phụ (mở rương...)
                    	 if (c.isSolid()) {
                    		    player.blockMovement();
                    		    blocked = true;
                    		    break;
                    		}
                    }
                }
            }
            if (!blocked) {
                player.getBounds().x = nextX;
                player.getBounds().y = nextY;
                // Cập nhật hướng di chuyển
                if (Math.abs(dx) > Math.abs(dy)) {
                    player.statusManager.setDirection(dx > 0 ? "right" : "left");
                } else {
                    player.statusManager.setDirection(dy > 0 ? "up" : "down");
                }
            } else {
                player.statusManager.setMoving(false);
            }
        }
        clampToMapBounds(player, gameMap);
    }
    @Override
    public void handleDash(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && player.smokeManager.getDashTimer() <= 0 && player.statusManager.isMoving()) {
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
                float maxDash = player.smokeManager.getDashDistance();
                float prevX = player.getBounds().x;
                float prevY = player.getBounds().y;

                // Try to move in increments until hit something or finished full dash
                while (totalDash < maxDash) {
                    float nextX = player.getBounds().x + (dx / len) * stepSize;
                    float nextY = player.getBounds().y + (dy / len) * stepSize;
                    Rectangle nextBounds = new Rectangle(nextX, nextY, player.getBounds().width, player.getBounds().height);

                    if (gameMap == null || gameMap.collisionSystem == null || !gameMap.collisionSystem.isBlocked(nextBounds)) {
                        player.getBounds().x = nextX;
                        player.getBounds().y = nextY;
                        totalDash += stepSize;
                    } else {
                        break;
                    }
                }
                player.smokeManager.setDashTimer(player.smokeManager.getDashCooldown());
                player.smokeManager.addSmoke(prevX, prevY);
            }
            clampToMapBounds(player, gameMap);
        }
    }

    @Override
    public void handleAttack(Player player, GameMap gameMap) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!player.statusManager.isAttacking() && !player.statusManager.isHit() && !player.statusManager.isDead()) {
                player.statusManager.setAttacking(true);
                player.statusManager.setStateTime(0f); // Reset lại thời gian để animation luôn chạy từ frame đầu
                if (gameMap != null) {
                    float centerX = player.getBounds().x + player.getBounds().width / 2;
                    float centerY = player.getBounds().y + player.getBounds().height / 2;
                    damageMonstersInRange(centerX, centerY, 3f, player.getAtk(), gameMap);
                }
            }
        }
    }
    public void damageMonstersInRange(float x, float y, float radius, float damage, GameMap gameMap) {
        for (Monster m : gameMap.getMonsters()) {
            if (!m.isDead() && isInRange(x, y, m.getBounds(), radius)) m.takeHit(damage);
        }
    }

    public boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public void handleSkills(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (player.getMp() >= 2 && player.getPlayerSkill1().canUse(System.currentTimeMillis())) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill1().castSkill(player.getAtk(), player.getBounds(), player.statusManager.getDirection());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (player.getMp() >= 2 && player.getPlayerSkill2().canUse(System.currentTimeMillis())) {
                player.setMp(player.getMp() - 2);
                player.getPlayerSkill2().castSkill(player.getAtk(), player.getBounds(), player.statusManager.getDirection());
            }
        }
    }

    @Override
    public void handleNPCInteraction(Player player, GameMap gameMap) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && showInteractMessage && gameMap != null) {
            // Tìm NPC gần nhất để tương tác
            Gipsy nearestNPC = null;
            float minDistance = Float.MAX_VALUE;

            for (Gipsy npc : gameMap.getNPCs()) {
                float distance = calculateDistance(player, npc);
                if (distance <= 2.5f && distance < minDistance) {
                    minDistance = distance;
                    nearestNPC = npc;
                }
            }

            if (nearestNPC != null) {

                nearestNPC.openChest();
                // Có thể thêm các logic tương tác khác ở đây
            }
        }
    }

    public void handleDialogue(GameScreen gameScreen, Player player) {
        // Handle F key for dialogue interaction
    	System.out.println("Map: " + gameScreen.mapManager.getCurrentMap().getMapName());
    	for (Gipsy npc : gameScreen.mapManager.getCurrentMap().getNPCs()) {
    		 float distance = calculateDistance(player, npc);
             if (distance <= 2.5f ) {
     	        gameScreen.currentTalkingNPC = npc;
    	    }
    	}
    	Random random = new Random();
    	int randomIndex = random.nextInt(3);
    	this.selectedOptionIndex = randomIndex; // Chọn ngẫu nhiên một tùy chọn
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (this.showDialogueOptions) {
                if (gameScreen.currentTalkingNPC != null) {
                	gameScreen.currentTalkingNPC.stateManager.setHasTalked(true);
                	this.pendingPotionToDrop = this.options[this.selectedOptionIndex];
                    this.showDialogueOptions = false;
                    this.selectedOptionIndex = 0;

                    if (gameScreen.currentTalkingNPC.hasNextLine()) {
                    	gameScreen.currentTalkingNPC.nextLine();
                    	gameScreen.dialogueBox.show(gameScreen.currentTalkingNPC.getCurrentLine());
                    } else {
                    	gameScreen.dialogueBox.hide();
                    	gameScreen.currentTalkingNPC.setTalking(false);


                        // SỬA 1: chỉ mở rương nếu chưa mở và không đang mở
                        if (gameScreen.currentTalkingNPC.isChestOpened() && !gameScreen.currentTalkingNPC.stateManager.isOpeningChest()) {
                        	gameScreen.currentTalkingNPC.openChest();
                        	gameScreen.currentTalkingNPC.stateManager.isChestOpened = true;
                        	finishNpcInteraction(gameScreen, player);
                        }


                    }
                }

            }
        }


    }

    public void dropPotionNextToPlayer(GameMapManager mapManager, String potionType, Player player) {
        float dropX = player.getBounds().x - player.getBounds().width - 0.2f;
        float dropY = player.getBounds().y;
        Item dropped = null;
        switch (potionType) {
            case "HP potion":
                dropped = new HPPotion(dropX, dropY, 1f, "items/potion/potion3.png", 100);
                break;
            case "MP potion":
                dropped = new MPPotion(dropX, dropY, 1f, "items/potion/potion9.png", 15);
                break;
            case "ATK potion":
                dropped = new ATKPotion(dropX, dropY, 1f, "items/atkbuff_potion/potion14.png", 10);
                break;
        }

        if (dropped != null) {
            mapManager.getCurrentMap().dropItem(dropped);

        }
    }

    public void finishNpcInteraction(GameScreen gameScreen, Player player) {
        if (this.pendingPotionToDrop != null ) {
            dropPotionNextToPlayer(gameScreen.mapManager, this.pendingPotionToDrop, player);
            this.pendingPotionToDrop = null;
        }
        if (gameScreen.currentTalkingNPC != null) {
        	gameScreen.currentTalkingNPC.setTalking(true);
        	gameScreen.currentTalkingNPC.stateManager.setChestOpened(true);

        }
        this.showDialogueOptions = false;
        this.selectedOptionIndex = 0;
        gameScreen.currentTalkingNPC.stateManager.isChestOpened = false; // Reset trạng thái mở rương


    }



	@Override
    public void handleZoomInput(GameScreen gameScreen) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) gameScreen.zoom = Math.min(3.0f, gameScreen.zoom + 0.1f);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) || Gdx.input.isKeyJustPressed(Input.Keys.PLUS))
        	gameScreen.zoom = Math.max(0.5f, gameScreen.zoom - 0.1f);
    }

	@Override
    public void handleChest(GameScreen gameScreen, Player player) {
		Chest chest = gameScreen.mapManager.getCurrentMap().getChest();
		if (chest != null && player.getBounds().overlaps(chest.getBounds())) {

	    	    player.blockMovement();

			if (!chest.isOpened())
				gameScreen.hud.showNotification("[F] Open Chest?");

			if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
				if (!chest.isOpened()) {
					chest.onPlayerCollision(player);
					Array<Item> items = chest.getItems();

			        StringBuilder itemListMessage = new StringBuilder("You received:\n");
			        for (Item item : items) {
			            itemListMessage.append("- ").append(item.getName()).append("\n");
			        }
			        gameScreen.hud.showNotification(itemListMessage.toString());
				}
			}
		}
	}
	@Override
    public void handleBook(GameScreen gameScreen, Player player) {
        Book book = gameScreen.mapManager.getCurrentMap().getBook();
        if (book != null && player.getBounds().overlaps(book.getBounds())) {
        	player.blockMovement();
            // Handle F key press for book interaction
        	if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                if (!book.isOpened()) {
                    book.onCollision(player);
                    // Show the book content with longer display time
                    if (gameScreen.game.controlScreen == null)
                    	gameScreen.game.controlScreen = new ControlScreen(gameScreen.game);
                    gameScreen.game.setScreen(gameScreen.game.controlScreen);
                }
        	}
        }
    }

    public float calculateDistance(Player player, Gipsy npc) {
        return (float) Math.sqrt(
            Math.pow(player.getBounds().x + player.getBounds().width/2 - (npc.getBounds().x + npc.getBounds().width/2), 2) +
            Math.pow(player.getBounds().y + player.getBounds().height/2 - (npc.getBounds().y + npc.getBounds().height/2), 2)
        );
    }

    public void clampToMapBounds(Player player, GameMap gameMap) {
        if (gameMap == null) return;

        float minX = 0;
        float minY = 0;
        float maxX = gameMap.getMapWidth() - player.getBounds().width;
        float maxY = gameMap.getMapHeight() - player.getBounds().height;

        player.getBounds().x = Math.max(minX, Math.min(player.getBounds().x, maxX));
        player.getBounds().y = Math.max(minY, Math.min(player.getBounds().y, maxY));
    }
}
