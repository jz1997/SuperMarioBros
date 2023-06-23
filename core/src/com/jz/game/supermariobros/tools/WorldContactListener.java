package com.jz.game.supermariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jz.game.supermariobros.sprites.Enemy;
import com.jz.game.supermariobros.sprites.InteractiveTileObject;
import com.jz.game.supermariobros.sprites.Mario;

import java.util.Objects;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int cdef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        if (Objects.equals(fixtureA.getUserData(), "head") || Objects.equals(fixtureB.getUserData(), "head")) {
            Fixture headFixture = Objects.equals(fixtureA.getUserData(), "head") ? fixtureA : fixtureB;
            Fixture objectFixture = Objects.equals(fixtureA.getUserData(), "head") ? fixtureB : fixtureA;
            if (objectFixture.getUserData() != null &&
                    InteractiveTileObject.class.isAssignableFrom(objectFixture.getUserData().getClass())
            ) {
                ((InteractiveTileObject) objectFixture.getUserData()).onHeadHit();
            }
        }


        switch (cdef) {
            case Mario.ENEMY_HEAD_BIT | Mario.MARIO_BIT:
                if (fixtureA.getFilterData().categoryBits == Mario.ENEMY_HEAD_BIT) {
                    Enemy enemy = (Enemy) fixtureA.getUserData();
                    enemy.hitOnHead();
                } else {
                    Enemy enemy = (Enemy) fixtureB.getUserData();
                    enemy.hitOnHead();
                }
                break;


            case Mario.ENEMY_BIT | Mario.OBJECT_BIT:
                Gdx.app.log("contact", "enemy collide with pipes");
                if (fixtureA.getFilterData().categoryBits == Mario.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case Mario.MARIO_BIT | Mario.ENEMY_BIT:
                Gdx.app.log("mario", "mario is died");
                break;
            case Mario.ENEMY_BIT:
                ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("contact", "mario end contact");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
