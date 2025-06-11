package com.paradise_seeker.game.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.rendering.Renderable;

public abstract class GameObject implements Collidable, Renderable {
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean solid;
    public GameObject(float x, float y, float width, float height, String texturePath) {
        this.bounds = new Rectangle(x, y, width, height);
        this.texture = new Texture(texturePath);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void onCollision(Collidable other) {
        //
    }

    public void dispose() {
        texture.dispose();
    }

	public abstract boolean isSolid();
}
