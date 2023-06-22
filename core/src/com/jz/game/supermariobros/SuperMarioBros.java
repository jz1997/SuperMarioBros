package com.jz.game.supermariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jz.game.supermariobros.screens.PlayScreen;

import javax.xml.transform.Source;

public class SuperMarioBros extends Game {
    public final static int V_WIDTH = 400;
    public final static int V_HEIGHT = 208;
    // pixel per meters 每米的像素
    public final static float PPM = 100;

    public SpriteBatch batch;

    public static AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Load Resources
        assetManager = new AssetManager();
        assetManager.load("audio/music/mario_music.ogg", Music.class);
        assetManager.load("audio/sounds/coin.wav", Sound.class);
        assetManager.load("audio/sounds/bump.wav", Sound.class);
        assetManager.load("audio/sounds/breakblock.wav", Sound.class);
        // 加载资源完成
        assetManager.finishLoading();

        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}
