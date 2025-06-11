package com.paradise_seeker.game.object;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.player.Player;

public class Book extends GameObject {
    private boolean isOpened = false;
    private String content = "This is a mysterious book...";
    private Rectangle interactionBounds; // Separate bounds for interaction

    public Book(float x, float y) {
        super(x, y, 1f, 1f, "images/objects/book/book.png");
        this.bounds = new Rectangle(x, y, 1f, 1f); // Keep original collision bounds
        this.interactionBounds = new Rectangle(x - 0.75f, y - 0.75f, 2.5f, 2.5f); // Larger interaction area
    }

    public Book(float x, float y, String texturePath) {
        super(x, y, 1f, 1f, texturePath);
        this.bounds = new Rectangle(x, y, 1f, 1f);
        this.interactionBounds = new Rectangle(x - 0.75f, y - 0.75f, 2.5f, 2.5f);
    }

    public Book(float x, float y, String texturePath, String content) {
        super(x, y, 1f, 1f, texturePath);
        this.bounds = new Rectangle(x, y, 1f, 1f);
        this.interactionBounds = new Rectangle(x - 0.75f, y - 0.75f, 2.5f, 2.5f);
        this.content = content;
    }

    // Check if player is in interaction range
    public boolean isPlayerInRange(Player player) {
        return player.getBounds().overlaps(interactionBounds);
    }

    public void open() {
        if (!isOpened) {
            isOpened = true;
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            // Don't block movement for books - they should be walkable
            // Only handle interaction logic here
        }
    }

    public boolean shouldShowInteractionMessage() {
        return !isOpened;
    }

    @Override
    public boolean isSolid() {
        return false; // Books are not solid - player can walk over them
    }

    public boolean isOpened() {
        return isOpened;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}