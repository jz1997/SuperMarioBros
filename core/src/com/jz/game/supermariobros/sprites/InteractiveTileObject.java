package com.jz.game.supermariobros.sprites;

import static com.jz.game.supermariobros.SuperMarioBros.PPM;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jz.game.supermariobros.screens.PlayScreen;

/**
 * 交互式对象
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen playScreen, Rectangle bounds) {
        this.world = playScreen.getWorld();
        this.map = playScreen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.x + bounds.width / 2) / PPM, (bounds.y + bounds.height / 2) / PPM);
        body = world.createBody(bdef);

        shape.setAsBox(bounds.width / 2 / PPM, bounds.height / 2 / PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        // 获取 Graphics Layer, 在 tiled 文件中属于第二层, 下标为 1
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        // 将 M 转化成像素, 然后 /16 算出在 tiled 文件中的单元格位置
        return layer.getCell((int) (body.getPosition().x * PPM / 16), (int) (body.getPosition().y * PPM / 16));
    }
}
