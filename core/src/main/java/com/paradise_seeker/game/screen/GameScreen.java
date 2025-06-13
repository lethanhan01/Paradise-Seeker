package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.monster.boss.ParadiseKing;
import com.paradise_seeker.game.entity.npc.Gipsy;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.ui.DialogueBox;
import com.paradise_seeker.game.ui.HUD;
import com.paradise_seeker.game.entity.skill.PlayerProjectile;
import com.paradise_seeker.game.main.Main;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.map.GameMapManager;
import com.paradise_seeker.game.object.*;
import com.paradise_seeker.game.object.item.ATKPotion;
import com.paradise_seeker.game.object.item.HPPotion;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.screen.cutscene.EndGame;
import com.paradise_seeker.game.screen.cutscene.EndMap1;
import com.paradise_seeker.game.screen.cutscene.EndMap2;
import com.paradise_seeker.game.screen.cutscene.EndMap3;
import com.paradise_seeker.game.screen.cutscene.EndMap4;
import com.paradise_seeker.game.object.item.MPPotion;


public class GameScreen implements Screen {
    final Main game;
    Player player = new Player();
    Music music;
    private float cameraLerp = 0.1f;// Controls how fast the camera follows the player
    private GameMapManager mapManager;// Manages the current map and transitions
    private HUD hud;// Heads-Up Display for player stats, inventory, etc.
    private DialogueBox dialogueBox;
    private Texture dialogueBg;
    private Gipsy currentTalkingNPC;
    private OrthographicCamera gameCamera;// Camera for the game world
    private OrthographicCamera hudCamera;// Camera for the HUD elements
    public ShapeRenderer shapeRenderer;
    public boolean isInGameMap = true;
    private boolean winTriggered = false;

    public static List<PlayerProjectile> activeProjectiles = new ArrayList<>();

    private final float CAMERA_VIEW_WIDTH = 16f;
    private final float CAMERA_VIEW_HEIGHT = 10f;
    private float zoom = 1.0f;

    // Dialogue choices
    private int selectedOptionIndex = 0;
    private final String[] options = {"HP potion", "MP potion", "ATK potion"};
    private boolean showDialogueOptions = false;
    private String pendingPotionToDrop = null;
    private boolean waitingForChestToOpen = false;

    private int[] mapcutsceneIndicesEnd = {0, 0, 0, 0, 0}; // Indices for cutscenes in the map

