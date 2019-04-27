package com.zeynep.game.handlers;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {
        private int numFoodContacts;
    @Override
    //called when two fixtures start to collide
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA==null || fixtureB==null) return;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot")) {
           numFoodContacts++;
        }

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot")) {
           numFoodContacts++;
        }
    }

    //called when two fictures no longer collide
    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA==null || fixtureB==null) return;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot")) {
            numFoodContacts--;
        }

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot")) {
            numFoodContacts--;
        }

    }


    //collision detection,presolve,handling,postsolve
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isPlayerOnGround() {
        return numFoodContacts > 0;
    }


}
