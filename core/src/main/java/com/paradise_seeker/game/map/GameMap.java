package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.collision.CollisionSystem;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.monster.projectile.HasProjectiles;
import com.paradise_seeker.game.entity.npc.NPC;
import com.paradise_seeker.game.entity.npc.gipsy.Gipsy;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.object.*;
import com.paradise_seeker.game.object.item.ATKPotion;
import com.paradise_seeker.game.object.item.Fragment;
import com.paradise_seeker.game.object.item.HPPotion;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.object.item.MPPotion;
import com.paradise_seeker.game.object.item.Skill1Potion;
import com.paradise_seeker.game.object.item.Skill2Potion;
import com.paradise_seeker.game.rendering.Renderable;
import java.util.*;

public abstract class GameMap implements Renderable {
    public int MAP_WIDTH;
    public int MAP_HEIGHT;
    protected int TILE_WIDTH;
    protected int TILE_HEIGHT;
    protected String mapName = "Unknown Map";
    public CollisionSystem collisionSystem;
    protected TiledMap tiledMap;
    protected Texture backgroundTexture;

    public Player player;
    public Monster monster;
    public Portal portal, startPortal;
    public Chest chest;
    public Book book;

    public List<Collidable> collidables = new ArrayList<>();
    public List<Gipsy> npcList = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>();

    // Items
    public List<HPPotion> hpItems = new ArrayList<>();
    public List<MPPotion> mpItems = new ArrayList<>();
    public List<ATKPotion> atkItems = new ArrayList<>();
    public List<Skill1Potion> skill1Items = new ArrayList<>();
    public List<Skill2Potion> skill2Items = new ArrayList<>();

    public float itemSpawnTimer = 0f;
    public static final float ITEM_SPAWN_INTERVAL = 120f;

    // Subclass must provide these
    protected abstract String getMapTmxPath();
    protected abstract String getMapBackgroundPath();

    public GameMap() {
        tiledMap = new TmxMapLoader().load(getMapTmxPath());
        backgroundTexture = new Texture(getMapBackgroundPath());

        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        loadCollidables(tiledMap);
    }

    public void loadSpawnPoints(Player player) {
        clearEntities();
        MapLayer spawnsLayer = tiledMap.getLayers().get("Spawns");
        if (spawnsLayer == null) return;

        for (MapObject obj : spawnsLayer.getObjects()) {
            String type = (String) obj.getProperties().get("type");
            if (type == null) continue;
            Float px = obj.getProperties().get("x", Float.class);
            Float py = obj.getProperties().get("y", Float.class);
            if (px == null || py == null) continue;
            float worldX = px / TILE_WIDTH;
            float worldY = py / TILE_HEIGHT;

            switch (type) {
                case "player":
                    Rectangle bounds = player.getBounds();
                    bounds.x = worldX;
                    bounds.y = worldY;
                    break;

                case "monster": {
                    String className = (String) obj.getProperties().get("class");
                    if (className != null) {
                        Monster monster = MonsterFactory.create(className, worldX, worldY);
                        if (monster != null) {
                            monsters.add(monster);
                            collidables.add(monster);
                        }
                    }
                    break;
                }

                case "portal":
                    // Main portal (exit/entrance)
                    portal = new Portal(worldX, worldY);
                    break;

                case "portal_start":
                    // For maps with a "start" portal (e.g., two-way portal)
                    startPortal = new Portal(worldX, worldY);
                    break;

                case "chest":
                	//test chest
                    chest = new Chest(worldX, worldY);
                    break;
                case "chest2":
                    chest = new Chest(worldX, worldY);
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag1.png", 1));

                    break;
                case "chest3":
                    chest = new Chest(worldX, worldY);
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag2.png", 2));

                    break;
                case "chest4":
                    chest = new Chest(worldX, worldY);
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag3.png", 3));

                    break;
                case "book":
                    // Create as Book type instead of GameObject
                    book = new Book(worldX, worldY, "items/book.png");

                    // Optional: Set custom content from map properties
                    String bookContent = (String) obj.getProperties().get("content");
                    if (bookContent != null && !bookContent.isEmpty()) {
                        book.setContent(bookContent);
                    } else {
                        // Default content if not specified in map
                        book.setContent("An ancient book filled with forgotten knowledge...");
                    }

                    gameObjects.add(book);
                    collidables.add(book);
                    break;

                case "randomPotion":
					Random rand = new Random();
                    int potionType = rand.nextInt(5); // 0 = HP, 1 = MP, 2 = ATK, 3 = Skill1, 4 = Skill2
					if (potionType == 0) {
						hpItems.add(new HPPotion(worldX, worldY, 1, "items/potion/potion3.png", 100));
					} else if (potionType == 1) {
						mpItems.add(new MPPotion(worldX, worldY, 1, "items/potion/potion9.png", 15));
					} else if (potionType == 2) {
						atkItems.add(new ATKPotion(worldX, worldY, 1, "items/atkbuff_potion/potion14.png",10));
					} else if (potionType == 3) {
						skill1Items.add(new Skill1Potion(worldX, worldY, 1, "items/buff/potion12.png"));
					} else if (potionType == 4) {
						skill2Items.add(new Skill2Potion(worldX, worldY, 1, "items/buff/potion13.png"));
					}
					break;
                case "npc":
                    String npcClass = (String) obj.getProperties().get("class");
                    Gipsy npc = new Gipsy(worldX, worldY); // Tạo NPC mặc định

                    // Kiểm tra nếu có property "class" và giá trị là "Gipsy"
                    if (npcClass != null && npcClass.equals("Gipsy")) {
                        npc = new Gipsy(worldX, worldY);
                    }

                    npcList.add(npc);
                    collidables.add(npc);
                    break;

                // You can add more types as needed!
                // case "item": ...
            }
        }
    }


