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
    private int life;


    public Personaje(float x, float y, int life){

        this.x = x;
        this.y = y;
        this.life = life;
        jettTexture = new Texture("jett.png");

    }

    //return the current life of the character
    public int getLife(int life){
        return this.life;
    }

    // return the current life after recive a attack
    public int damage(int damage){
        this.life -= damage;
        return this.life;
    }

    public void render(SpriteBatch batch){

        batch.draw(jettTexture,x,y);

    }

    public float getPositionX(){
        return this.x;
    }

    public float getPositionY(){
        return this.y;
    }

    public void mover (float dx, float dy){
        x += dx;
        y += dy;
    }
}
