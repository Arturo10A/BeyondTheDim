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
    public static final int SPEED=400;
    private static Texture texture;

    float x,y,dx,dy;

    public boolean removeB = false;

    public Bullet(float x, float y, float dx,float dy){

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;


        if (texture == null) {
            texture = new Texture("Objetos_varios/bala_circular.png");
        }
    }


    public float getPositionX(){
        return this.x;
    }

    public float getPositionY(){
        return this.y;
    }

    public void update (float deltaTime)
    {
        if(dx==0 || dy==0){
            System.out.println("ENTRO");
            x+= (SPEED*1.5*deltaTime*dx);
            y+= (SPEED*1.5*deltaTime*dy);
        }else{
            x+= SPEED*deltaTime*(dx);
            y+= SPEED*deltaTime*(dy);
        }

    }


    public void render (SpriteBatch batch){
        batch.draw(texture,x,y);
    }

    public void collisionD(Double distance){

        if (distance < 2){
            System.out.println("** collisionD **");
        }


    }

    public double distance(Enemy target){

        //character position
        double enemyX = target.getPositionX();
        double enemyY = target.getPositionY();

        //Enemy position
        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt( Math.pow(enemyX-currentX,2) + Math.pow(enemyY-currentY,2));
        return distance;

    }

}