    public GameScreen(final Main game) {
        this.game = game;

        // Create player, initial position will be set from Tiled data by mapManager!
		player = new Player();
        this.mapManager = new GameMapManager(player);
        this.hud = new HUD(player, game.font);
        this.shapeRenderer = new ShapeRenderer();

        // Reset font color to white when starting new game
        game.font.setColor(Color.WHITE);

        dialogueBg = new Texture(Gdx.files.internal("ui/dialog/dlg_box_bg/dialogboxc.png"));
        float boxHeight = 180f;
        float dialogX = 0;
        float dialogY = 0f;
        float dialogWidth = Gdx.graphics.getWidth();
        float dialogHeight = boxHeight;

        dialogueBox = new DialogueBox(
            "",
            dialogueBg,
            game.font,
            dialogX,
            dialogY,
            dialogWidth,
            dialogHeight
        );
        currentTalkingNPC = null;

        this.gameCamera = new OrthographicCamera(CAMERA_VIEW_WIDTH, CAMERA_VIEW_HEIGHT);
        // Initial camera will be positioned on first render/update
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setmusic() {
    	String musicPath = mapManager.getCurrentMapMusic();
    	music = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
    	music.setLooping(true);
    	music.setVolume(game.settingMenu.musicVolume);
    	music.play();
    	hud.showMapNotification(mapManager.getCurrentMap().getMapName());
	}
    @Override
    public void show() {
        if (music != null) music.stop();
        setmusic();
    }

    @Override
    public void render(float delta) {
        // Dialogue logic (unchanged)
        handleDialogue();

        // Zoom logic
        handleZoomInput();

        // Game update logic (outside dialogue)
        if (!dialogueBox.isVisible() && !showDialogueOptions && !waitingForChestToOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new PauseScreen(game));
                music.pause();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                if (game.inventoryScreen == null) game.inventoryScreen = new InventoryScreen(game, player);
                game.setScreen(game.inventoryScreen);
                music.pause();
            }
            if (player.hp == 0) {
                game.setScreen(new DeadScreen(game));
                music.stop();
                game.currentGame = null;
            }

            player.act(delta,mapManager.getCurrentMap());
            mapManager.update(delta);

            Chest chest = mapManager.getCurrentMap().getChest();
            if (chest != null) {
				handleChest();
			}

            handleBook();

            mapManager.getCurrentMap().checkCollisions(player, hud);
            float playerCenterX = player.getBounds().x + player.getBounds().width / 2f;
            float playerCenterY = player.getBounds().y + player.getBounds().height / 2f;
            player.playerSkill2.updatePosition(playerCenterX, playerCenterY);

            // Cập nhật logic skill2
            player.playerSkill2.updateSkill(delta, mapManager.getCurrentMap().getMonsters());
            // Update projectiles
            for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
                PlayerProjectile projectile = activeProjectiles.get(i);
                projectile.update();
                for (Monster monster : mapManager.getCurrentMap().getMonsters()) {
                    if (projectile.isActive() && !monster.isDead() && monster.getBounds().overlaps(projectile.getHitbox())) {
                        monster.takeHit(projectile.getDamage());
                        projectile.setInactive();
                    }
                }
                if (!projectile.isActive()) activeProjectiles.remove(i);
            }

