package com.jz.game.supermariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.jz.game.supermariobros.SuperMarioBros;
import com.jz.game.supermariobros.scences.Hud;

public class Brick extends InteractiveTileObject {
    private Sound sound;

    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Mario.BRICK_BIT);

        sound = SuperMarioBros.assetManager.get("audio/sounds/breakblock.wav", Sound.class);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        // 播放音乐
        sound.play();

        // 设置为已经小时的额 filed
        setCategoryFilter(Mario.DESTROY_BIT);
        getCell().setTile(null);

        // 增加分数
        Hud.addScore(50);
    }
}
