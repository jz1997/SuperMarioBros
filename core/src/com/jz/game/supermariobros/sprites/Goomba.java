package com.jz.game.supermariobros.sprites;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.jz.game.supermariobros.SuperMarioBros;
import com.jz.game.supermariobros.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<>();
        TextureAtlas.AtlasRegion goombaAnimationRegion = screen.getAtlas().findRegion("goomba");
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(goombaAnimationRegion, i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation<>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / SuperMarioBros.PPM, 16 / SuperMarioBros.PPM);
        setDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        stateTime += dt;
        if (setDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    @Override
    protected void defineEnemy() {
        // create body def
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // create fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        fdef.shape = shape;
        fdef.filter.categoryBits = Mario.ENEMY_BIT;
        fdef.filter.maskBits = Mario.GROUND_BIT | Mario.COIN_BIT
                | Mario.BRICK_BIT | Mario.ENEMY_BIT | Mario.OBJECT_BIT | Mario.MARIO_BIT;
        shape.setRadius(6 / SuperMarioBros.PPM);
        b2body.createFixture(fdef).setUserData(this);

        // 创建头部碰撞位置
        PolygonShape head = new PolygonShape();
        Vector2[] headVectors = new Vector2[4];
        headVectors[0] = new Vector2(-5, 8).scl(1 / SuperMarioBros.PPM);
        headVectors[1] = new Vector2(5, 8).scl(1 / SuperMarioBros.PPM);
        headVectors[2] = new Vector2(-3, 3).scl(1 / SuperMarioBros.PPM);
        headVectors[3] = new Vector2(3, 3).scl(1 / SuperMarioBros.PPM);
        head.set(headVectors);
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Mario.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setDestroy = true;
    }
}
