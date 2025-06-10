package com.paradise_seeker.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.monster.MonsterFactory;
import com.paradise_seeker.game.entity.npc.Gipsy;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.object.*;
import com.paradise_seeker.game.object.item.ATKitem;
import com.paradise_seeker.game.object.item.Fragment;
import com.paradise_seeker.game.object.item.HPitem;
import com.paradise_seeker.game.object.item.Item;
import com.paradise_seeker.game.object.item.MPitem;
import com.paradise_seeker.game.object.item.Skill1item;
import com.paradise_seeker.game.object.item.Skill2item;
import com.paradise_seeker.game.ui.HUD;
import java.util.*;

public abstract class GameMap {
    public int MAP_WIDTH;
    public int MAP_HEIGHT;
    protected int TILE_WIDTH;
    protected int TILE_HEIGHT;
    protected String mapName = "Unknown Map";
    public String getMapName() { return mapName; }

    protected TiledMap tiledMap;
    protected Texture backgroundTexture;

    public Player player;
    public Portal portal, startPortal;
    public Chest chest;
    public Monster monster;

    public List<Collidable> collidables = new ArrayList<>();
    public List<Gipsy> npcList = new ArrayList<>();
    public List<Monster> monsters = new ArrayList<>();
    public List<GameObject> gameObjects = new ArrayList<>();
    public List<Rectangle> occupiedAreas = new ArrayList<>();

    // Items
    private List<HPitem> hpItems = new ArrayList<>();
    private List<MPitem> mpItems = new ArrayList<>();
    private List<ATKitem> atkItems = new ArrayList<>();
    private List<Skill1item> skill1Items = new ArrayList<>();
    private List<Skill2item> skill2Items = new ArrayList<>();

    private float itemSpawnTimer = 0f;
    private static final float ITEM_SPAWN_INTERVAL = 120f;


    // Subclass must provide these
    protected abstract String getMapTmxPath();
    protected abstract String getMapBackgroundPath();

