package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Arturo on 18/11/17.
 */

public class BulletBoss {

    private float lifeTime;
    private float lifeTimer;
    public static final int SPEED=600;
    private static Texture texture;
    public int damage =20;

    float x,y,dx,dy;

    public boolean removeB = false;

    public BulletBoss(float x, float y, float dx,float dy){

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;


        if (texture == null) {
            texture = new Texture("Objetos_varios/bala_boss.png");
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

        x += SPEED*deltaTime*dx;
        y += SPEED*deltaTime*dy;

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

    public double distanceJett(Personaje target){

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
