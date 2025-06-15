package com.paradise_seeker.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.paradise_seeker.game.main.Main;

public class ControlScreen implements Screen {

	final Main game;
	BitmapFont font;
	GlyphLayout layout;
 // he thong dieu khien
	String[][] controls = {
			{"Moving", "WASD / Arrow keys"},
			{"Dashing", "Move + Left Shift"},
			{"Attack", "SPACE"},
			{"Skill 1", "U"},
			{"Skill 2", "I"},
			{"Pause Game", "ESC"},
			{"Inventory", "B"},
			{"   - Use Item", "E"},
			{"   - Drop Item", "Q"}
	};

	public ControlScreen(Main game) {
		this.game = game;
		this.font = game.font; // dùng font chung
		this.layout = new GlyphLayout();
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		// Thiết lập camera và viewport
		float viewportWidth = game.viewport.getWorldWidth();
        float viewportHeight = game.viewport.getWorldHeight();
        // xóa màn hình với màu nền
		ScreenUtils.clear(Color.DARK_GRAY);
       // Cập nhật camera và vẽ lên batch
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		// Bắt đầu vẽ
		game.batch.begin();

		float xLeft = 2f; // vị trí cột trái
		float xRight = 10f;// vị trí cột phải
		float yStart = game.viewport.getWorldHeight() - 2f;// vị trí bắt đầu vẽ từ trên xuống
		float lineHeight = 0.75f;//khoang gian giữa các dòng

		font.setColor(Color.WHITE);//mau chữ trắng

		for (int i = 0; i < controls.length; i++) {
			// lấy từng cặp điều khiển
			String left = controls[i][0];
			String right = controls[i][1];

			float y = yStart - i * lineHeight;// thay đối vi trí y theo dòng
          	// vẽ chữ
			font.draw(game.batch, left, xLeft, y);
			font.draw(game.batch, right, xRight, y);
		}
		// Tiêu đề
		font.setColor(Color.RED);
		layout.setText(font, "- Controls -");
		//lấy vị trí để căn giữa
        float x = (viewportWidth - layout.width) / 2f;
		font.draw(game.batch, layout, x, viewportHeight);
		// Hint để quay lại
		font.setColor(Color.YELLOW);
		font.draw(game.batch, "[ESC] Return", xLeft, 0.5f);
		font.setColor(Color.WHITE);
		game.batch.end();

		// Quay lại SettingScreen
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(game.currentGame);
		}
	}

	@Override public void resize(int width, int height) {
		game.viewport.update(width, height, true);
	}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}
	@Override public void dispose() {}
}

