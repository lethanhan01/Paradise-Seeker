package com.paradise_seeker.game.screen;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.paradise_seeker.game.main.Main;
import com.paradise_seeker.game.screen.cutscene.IntroCutScene;
import com.badlogic.gdx.audio.Music;
public class MainMenuScreen implements Screen {

    Vector2 touchPos;
    Texture titleTexture;
    int selectedIndex = 0;
    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    Texture[] buttonTextures;
    Texture background;
    Texture[] selectedButtonTextures;
    Texture leftIcon;
    Texture rightIcon;

    Music menuMusic;
    public MainMenuScreen(final Main game) {
    
		this.batch = game.batch;
		this.font = game.font;
		this.camera = game.camera;
		this.viewport = game.viewport;
        touchPos = new Vector2();
        //audio menu music
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menutheme.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(game.setVolume);
        // Use your title PNG here
        titleTexture = new Texture(Gdx.files.internal("menu/start_menu/main_menu/psk2.png"));
        titleTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        background = new Texture("menu/start_menu/main_menu/bgnew.png");

        leftIcon = new Texture(Gdx.files.internal("menu/start_menu/main_menu/arrleft.png"));
        rightIcon = new Texture(Gdx.files.internal("menu/start_menu/main_menu/arr.png"));


        buttonTextures = new Texture[] {
            new Texture("menu/start_menu/main_menu/newgame1.png"),
            new Texture("menu/start_menu/main_menu/loadgame1.png"),
            new Texture("menu/start_menu/main_menu/settings1n.png"),
            new Texture("menu/start_menu/main_menu/exit1n.png")
        };
        selectedButtonTextures = new Texture[] {
            new Texture("menu/start_menu/main_menu/newgame_test.png"),
            new Texture("menu/start_menu/main_menu/loadgame2.png"),
            new Texture("menu/start_menu/main_menu/settings2.png"),
            new Texture("menu/start_menu/main_menu/exit2.png")
        };
    }

    @Override
    public void show() {
    	menuMusic.setVolume(((Main) Gdx.app.getApplicationListener()).setVolume);
        menuMusic.play();
    }

    @Override
    public void render(float delta) {

		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    	// Clear the screen with a black color
        ScreenUtils.clear(Color.BLACK);
        // Update camera and batch
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
         // Set the viewport dimensions
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        batch.begin();

        // 1. Draw background
        batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        // 2. Draw the title image at the top center
        float titleWidth = 11.7f;   // Adjust as needed for your image
        float titleHeight = 5.5f;  // Adjust as needed for your image
        float xTitle = (viewportWidth - titleWidth) / 2f;
        float yTitle = viewportHeight - titleHeight - 0.4f;
        batch.draw(titleTexture, xTitle, yTitle, titleWidth, titleHeight);

     // Draw left and right icons
        float iconWidth = 1.5f; // Adjust size as needed
        float iconHeight = 0.4f;
        float xLeftIcon = xTitle - iconWidth; // Add margin
        float xRightIcon = xTitle + titleWidth; // Add margin
        float yIcons = yTitle + (titleHeight - iconHeight) / 2f; // Center vertically
        batch.draw(leftIcon, xLeftIcon, yIcons, iconWidth, iconHeight);
        batch.draw(rightIcon, xRightIcon, yIcons, iconWidth, iconHeight);

        // Draw the menu buttons
        float buttonWidth = viewportWidth * 0.23f * 0.8f;
        float buttonHeight = viewportHeight * 0.1f * 0.8f;
        float xButton = (viewportWidth - buttonWidth) / 2f; // Center buttons horizontally


        // Buttons start below the title image
        float yStart = yTitle - buttonHeight +1f;

        for (int i = 0; i < buttonTextures.length; i++) {
            float yButton = yStart - i * (buttonHeight + 0.15f); // Space between buttons
            Texture buttonTex = (i == selectedIndex) ? selectedButtonTextures[i] : buttonTextures[i];
            batch.draw(buttonTex, xButton, yButton, buttonWidth, buttonHeight);

            // Draw selector arrow
            if (i == selectedIndex) {
                font.setColor(Color.WHITE);
                // Draw ">" on the left
                font.draw(batch, ">", xButton - 0.3f, yButton + buttonHeight * 0.7f);
                // Draw "<" on the right
                font.draw(batch, "<", xButton + buttonWidth + 0.1f, yButton + buttonHeight * 0.7f);
            }
        }

        batch.end();

        handleInput();// Handle user input for menu navigation
    }
     // Handle user input for menu navigation
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = buttonTextures.length - 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= buttonTextures.length) selectedIndex = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
           selectMenuItem();
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);

            float viewportHeight = viewport.getWorldHeight();
            float titleHeight = 2f; // Should match above
            float yTitle = viewportHeight - titleHeight - 0.4f;
            float buttonHeight = 0.9f;
            float yStart = yTitle - buttonHeight - 0.8f;

            for (int i = 0; i < buttonTextures.length; i++) {
                float yButton = yStart - i * (buttonHeight + 0.2f);
                if (touchPos.y > yButton && touchPos.y < yButton + buttonHeight) {
                    selectedIndex = i;
                    selectMenuItem();
                    break;
                }
            }
        }
    }

    private void selectMenuItem( ) {
    	com.paradise_seeker.game.main.Main game = (com.paradise_seeker.game.main.Main) Gdx.app.getApplicationListener();
        switch (selectedIndex) {
            case 0:
                if (game.currentGame == null) {
                    game.currentGame = new GameScreen(game);
                } else {
                    game.currentGame = null;
                    game.inventoryScreen = null; // Reset inventory screengam
                    game.currentGame = new GameScreen(game);
                }
                game.setScreen(new IntroCutScene(game));
                break;
            case 1:
                if (game.currentGame == null) {
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    game.setScreen(game.currentGame);
                }
                break;
            case 2:
                game.setScreen(game.settingMenu);
                break;
            case 3:
                Gdx.app.exit();
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override
    public void hide() {
        // Stop the music when this screen is no longer shown
        menuMusic.stop();
    }

    @Override
    public void dispose() {
        titleTexture.dispose();
        leftIcon.dispose();
        rightIcon.dispose();
        for (Texture t : buttonTextures) t.dispose();
        for (Texture t : selectedButtonTextures) t.dispose();
        menuMusic.dispose();
    }

}
