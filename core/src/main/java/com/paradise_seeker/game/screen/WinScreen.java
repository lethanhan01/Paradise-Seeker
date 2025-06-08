package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.paradise_seeker.game.main.Main;

public class WinScreen implements Screen {

    private final Main game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture background;

    private String[] buttons = {"Play Again", "Back to Menu"};
    private int selectedIndex = 0;

    public WinScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();  // dùng font mặc định
        this.font.getData().setScale(2f);
        this.font.setColor(Color.WHITE);
        this.background = new Texture("menu/win_menu/menu_end/win_menu.png"); // nên tạo ảnh riêng hoặc dùng tạm ảnh nền cũ
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Tiêu đề
        font.draw(batch, " Congratulations! You Win! ", 100, 450);

        // Danh sách nút
        for (int i = 0; i < buttons.length; i++) {
            if (i == selectedIndex) {
                font.setColor(Color.YELLOW);
            } else {
                font.setColor(Color.WHITE);
            }
            font.draw(batch, buttons[i], 120, 350 - i * 60);
        }

        // Credit
        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f);
        font.draw(batch, "Group Member (22):", 100, 210);
        font.draw(batch, "- Hiep", 120, 180);
        font.draw(batch, "- An", 120, 150);
        font.draw(batch, "- Nhi", 120, 120);
        font.draw(batch, "- Anh", 120, 90);
        font.draw(batch, "- Long", 120, 60);
        font.draw(batch, "- Minh", 120, 30);
        font.getData().setScale(2f);

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
                    game.setScreen(new GameScreen(game)); // chơi lại
                    break;
                case 1:
                    game.setScreen(new MainMenuScreen(game)); // quay lại menu
                    break;
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
        background.dispose();
    }
}
