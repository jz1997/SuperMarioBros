package com.jz.game.supermariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.jz.game.supermariobros.SuperMarioBros;
import com.jz.game.supermariobros.scences.Hud;


public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLOCK_COIN = 28;

    private Sound coinSound;
    private Sound blockCoinSound;

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Mario.COIN_BIT);

        tileSet = map.getTileSets().getTileSet("tileset_gutter");

        coinSound = SuperMarioBros.assetManager.get("audio/sounds/coin.wav", Sound.class);
        blockCoinSound = SuperMarioBros.assetManager.get("audio/sounds/bump.wav", Sound.class);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        playSound();

        getCell().setTile(tileSet.getTile(BLOCK_COIN));
        Hud.addScore(200);
    }

    private void playSound() {
        if (getCell().getTile().getId() == BLOCK_COIN) {
            blockCoinSound.play();
        } else {
            coinSound.play();
        }
    }
}
