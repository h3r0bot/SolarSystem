package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Anatoly on 05.02.2019.
 */
public class Body {
    public Vector2 position;
    public Vector2 velocity;
    private static Texture myTexture;
    //private static Texture moonTexture;

    public static void setMyTexture(Texture myTexture) {
        Body.myTexture = myTexture;
    }
    //public static void setMoonTexture(Texture moonTexture) { Body.moonTexture = moonTexture; }

    public Body(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void render(SpriteBatch batch) {
        batch.draw(myTexture, position.x - 25, position.y - 25);
        //batch.draw(moonTexture, position.x - 25, position.y - 25);
    }

    public void update() {
        position.add(velocity);
        velocity.scl(Gdx.graphics.getDeltaTime(),  Gdx.graphics.getDeltaTime());
    }
}