    public GameMap() {
        // 1. Load map and background
        tiledMap = new TmxMapLoader().load(getMapTmxPath());
        backgroundTexture = new Texture(getMapBackgroundPath());

        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        // Do NOT call loadSpawnPoints here!
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
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag1.png", 1));
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag2.png", 2));
                    chest.addItem(new Fragment(worldX, worldY, 1f, "items/fragment/frag3.png", 3));

                    break;

                case "npc":
                    String npcClass = (String) obj.getProperties().get("class");
                    Gipsy npc = new Gipsy(worldX, worldY); // Tạo NPC mặc định

                    // Kiểm tra nếu có property "class" và giá trị là "Gipsy"
                    if (npcClass != null && npcClass.equals("Gipsy")) {
                        npc = new Gipsy(worldX, worldY);
                    }
                    // Bạn có thể thêm các điều kiện khác cho các class NPC khác ở đây
                    // else if (npcClass != null && npcClass.equals("OtherNPC")) {
                    //     npc = new OtherNPC(worldX, worldY);
                    // }

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

    public void render(SpriteBatch batch) {
        batch.draw(backgroundTexture, 0, 0, MAP_WIDTH, MAP_HEIGHT);
        for (GameObject obj : gameObjects) obj.render(batch);
        for (HPitem item : hpItems) item.render(batch);
        for (MPitem item : mpItems) item.render(batch);
        for (ATKitem item : atkItems) item.render(batch);
        for (Skill1item item : skill1Items) item.render(batch);
        for (Skill2item item : skill2Items) item.render(batch);
        for (Monster m : monsters) m.render(batch);
        for (Gipsy npc : npcList) npc.render(batch);
        if (portal != null) portal.render(batch);
        if (startPortal != null) startPortal.render(batch);
        if (chest != null) chest.render(batch);
    }

    public void update(float deltaTime) {
        for (Gipsy npc : npcList) npc.act(deltaTime, GameMap.this);
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

    public void checkCollisions(Player player, HUD hud) {
        CollisionSystem.checkCollisions(player, collidables);
        List<List<? extends Item>> allItemLists = Arrays.asList(
            hpItems, mpItems, atkItems, skill1Items, skill2Items
        );
        for (List<? extends Item> itemList : allItemLists) {
            for (Item item : itemList) {
                if (item.isActive() && item.getBounds().overlaps(player.getBounds())) {
                    boolean canStack = false;
                    boolean hasStackWithSpace = false;
                    if (item.isStackable()) {
                        for (Item invItem : player.getInventory()) {
                            if (invItem.canStackWith(item) && invItem.getCount() < invItem.getMaxStackSize()) {
                                hasStackWithSpace = true;
                                break;
                            }
                        }
                        canStack = hasStackWithSpace;
                    }
                    boolean isFull = player.getInventory().size() >= player.getInventorySize();
                    if (!canStack && isFull) {
                        if (hud != null) hud.showNotification("> Inventory is full!");
                    } else {
                        item.onCollision(player);
                        if (hud != null) hud.showNotification("> Picked up: " + item.getName());
                    }
                }
            }
        }
    }

    public boolean isBlocked(Rectangle nextBounds, Collidable self) {
        for (Collidable c : collidables) {
            if (c == self) continue; // tránh kiểm tra chính mình
            if (c.getBounds().overlaps(nextBounds)) return true;
        }
        return false;
    }
    public boolean isBlocked(Rectangle nextBounds) {
        return isBlocked(nextBounds, null);
    }


    public void dispose() {
        backgroundTexture.dispose();
        for (GameObject obj : gameObjects) obj.dispose();
    }

    public void dropItem(Item item) {
        if (item instanceof HPitem) hpItems.add((HPitem) item);
        else if (item instanceof MPitem) mpItems.add((MPitem) item);
        else if (item instanceof ATKitem) atkItems.add((ATKitem) item);
        // Extend for other item types as needed
    }

    public void damageMonstersInRange(float x, float y, float radius, float damage) {
        for (Monster m : monsters) {
            if (!m.isDead() && isInRange(x, y, m.getBounds(), radius)) m.takeHit(damage);
        }
    }

    private boolean isInRange(float x, float y, Rectangle bounds, float radius) {
        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        float dx = centerX - x;
        float dy = centerY - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public List<Gipsy> getNPCs() { return npcList; }
    public List<Monster> getMonsters() { return monsters; }
    public float getMapWidth() { return MAP_WIDTH; }
    public float getMapHeight() { return MAP_HEIGHT; }
    public Portal getStartPortal() { return startPortal; }
    public Portal getPortal() { return portal; }
    public Chest getChest() { return chest; }

    // === Random item generator (if you want to keep it) ===
    public void generateRandomItems(int hpCount, int mpCount) {
        Random rand = new Random();
        String[] hpTextures = {"items/potion/potion3.png", "items/potion/potion4.png", "items/potion/potion5.png"};
        int[] hpValues = {20, 40, 60};
        String[] mpTextures = {"items/potion/potion9.png", "items/potion/potion10.png", "items/potion/potion11.png"};
        int[] mpValues = {15, 30, 50};
        String[] atkTextures = {"items/atkbuff_potion/potion14.png", "items/atkbuff_potion/potion15.png", "items/atkbuff_potion/potion16.png"};
        int[] atkValues = {5, 10, 15};
        for (int i = 0; i < hpCount; i++) {
            int idx = rand.nextInt(hpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 2) + 1;
            float y = rand.nextFloat() * (MAP_HEIGHT - 2) + 1;
            hpItems.add(new HPitem(x, y, 1f, hpTextures[idx], hpValues[idx]));
        }
        for (int i = 0; i < mpCount; i++) {
            int idx = rand.nextInt(mpTextures.length);
            float x = rand.nextFloat() * (MAP_WIDTH - 2) + 1;
            float y = rand.nextFloat() * (MAP_HEIGHT - 2) + 1;
            mpItems.add(new MPitem(x, y, 1f, mpTextures[idx], mpValues[idx]));
        }
        for (int i = 0; i < 3; i++) {
            int idx = rand.nextInt(atkTextures.length);
            atkItems.add(new ATKitem(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, atkTextures[idx], atkValues[idx]));
            skill1Items.add(new Skill1item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion12.png"));
            skill2Items.add(new Skill2item(rand.nextFloat() * MAP_WIDTH, rand.nextFloat() * MAP_HEIGHT, 1, "items/buff/potion13.png"));
        }
    }

    private void spawnRandomItem() {
        Random rand = new Random();
        float x = rand.nextFloat() * (MAP_WIDTH - 2) + 1;
        float y = rand.nextFloat() * (MAP_HEIGHT - 2) + 1;
        int type = rand.nextInt(5);
        switch (type) {
            case 0:
                hpItems.add(new HPitem(x, y, 1f, "items/potion/potion3.png", 20));
                break;
            case 1:
                mpItems.add(new MPitem(x, y, 1f, "items/potion/potion9.png", 15));
                break;
            case 2:
                atkItems.add(new ATKitem(x, y, 1f, "items/atkbuff_potion/potion14.png", 5));
                break;
            case 3:
                skill1Items.add(new Skill1item(x, y, 1f, "items/buff/potion12.png"));
                break;
            case 4:
                skill2Items.add(new Skill2item(x, y, 1f, "items/buff/potion13.png"));
                break;
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
            c instanceof HPitem ||
            c instanceof MPitem ||
            c instanceof ATKitem ||
            c instanceof Skill1item ||
            c instanceof Skill2item
        );
    }
}
