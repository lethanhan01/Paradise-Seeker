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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.paradise_seeker.game.main.Main;

public class SettingScreen implements Screen {

	public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    GlyphLayout layout;
    // Menu items
    String[] menuItems = {"Full Screen", "Music", "Control", "Back to the Game", "Return to Menu"};
    int selectedIndex = 0;
    // audio volume configuration
    public float setVolume = 0.5f;
    Texture background;
    // Default music volume
    float musicVolume = setVolume;
    // ShapeRenderer for drawing bars
    ShapeRenderer shapeRenderer;

    // Font scale menu
    private static final float BASE_HEIGHT = 950f;
    private static final float cd = Gdx.graphics.getHeight();
    private float fontScale = 0.025f;
    private float menuItemSpacing = 0.95f;
    private float menuStartY = 0;

    // Track fullscreen toggle state for display
    public boolean showFullscreenToggle = false;

    public SettingScreen(Main game) {
		this.batch = game.batch;
		this.layout = new GlyphLayout();
		this.font = game.font; // Use the default font from Main
		this.camera = game.camera;
		this.viewport = game.viewport; // Use the shared viewport
        background = new Texture("menu/settings_menu/setting_main/settings_scr2.png");
        shapeRenderer = new ShapeRenderer();
        updateFontScale();
    }
    //update font scale based on screen height
    private void updateFontScale() {
    float screenHeight = Gdx.graphics.getHeight();
    float ratio	 = screenHeight / cd;
        this.fontScale = (screenHeight / (BASE_HEIGHT*ratio)) * 0.045f;
        menuItemSpacing = 0.95f * fontScale * 48f;
        menuStartY = viewport.getWorldHeight() - 3f;
    }

    @Override
    public void show() {
        updateFontScale();
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a black color
        ScreenUtils.clear(Color.BLACK);
        // Update camera and batch projection matrix
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // Set the viewport for the batch
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        float originalScaleX = font.getData().scaleX;
        float originalScaleY = font.getData().scaleY;
        font.getData().setScale(fontScale);

        batch.begin();
        batch.draw(background, 0, 0, viewportWidth, viewportHeight);

        float xMenu = 3f; // X position for menu items
        float y = menuStartY;// Starting Y position for menu items

        // We'll need to remember where the Music bar is, for the toggle label.
        float barX = 0, barY = 0, barWidth = 5.5f, barHeight = 0.2f;
        boolean musicBarFound = false;
         // Draw menu items
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            boolean isSelected = (i == selectedIndex);

            if (isSelected) {
                font.setColor(Color.GOLD);
                drawText("> " + text, xMenu, y);
            } else {
            	font.setColor(Color.WHITE);
                drawText("  " + text, xMenu, y);
            }

            // Only draw music volume bar for the Music option (index 1)
            if (i == 1) {
                float vol = musicVolume;
                barX = xMenu + 4.1f;
                barY = y - 0.25f;
                musicBarFound = true;

                batch.end();
                // Draw the volume bar
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(barX, barY, barWidth, barHeight * 0.45f);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(barX, barY, barWidth * vol, barHeight * 0.45f);
                shapeRenderer.end();
                batch.begin();

                // Draw A/D and volume value
                font.setColor(Color.LIGHT_GRAY);
                font.draw(batch, "<A>", barX - 1.2f, barY + barHeight * 1.0f);
                font.draw(batch, "<D>", barX + barWidth + 0.3f, barY + barHeight * 1.0f);
                // Draw the volume value as text
                int volumeValue = Math.round(vol * 10);
                String volumeText = String.valueOf(volumeValue);
                GlyphLayout volumeLayout = new GlyphLayout(font, volumeText);
                float volumeTextX = barX + (barWidth - volumeLayout.width) / 2;
                float volumeTextY = barY + barHeight * -1.0f;
                font.setColor(Color.GOLD);
                font.draw(batch, volumeText, volumeTextX, volumeTextY);
            }

            y -= menuItemSpacing;
        }

        // Draw On/Off label **above the bar** when Full Screen is selected
        if (selectedIndex == 0 && musicBarFound) {
            boolean isFullScreen = Gdx.graphics.isFullscreen();
            String status = isFullScreen ? "On" : "Off";
            Color statusColor = isFullScreen ? Color.GREEN : Color.RED;

            float labelX = barX + barWidth / 2f;
            float labelY = barY + barHeight * 6.3f;

            font.setColor(statusColor);
            GlyphLayout statusLayout = new GlyphLayout(font, status);
            // Centered above the bar
            font.draw(batch, status, labelX - statusLayout.width / 2, labelY);
        }

        batch.end();
        font.getData().setScale(originalScaleX, originalScaleY);

        handleInput();// Handle user input for navigation and actions
    }
    // Draw text with the current font and position
    private void drawText(String text, float x, float y) {
        layout.setText(font, text);
        font.draw(batch, layout, x, y);
    }
    // Handle user input for navigation and actions
    private void handleInput() {
    	Main game = (Main) Gdx.app.getApplicationListener();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = menuItems.length - 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuItems.length) selectedIndex = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (selectedIndex == 1) {
                musicVolume = Math.max(0f, musicVolume - 0.1f);
                game.setVolume = musicVolume; // Update the setVolume for consistency
                if (game.currentGame != null)
                    game.currentGame.music.setVolume(game.setVolume);
                
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (selectedIndex == 1) {
                musicVolume = Math.min(1f, musicVolume + 0.1f);
                game.setVolume = musicVolume; // Update the setVolume for consistency
                if (game.currentGame != null)
                    game.currentGame.music.setVolume(game.setVolume);
                
            }
        }

        // Toggle fullscreen when pressing enter/space on "Full Screen"
        if ((Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            switch (selectedIndex) {
                case 0: // Fullscreen
                    toggleFullscreen();
                    break;
                case 1: break; // Music
                case 2: // Control
                    if (game.controlScreen == null)
                        game.controlScreen = new ControlScreen(game);
                    game.setScreen(game.controlScreen);
                    break;
                case 3: // End Game (back to game or menu)
                    if (game.currentGame != null)
                        game.setScreen(game.currentGame);
                    else
                        game.setScreen(game.mainMenu);
                    break;
                case 4: game.setScreen(game.mainMenu); break; // Back to main menu
            }
        }
    }

    private void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(640, 480);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        updateFontScale(); // Update font scale after toggling fullscreen
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateFontScale();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        shapeRenderer.dispose();
    }
}
