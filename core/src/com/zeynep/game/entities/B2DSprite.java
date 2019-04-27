package com.zeynep.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zeynep.game.handlers.Animation;
import com.zeynep.game.handlers.B2DVars;

public class B2DSprite {

    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;

    public B2DSprite(Body body) {
        this.body = body;
        animation = new Animation();

    }
    public void setAnimation (TextureRegion[] reg, float delay ) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }
    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(
                animation.getFrame(),
                body.getPosition().x* B2DVars.PIXEL_PER_METER- width / 2,
                body.getPosition().y* B2DVars.PIXEL_PER_METER- height / 2
        );
        spriteBatch.end();
    }
    public Body getBody() {return body; }
    public Vector2 getPosition() {return body.getPosition();}
    public float getWidth() {return width;}
    public float getHeight() {return height;}

}
