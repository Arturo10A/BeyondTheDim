package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Arturo on 10/10/17.
 */

public class Enemy {

    private Texture enemmyTexture;
    private float x;
    private float y;
    private float life;


    public Enemy(float x, float y, float life){

        this.x = x;
        this.y = y;
        this.life = life;
        enemmyTexture = new Texture("block.png");

    }

    public void render(SpriteBatch batch){
        batch.draw(enemmyTexture, x, y);
    }

    public void mover(float dx, float dy){
        x += dx;
        y += dy;
    }

    //Change method name
    public float getX(){
        return this.x;
    }

    //Change method name
    public float getY(){
        return this.y;
    }

}
