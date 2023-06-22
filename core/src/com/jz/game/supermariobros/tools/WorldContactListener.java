package com.jz.game.supermariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jz.game.supermariobros.sprites.InteractiveTileObject;

import java.util.Objects;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (Objects.equals(fixtureA.getUserData(), "head") || Objects.equals(fixtureB.getUserData(), "head")) {
            Fixture headFixture = Objects.equals(fixtureA.getUserData(), "head") ? fixtureA : fixtureB;
            Fixture objectFixture = Objects.equals(fixtureA.getUserData(), "head") ? fixtureB : fixtureA;
            if (objectFixture.getUserData() != null &&
                    InteractiveTileObject.class.isAssignableFrom(objectFixture.getUserData().getClass())
            ) {
                ((InteractiveTileObject) objectFixture.getUserData()).onHeadHit();
            }
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
