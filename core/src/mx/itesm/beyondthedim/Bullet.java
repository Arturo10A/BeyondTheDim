package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by rodri on 21/10/2017.
 */

class Bullet {
    private float lifeTime;
    private float lifeTimer;
    public static final int SPEED=500;
    private static Texture texture;

    float x,y,dx,dy;

    public boolean removeB = false;

    public Bullet(float x, float y, float dx,float dy){

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;


        if (texture == null) {
            texture = new Texture("like.png");
        }
    }

    public void update (float deltaTime)
    {
        x+= SPEED*deltaTime*dx;
        y+= SPEED*deltaTime*dy;

    }


    public void render (SpriteBatch batch){
        batch.draw(texture,x,y);
    }

}
