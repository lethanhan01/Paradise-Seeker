package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.paradise_seeker.game.main.Main;

public class DeadScreen implements Screen{
	
	public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    GlyphLayout layout;
    
    int selectedIndex = 1; 
    Texture background;
    Texture[] buttonTextures;
    Texture[] selectedButtonTextures;
    public DeadScreen(Main game) {
        this.layout = new GlyphLayout();
        background = new Texture("menu/end_menu/main_death/bgdeath3.png");

        buttonTextures = new Texture[] {
            new Texture("menu/end_menu/main_death/newgame.png"),
            new Texture("menu/end_menu/main_death/backtomain.png")
        };

        selectedButtonTextures = new Texture[] {
            new Texture("menu/end_menu/main_death/newgame2.png"),
            new Texture("menu/end_menu/main_death/backtomain2.png")
        };

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
        batch.draw(background, 0, 0, viewportWidth, viewportHeight);
        //Draw the title text
        float buttonWidth = 5f;
        float buttonHeight = 1f;
        float xButton = (viewportWidth - buttonWidth) / 2f;
        float yStart = viewportHeight - 7f;
  
        for (int i = 0; i < buttonTextures.length; i++) {
            float yButton = yStart - i * (buttonHeight + 0.1f);
            if (i == selectedIndex) {
                font.setColor(Color.RED);
                // Draw ">" on the left
                font.draw(batch, ">", xButton - 0.5f, yButton + buttonHeight * 0.7f);
                // Draw "<" on the right
                font.draw(batch, "<", xButton + buttonWidth + 0.2f, yButton + buttonHeight * 0.7f);
            }
            // Draw the button texture
            Texture tex = (i == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            batch.draw(tex, xButton, yButton, buttonWidth, buttonHeight);
        }


        batch.end();

        handleInput();// Handle user input for navigation and selection
    }
    // Handle user input for navigation and selection
    private void handleInput() {
    	Main game = (Main) Gdx.app.getApplicationListener();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = buttonTextures.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= buttonTextures.length) selectedIndex = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        	switch (selectedIndex) {
            case 0: // NEW GAME
                game.currentGame = new GameScreen(game);
                game.setScreen(game.currentGame);
                break;
            case 1: // RETURN TO MAIN MENU
                game.setScreen(new MainMenuScreen(game));
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
