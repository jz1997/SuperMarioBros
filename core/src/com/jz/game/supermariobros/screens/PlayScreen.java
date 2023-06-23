package com.jz.game.supermariobros.screens;

import static com.jz.game.supermariobros.SuperMarioBros.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jz.game.supermariobros.SuperMarioBros;
import com.jz.game.supermariobros.scences.Hud;
import com.jz.game.supermariobros.sprites.Enemy;
import com.jz.game.supermariobros.sprites.Goomba;
import com.jz.game.supermariobros.sprites.Mario;
import com.jz.game.supermariobros.tools.B2WorldCreator;
import com.jz.game.supermariobros.tools.WorldContactListener;

public class PlayScreen implements Screen {
    private TextureAtlas atlas;
    private SuperMarioBros game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Box2d
    private B2WorldCreator worldCreator;
    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario mario;

    private Music music;

    private Goomba goomba;

    public PlayScreen(SuperMarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.atlas");

        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SuperMarioBros.V_WIDTH / PPM, SuperMarioBros.V_HEIGHT / PPM, camera);
        this.hud = new Hud(game.batch);

        // set tmx map loader
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tmx/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // set camera position
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // Box2d
        world = new World(new Vector2(0, -10), true);
        // Register world contact listener
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();


        // create world
        worldCreator = new B2WorldCreator(this);

        mario = new Mario(this);

        // Music Play
        music = SuperMarioBros.assetManager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        // Goomba
        goomba = new Goomba(this, 5.64f, .16f);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float dt) {
        // update
        update(dt);

        // clear
        clear();

        // render map
        mapRenderer.render();

        // render box2d debug lines
        b2dr.render(world, camera.combined);

        // draw mario
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        mario.draw(game.batch);

        // draw goomba
        for (Enemy enemy : worldCreator.getGoombas()) {
            enemy.draw(game.batch);
        }
        game.batch.end();


        // draw hub camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    public void clear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void update(float dt) {
        handleInput(dt);

        // word step
        world.step(1 / 60f, 1, 2);

        // move camera x position with mario
        camera.position.x = mario.b2body.getPosition().x;

        // update mario
        mario.update(dt);

        // update goomba
        for (Enemy enemy : worldCreator.getGoombas()) {
            enemy.update(dt);
        }
//        goomba.update(dt);

        // update hud
        hud.update(dt);

        // update camera
        camera.update();
        mapRenderer.setView(camera);
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            mario.b2body.applyLinearImpulse(new Vector2(0, 4f), mario.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && mario.b2body.getLinearVelocity().x <= 2) {
            mario.b2body.applyLinearImpulse(new Vector2(0.1f, 0), mario.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && mario.b2body.getLinearVelocity().x >= -2) {
            mario.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), mario.b2body.getWorldCenter(), true);
        }
    }

    public TiledMap getMap() {
        return this.map;
    }

    public World getWorld() {
        return this.world;
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        hud.dispose();
        mapRenderer.dispose();
        b2dr.dispose();
        world.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