            // Update all monsters (AI, attack, movement)
            for (Monster monster : mapManager.getCurrentMap().getMonsters()) {
                monster.act(delta, player, mapManager.getCurrentMap());
            }

        } else {
            mapManager.update(delta);
        }

        if (waitingForChestToOpen && currentTalkingNPC != null) {
            if (currentTalkingNPC.isChestOpenAndFinished()) {
                waitingForChestToOpen = false;
                finishNpcInteraction();
            }
        }

        // Camera follows player
        Vector2 playerCenter = new Vector2(player.getBounds().x + player.getBounds().width / 2, player.getBounds().y + player.getBounds().height / 2);
        Vector2 currentCameraPos = new Vector2(gameCamera.position.x, gameCamera.position.y);
        Vector2 newCameraPos = currentCameraPos.lerp(playerCenter, cameraLerp);
        gameCamera.position.set(newCameraPos.x, newCameraPos.y, 0);
        gameCamera.viewportWidth = CAMERA_VIEW_WIDTH * zoom;
        gameCamera.viewportHeight = CAMERA_VIEW_HEIGHT * zoom;
        gameCamera.update();

        // Clear the screen with a black color
        ScreenUtils.clear(Color.BLACK);
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        mapManager.render(game.batch);
        // Render all monsters
        for (Monster monster : mapManager.getCurrentMap().getMonsters()) {
            monster.isRendered(game.batch);
        }
        // Render player and skills
        player.isRendered(game.batch);
        player.playerSkill1.render(game.batch);
        player.playerSkill2.render(game.batch);
        for (PlayerProjectile projectile : activeProjectiles) projectile.isRendered(game.batch);
        game.batch.end();
        // Render dialogue box
        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        float baseHeight = 720f;
        float fontScale = Math.max(Gdx.graphics.getHeight() / baseHeight, 0.05f);
        dialogueBox.render(game.batch, fontScale);
        game.batch.end();
        // Render HUD
        hud.shapeRenderer.setProjectionMatrix(hudCamera.combined);
        hud.spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(hudCamera.viewportHeight);

        //renderDialogueOptions(fontScale);

        // --- PORTAL & MAP SWITCH ---
        handlePortals();
    }

    /** All portal/map transition logic, always uses Tiled player spawn! */
    private void handlePortals() {
        GameMap currentMap = mapManager.getCurrentMap();
        if (currentMap.portal != null && player.getBounds().overlaps(currentMap.portal.getBounds())) {
            currentMap.portal.onCollision(player);

            switch (mapManager.getCurrentMapIndex()) {
				case 0: // Map 1 to Map 2
					if (mapcutsceneIndicesEnd[0] == 0) {
						mapcutsceneIndicesEnd[0] = 1; // Set cutscene index for Map 1 to Map 2
					    game.setScreen(new EndMap1(game));
					}
					break;
				case 1: // Map 2 to Map 3
					if (mapcutsceneIndicesEnd[1] == 0) {
						mapcutsceneIndicesEnd[1] = 1; // Set cutscene index for Map 2 to Map 3
					    game.setScreen(new EndMap2(game));
					}
					break;
				case 2: // Map 3 to Map 4
					if (mapcutsceneIndicesEnd[2] == 0) {
						mapcutsceneIndicesEnd[2] = 1; // Set cutscene index for Map 3 to Map 4
					    game.setScreen(new EndMap3(game));
					}
					break;
				case 3: // Map 4 to Map 5
					break;
				case 4: // Map 5 to Map 1 (loop back)
					// Kiểm tra boss ParadiseKing đã chết chưa và chuyển sang màn hình chiến thắng
		            for (Monster monster : mapManager.getCurrentMap().getMonsters()) {
		            	if (monster instanceof ParadiseKing && monster.isDead() && !winTriggered){
		                    winTriggered = true;
		                    Gdx.app.postRunnable(() -> {
		                        music.stop(); // Dừng nhạc hiện tại
		                        if (mapcutsceneIndicesEnd[4] == 0) {
		    						mapcutsceneIndicesEnd[4] = 1; // Set cutscene index for Map 5 to Map 1
		    					    game.setScreen(new EndGame(game)); // Reuse EndMap1 for Map 5 to Map 1 transition
		    					}
		                    });
		                }
		            }
					break;
			}

            if (mapManager.getCurrentMapIndex() != 3 && mapManager.getCurrentMapIndex() != 4) {

            	mapManager.switchToNextMap();
                switchMusicAndShowMap();
			} else {
				boolean hasKey = false;
	        	for (Item item : player.getInventory()) {
					if (item.getName().equals("Fragment of the Lost Treasure")) {
						hasKey = true;
						break;
					}
				}
	        	if (hasKey) {
	                // chuyển sang map 5
	        		if (mapcutsceneIndicesEnd[3] == 0) {
						mapcutsceneIndicesEnd[3] = 1; // Set cutscene index for Map 4 to Map 5
					    game.setScreen(new EndMap4(game)); // Reuse EndMap1 for Map 4 to Map 5 transition
					}
	        		if (mapManager.getCurrentMapIndex() == 3) {
		                mapManager.switchToNextMap();	                
		                }

	            } else {
	                hud.showNotification("> You need the Key to enter!");
	            }
			}

        }
        if (currentMap.getStartPortal() != null && player.getBounds().overlaps(currentMap.getStartPortal().getBounds())) {
            currentMap.getStartPortal().onCollision(player);
            mapManager.switchToPreviousMap();
            switchMusicAndShowMap();
        }
    }

    private void handleBook() {
        Book book = mapManager.getCurrentMap().getBook();
        if (book != null && book.isPlayerInRange(player)) {
            // Show interaction message if book hasn't been opened yet


            // Handle F key press for book interaction
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                if (!book.isOpened()) {
                    book.onCollision(player);
                    // Show the book content with longer display time
                    hud.showNotification(book.getContent());
                } else {
                    // If already opened, show a different message
                    hud.showNotification("You have already read this book.");
                }
            }
        }
    }





    private void handleChest() {
		Chest chest = mapManager.getCurrentMap().getChest();
		if (chest != null && player.getBounds().overlaps(chest.getBounds())) {
			
	    	    player.blockMovement();

			if (!chest.isOpened())
				hud.showNotification("[F] Open Chest?");


			if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
				if (!chest.isOpened()) {
					chest.onPlayerCollision(player);
					Array<Item> items = chest.getItems();

			        StringBuilder itemListMessage = new StringBuilder("You received:\n");
			        for (Item item : items) {
			            itemListMessage.append("- ").append(item.getName()).append("\n");
			        }

			        hud.showNotification(itemListMessage.toString());
				}
			}
		}
	}

    private void handleDialogue() {
        // Handle F key for dialogue interaction
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (showDialogueOptions) {
                if (currentTalkingNPC != null) {
                    currentTalkingNPC.stateManager.setHasTalked(true);
                    pendingPotionToDrop = options[selectedOptionIndex];
                    showDialogueOptions = false;
                    selectedOptionIndex = 0;

                    if (currentTalkingNPC.hasNextLine()) {
                        currentTalkingNPC.nextLine();
                        dialogueBox.show(currentTalkingNPC.getCurrentLine());
                    } else {
                        dialogueBox.hide();
                        currentTalkingNPC.setTalking(false);
                        currentTalkingNPC.openChest();
                        waitingForChestToOpen = true;
                        finishNpcInteraction();
                    }
                }
            } else if (currentTalkingNPC != null) {
                if (currentTalkingNPC.shouldShowOptions() && !showDialogueOptions) {
                    showDialogueOptions = true;
                } else {
                    if (currentTalkingNPC.hasNextLine()) {
                        currentTalkingNPC.nextLine();
                        dialogueBox.show(currentTalkingNPC.getCurrentLine());
                    } else {
                        dialogueBox.hide();
                        currentTalkingNPC.setTalking(false);
                        if (!currentTalkingNPC.isChestOpened()) {
                            currentTalkingNPC.openChest();
                            waitingForChestToOpen = true;
                        }
                        finishNpcInteraction();
                    }
                }
            } else {
                for (Gipsy npc : mapManager.getCurrentMap().getNPCs()) {
                    float dx = Math.abs(player.getBounds().x - npc.getBounds().x);
                    float dy = Math.abs(player.getBounds().y - npc.getBounds().y);
                    if (dx < 2.5f && dy < 2.5f) {
                        currentTalkingNPC = npc;
                        if (!npc.hasTalked()) {
                            npc.resetDialogue();
                            npc.setTalking(true);
                            dialogueBox.show(npc.getCurrentLine());
                        } else if (npc.isChestOpened()) {
                            npc.setTalking(true);
                            dialogueBox.show("<You've already chosen a potion.>");
                        }
                        break;
                    }
                }
            }
        }

        // Handle left/right input for options
        if (showDialogueOptions) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedOptionIndex = (selectedOptionIndex - 1 + options.length) % options.length;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedOptionIndex = (selectedOptionIndex + 1) % options.length;
            }
        }
    }

    private void finishNpcInteraction() {
        if (pendingPotionToDrop != null) {
            dropPotionNextToPlayer(pendingPotionToDrop);
            pendingPotionToDrop = null;
        }
        if (currentTalkingNPC != null) {
            currentTalkingNPC.setTalking(false);
            currentTalkingNPC = null;
        }
        showDialogueOptions = false;
        selectedOptionIndex = 0;
        waitingForChestToOpen = false;
    }

    private void dropPotionNextToPlayer(String potionType) {
        float dropX = player.getBounds().x + player.getBounds().width + 0.2f;
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

    private void handleZoomInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) zoom = Math.min(3.0f, zoom + 0.1f);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) || Gdx.input.isKeyJustPressed(Input.Keys.PLUS))
            zoom = Math.max(0.5f, zoom - 0.1f);
    }

    @Override public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        if (music != null) music.dispose();
        hud.dispose();
        dialogueBg.dispose();
    }

    public void switchMusicAndShowMap() {
        if (music != null) {
            music.stop();
            music.dispose();
        }
        setmusic();
    }
}
