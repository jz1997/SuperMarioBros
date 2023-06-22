package com.jz.game.supermariobros.sprites;

import static com.jz.game.supermariobros.SuperMarioBros.PPM;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jz.game.supermariobros.screens.PlayScreen;

public class Mario extends Sprite {

    public enum State {
        STANDING,
        FALLING,
        JUMPING,
        RUNNING
    }

    private final World world;
    public Body b2body;

    private TextureRegion marioStand;

    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private float animationTimer = 0;
    private State currentState;
    private State previousState;
    private boolean runningRight;

    public static final short DEFAULT_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROY_BIT = 16;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        defineMario();

        TextureAtlas.AtlasRegion littleMarioRegion = screen.getAtlas().findRegion("little_mario");
        marioStand = new TextureRegion(littleMarioRegion, 0, 0, 16, 16);
        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(marioStand);

        animationTimer = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
        // 初始化 running animation
        for (int index = 1; index < 4; index++) {
            frames.add(new TextureRegion(littleMarioRegion, index * 16, 0, 16, 16));
        }
        runningAnimation = new Animation<>(0.1f, frames);
        frames.clear();

        // 初始化 jumping animation
        for (int index = 4; index < 6; index++) {
            frames.add(new TextureRegion(littleMarioRegion, index * 16, 0, 16, 16));
        }
        jumpingAnimation = new Animation<>(0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = jumpingAnimation.getKeyFrame(animationTimer);
                break;
            case RUNNING:
                // 跑步动作需要循环
                region = runningAnimation.getKeyFrame(animationTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        // 向左移动, 并且没有做翻转
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            runningRight = false;
            region.flip(true, false);
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            runningRight = true;
            region.flip(true, false);
        }

        animationTimer = previousState == currentState ? animationTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    private void defineMario() {
        // create body def
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // create fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        fdef.shape = shape;
        fdef.filter.categoryBits = MARIO_BIT;
        fdef.filter.maskBits = DEFAULT_BIT | COIN_BIT | BRICK_BIT;
        shape.setRadius(6 / PPM);
        b2body.createFixture(fdef);

        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-2f / PPM, 6f / PPM), new Vector2(2f / PPM, 6f / PPM));
        fdef.shape = headShape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");
    }
}
