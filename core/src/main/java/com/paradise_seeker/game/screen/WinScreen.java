package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.paradise_seeker.game.main.Main;
import com.paradise_seeker.game.screen.cutscene.IntroCutScene;

public class WinScreen implements Screen {

    private final Main game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture background;
    private final GlyphLayout layout;

    private String[] buttons = {"Play Again", "Back to Menu"};
    private int selectedIndex = 0;

    public WinScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = game.font;  // dùng font mặc định
        this.font.setColor(Color.WHITE);
        this.background = new Texture("menu/win_menu/menu_end/win_menu.png");
        this.layout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float screenWidth = Gdx.graphics.getWidth();
        
        // Tiêu đề
        String title = "Congratulations! You Win!";
        layout.setText(font, title);
        float titleX = (screenWidth - layout.width) / 2;
        font.draw(batch, title, titleX, 430);

        // Danh sách nút
        for (int i = 0; i < buttons.length; i++) {
            if (i == selectedIndex) {
                font.setColor(Color.BLUE);
            } else {
                font.setColor(Color.WHITE);
            }
            layout.setText(font, buttons[i]);
            float buttonX = (screenWidth - layout.width) / 2;
            font.draw(batch, buttons[i], buttonX, 350 - i * 60);
        }

        // Credit
        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(0.85f);
        
        String credit = "- Credits - ";
        layout.setText(font, credit);
        float creditX = (screenWidth - layout.width) / 2;
        font.draw(batch, credit, creditX, 210);
        
        String[] members = {
            "Nguyen Hoang Hiep", "Le Thanh An", "Pham Yen Nhi", 
            "Bui Tuan Anh", "Nguyen Hoang Long", "Trinh Van Minh"
        };
        
        for (int i = 0; i < members.length; i++) {
            layout.setText(font, members[i]);
            float memberX = (screenWidth - layout.width) / 2;
            font.draw(batch, members[i], memberX, 180 - i * 30);
        }
        
        font.getData().setScale(1.3f);

        batch.end();

        // Di chuyển chọn
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + buttons.length) % buttons.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % buttons.length;
        }

        // Xác nhận chọn
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedIndex) {
                case 0:
                    game.currentGame = null;
                    game.inventoryScreen = null; // Reset inventory screengam
                    game.currentGame = new GameScreen(game);
                    game.setScreen(new IntroCutScene(game));
                    break;
                case 1:

                	game.currentGame = null; // Reset current game
                	game.inventoryScreen = null; // Reset inventory screen
                    game.setScreen(game.mainMenu); // quay lại menu
                    break;
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {
    	font.getData().setScale( Main.WORLD_HEIGHT / Gdx.graphics.getHeight());
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
        background.dispose();
        // layout does not need to be disposed
    }
}
