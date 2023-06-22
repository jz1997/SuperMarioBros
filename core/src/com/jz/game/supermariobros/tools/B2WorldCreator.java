package com.jz.game.supermariobros.tools;

import static com.jz.game.supermariobros.SuperMarioBros.PPM;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jz.game.supermariobros.sprites.Brick;
import com.jz.game.supermariobros.sprites.Coin;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;


        // create ground bodies/fixture
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.x + rectangle.width / 2) / PPM, (rectangle.y + rectangle.height / 2) / PPM);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox((rectangle.width / 2) / PPM, (rectangle.height / 2) / PPM);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        // create pipes bodies/fixture
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.x + rectangle.width / 2) / PPM, (rectangle.y + rectangle.height / 2) / PPM);

            body = world.createBody(bodyDef);

            polygonShape.setAsBox((rectangle.width / 2) / PPM, (rectangle.height / 2) / PPM);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        // create coins bodies/fixture
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();

            new Coin(world, map, rectangle);
        }

        // create bricks bodies/fixture
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();

            new Brick(world, map, rectangle);
        }
    }
}
