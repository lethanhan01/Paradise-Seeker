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

public class ControlScreen implements Screen {

	public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
	public GlyphLayout layout;
	
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
		this.font = game.font; // dùng font chung
		this.batch = game.batch; // dùng batch chung
		this.camera = game.camera; // dùng camera chung
		this.viewport = game.viewport; // dùng viewport chung
		this.layout = new GlyphLayout();
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		// Thiết lập camera và viewport
    	com.paradise_seeker.game.main.Main game = (com.paradise_seeker.game.main.Main) Gdx.app.getApplicationListener();

		float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();
        // xóa màn hình với màu nền
		ScreenUtils.clear(Color.DARK_GRAY);
       // Cập nhật camera và vẽ lên batch
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		// Bắt đầu vẽ
		batch.begin();

		float xLeft = 2f; // vị trí cột trái
		float xRight = 10f;// vị trí cột phải
		float yStart = viewport.getWorldHeight() - 2f;// vị trí bắt đầu vẽ từ trên xuống
		float lineHeight = 0.75f;//khoang gian giữa các dòng

		font.setColor(Color.WHITE);//mau chữ trắng

		for (int i = 0; i < controls.length; i++) {
			// lấy từng cặp điều khiển
			String left = controls[i][0];
			String right = controls[i][1];

			float y = yStart - i * lineHeight;// thay đối vi trí y theo dòng
          	// vẽ chữ
			font.draw(batch, left, xLeft, y);
			font.draw(batch, right, xRight, y);
		}
		// Tiêu đề
		font.setColor(Color.RED);
		layout.setText(font, "- Controls -");
		//lấy vị trí để căn giữa
        float x = (viewportWidth - layout.width) / 2f;
		font.draw(batch, layout, x, viewportHeight);
		// Hint để quay lại
		font.setColor(Color.YELLOW);
		font.draw(batch, "[ESC] Return", xLeft, 0.5f);
		font.setColor(Color.WHITE);
		batch.end();

		// Quay lại SettingScreen
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(game.currentGame);
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

