package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Arturo on 08/09/17.
 */

class Personaje {

    private Texture jettTexture;
    private float  x;
    private  float y;


    public Personaje(float x, float y){

        this.x = x;
        this.y = y;
        jettTexture = new Texture("jett.png");

    }

    public void render(SpriteBatch batch){

        batch.draw(jettTexture,x,y);

    }


    public void mover (float dx, float dy){
        x += dx;
        y += dy;
    }
}
