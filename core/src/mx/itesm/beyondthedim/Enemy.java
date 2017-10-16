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
    private int damage;


    public Enemy(float x, float y, float life, int damage){

        this.x = x;
        this.y = y;
        this.life = life;
        this.damage = damage;
        enemmyTexture = new Texture("block.png");

    }

    public void render(SpriteBatch batch){
        batch.draw(enemmyTexture, x, y);
    }

    public void mover(float dx, float dy){
        x += dx;
        y += dy;
    }

    public void atack(Personaje target){

        this.x += (float) ((target.getPositionX() - this.getPositionX()) * 0.2);
        this.y += (float) ((target.getPositionY() - this.getPositionY()) * 0.2);

    }

    public void doDamage(Personaje target){

        if (distance(target) < 80){
            target.damage(this.damage);
            System.out.println("doDamge");
            System.out.println("Jett life: "+target.getLife());
        }

    }

    public double distance(Personaje target){

        double personajeX = target.getPositionX();
        double personajeY = target.getPositionY();

        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt( Math.pow(personajeX-currentX,2) + Math.pow(personajeY-currentY,2));
        return distance;

    }

    //Change method name
    public float getPositionX(){
        return this.x;
    }

    //Change method name
    public float getPositionY(){
        return this.y;
    }

}
