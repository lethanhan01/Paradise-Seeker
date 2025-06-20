package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.paradise_seeker.game.main.Main;

public class PauseScreen implements Screen {

	public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    GlyphLayout layout;
    String[] menuItems = {"- Pausing -", "Continue Game", "Settings", "Return to Main Menu"};
    int selectedIndex = 1;
    public PauseScreen(Main game) {
        this.batch = game.batch; // Use the shared batch
        this.font = game.font; // Use the shared font
        this.camera = game.camera; // Use the shared camera
        this.viewport = game.viewport; // Use the shared viewport
        this.layout = new GlyphLayout();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    	// Clear the screen with a black color
        ScreenUtils.clear(Color.BLACK);
        // Update camera and batch
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // Set the viewport dimensions
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        batch.begin();

        font.setColor(Color.RED);
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            layout.setText(font, text);
            float x = (viewportWidth - layout.width) / 2f;
            float y = viewportHeight - 2f - i * 1.5f;


                font.setColor(Color.WHITE);
                if (i == selectedIndex) {
                	//>
                    font.draw(batch, ">", x - 1.2f, y);
            }

            font.draw(batch, layout, x, y);
        }

        batch.end();

        handleInput();// Handle user input for menu navigation
    }
    // Handle user input for menu navigation
    private void handleInput() {
    	Main game = (Main) Gdx.app.getApplicationListener();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 1) selectedIndex = menuItems.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuItems.length) selectedIndex = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (selectedIndex) {
                case 1:
                	game.setScreen(game.currentGame);
                    break;
                case 2: // Setting
            		game.setScreen(game.settingMenu);
                    break;
                case 3: // Return to Main Menu
                	game.setScreen(game.mainMenu);
                    break;
            }
        }
    }


    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
