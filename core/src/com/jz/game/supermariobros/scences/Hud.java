package com.jz.game.supermariobros.scences;

import static com.jz.game.supermariobros.SuperMarioBros.V_HEIGHT;
import static com.jz.game.supermariobros.SuperMarioBros.V_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import javax.swing.LayoutStyle;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch batch) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // 分数
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format(Locale.getDefault(), "%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // 关卡
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // 时间
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownLabel = new Label(String.format(Locale.getDefault(), "%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt) {
        timeCount += dt;
        // 时间减少
        if (timeCount >= 1) {
            worldTimer--;
            timeCount -= 1;
            countdownLabel.setText(String.format(Locale.getDefault(), "%03d", worldTimer));
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format(Locale.getDefault(), "%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