    protected void loadCollidables(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Object solidProp = obj.getProperties().get("solid");
                    if (solidProp instanceof Boolean && (Boolean) solidProp) {
                        Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                        float worldX = rect.x / TILE_WIDTH;
                        float worldY = rect.y / TILE_HEIGHT;
                        float worldWidth = rect.width / TILE_WIDTH;
                        float worldHeight = rect.height / TILE_HEIGHT;
                        collidables.add(new SolidObject(new Rectangle(worldX, worldY, worldWidth, worldHeight)));
                    }
                }
            }
        }
    }

    public String getMapName() { return mapName; }

    @Override
    public void render(SpriteBatch batch) { //render những đối tượng trong bản đồ, monster/npc phức tạp nên có hàm riêng
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);
        for (GameObject obj : gameObjects) obj.render(batch);
        for (HPPotion item : hpItems) item.render(batch);
        for (MPPotion item : mpItems) item.render(batch);
        for (ATKPotion item : atkItems) item.render(batch);
        for (Skill1Potion item : skill1Items) item.render(batch);
        for (Skill2Potion item : skill2Items) item.render(batch);
        for (Monster m : monsters) {
            m.renderer.render(m, batch);
            m.hpBarRenderer.render(batch, m.getBounds(), m.animationManager.getCurrentFrame(), m.getHp(), m.getMaxHp(), m.isDead());
            if (m instanceof HasProjectiles) {
                for (Renderable p : ((HasProjectiles)m).getProjectiles()) {
                    p.render(batch);
                }
            }
        }
        for (NPC npc : npcList) {
            npc.npcRenderer.render(npc, batch);
        }
        if (portal != null) portal.render(batch);
        if (startPortal != null) startPortal.render(batch);
        if (chest != null) chest.render(batch);
    }

    public void update(float deltaTime) {
        for (NPC npc : npcList) npc.act(deltaTime, GameMap.this);
        for (Monster m : monsters) m.act(deltaTime, player, this);
        hpItems.removeIf(item -> !item.isActive());
        mpItems.removeIf(item -> !item.isActive());
        atkItems.removeIf(item -> !item.isActive());
        skill1Items.removeIf(item -> !item.isActive());
        skill2Items.removeIf(item -> !item.isActive());
        itemSpawnTimer += deltaTime;
        if (itemSpawnTimer >= ITEM_SPAWN_INTERVAL) {
            spawnRandomItem();
            itemSpawnTimer = 0f;
        }
        if (chest != null) chest.update(deltaTime);
    }

    public void setCollisionSystem(CollisionSystem system) {
        this.collisionSystem = system;

        // Liên kết trực tiếp
        system.collidables = this.collidables;
        system.hpItems = this.hpItems;
        system.mpItems = this.mpItems;
        system.atkItems = this.atkItems;
        system.skill1Items = this.skill1Items;
        system.skill2Items = this.skill2Items;
    }
    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject obj : gameObjects) obj.dispose();
    }

    public void dropItem(Item item) {
        if (item instanceof HPPotion) hpItems.add((HPPotion) item);
        else if (item instanceof MPPotion) mpItems.add((MPPotion) item);
        else if (item instanceof ATKPotion) atkItems.add((ATKPotion) item);
        // Extend for other item types as needed
    }


    public List<Gipsy> getNPCs() { return npcList; }
    public List<Monster> getMonsters() { return monsters; }
    public Monster getMonster() { return monster; }
    public float getMapWidth() { return MAP_WIDTH; }
    public float getMapHeight() { return MAP_HEIGHT; }
    public Portal getStartPortal() { return startPortal; }
    public Portal getPortal() { return portal; }
    public Chest getChest() { return chest; }
    public Book getBook() {return book; }

    private void spawnRandomItem() {
        Random rand = new Random();
        int type = rand.nextInt(5); // 0 = HP, 1 = MP, 2 = ATK, 3 = Skill1, 4 = Skill2
        if (type == 0) {
            String[] textures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
            int[] values = {100, 200, 300};
            int idx = rand.nextInt(textures.length);
            hpItems.add(new HPPotion(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 1) {
            String[] textures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
            int[] values = {15, 30, 50};
            int idx = rand.nextInt(textures.length);
            mpItems.add(new MPPotion(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 2) {
            String[] textures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
            int[] values = {10, 15, 20};
            int idx = rand.nextInt(textures.length);
            atkItems.add(new ATKPotion(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, textures[idx], values[idx]));
        } else if (type == 3) {
            skill1Items.add(new Skill1Potion(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
        } else {
            skill2Items.add(new Skill2Potion(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void clearEntities() {
        monsters.clear();
        npcList.clear();
        hpItems.clear();
        mpItems.clear();
        atkItems.clear();
        skill1Items.clear();
        skill2Items.clear();
        // Chỉ xóa collidable là quái, NPC, item động
        collidables.removeIf(c ->
            c instanceof Monster ||
            c instanceof Gipsy ||
            c instanceof HPPotion ||
            c instanceof MPPotion ||
            c instanceof ATKPotion ||
            c instanceof Skill1Potion ||
            c instanceof Skill2Potion
        );
    }

}
