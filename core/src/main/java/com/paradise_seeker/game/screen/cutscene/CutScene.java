package com.paradise_seeker.game.screen.cutscene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.paradise_seeker.game.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public abstract class CutScene implements Screen {
    protected final Main game;
    protected List<Texture> backgrounds = new ArrayList<>();
    protected List<String> cutsceneTexts = new ArrayList<>();
    protected float cutsceneDuration;
    protected String skipText = "> Press SPACE to skip <";
    protected GlyphLayout layout;
    protected ShapeRenderer shapeRenderer;
    

    protected int currentSceneIndex = 0;
    protected float timeElapsed = 0f;

    public CutScene(Main game, List<String> backgroundPaths, List<String> texts, float cutsceneDuration) {
        this.game = game;
        this.cutsceneDuration = cutsceneDuration;

        for (String path : backgroundPaths) {
            backgrounds.add(new Texture(path));
        }

        this.cutsceneTexts.addAll(texts);
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        timeElapsed += delta;

        // Tự động chuyển cảnh nếu hết thời lượng
        if (timeElapsed >= cutsceneDuration) {
            nextScene();
        }
        if (currentSceneIndex >= backgrounds.size()) return;

        game.batch.begin();
        game.batch.draw(backgrounds.get(currentSceneIndex), 0, 0,
                game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        game.batch.end();
        
    	drawTextBox( 1, 1, 13, 2);
    	
        game.batch.begin();
        drawCutsceneContent(delta);
        game.batch.end();

        handleInput();
    }

    protected void nextScene() {
        timeElapsed = 0;
        currentSceneIndex++;
        if (currentSceneIndex >=  backgrounds.size()) {
            onCutsceneEnd();
        }
    }
    
    protected void drawTextBox(float x, float y, float width, float height) {
        shapeRenderer.setProjectionMatrix(game.camera.combined);

        // Vẽ nền ô chữ nhật
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f); // Màu đen mờ
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();

        // Vẽ viền trắng (nếu muốn)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }


	protected void drawCutsceneContent(float delta) {
		game.font.draw(game.batch, skipText, 9f, 9.5f); 
		if (currentSceneIndex < backgrounds.size()) {
			String text = cutsceneTexts.get(currentSceneIndex);
			
	        game.font.draw(game.batch, text, 1f, 2.5f);
		}
	
	}

    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            nextScene();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        for (Texture t : backgrounds) {
            t.dispose();
        }
    }

    protected void onCutsceneEnd() {
        // Sau khi cutscene kết thúc, chuyển sang màn hình chơi game
        if (game.currentGame == null) {
            game.currentGame = new com.paradise_seeker.game.screen.GameScreen(game);
        }
        game.setScreen(game.currentGame);
    }
}
