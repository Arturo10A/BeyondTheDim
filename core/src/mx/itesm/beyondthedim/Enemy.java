package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class Enemy extends Objeto{

    private Texture enemmyTexture;
    private float x;
    private float y;
    private float life;
    private int damage;
    public static final int SPEED=1;


    public Enemy(float x, float y, float life, int damage){
        this.x = x;
        this.y = y;
        this.life = life;
        this.damage = damage;
        enemmyTexture = new Texture("Enemigos/enemigo_mosca.png");
    }

    public void render(SpriteBatch batch){batch.draw(enemmyTexture, x, y);}


    public void mover(float dx, float dy){
        x += dx;
        y += dy;
    }

    public void goBack(){
        mover(50,0);
    }

    public void atack(Personaje target){

        this.x +=  ((float) ((target.getPositionX() - this.getPositionX()) * 0.02));
        this.y +=  ((float) ((target.getPositionY() - this.getPositionY()) * 0.02));

    }

    public void doDamage(Personaje target){
        if (distance(target) < 80){
            target.damage(this.damage);
        }
    }

    public double distance(Personaje target){

        //character position
        double personajeX = target.getPositionX();
        double personajeY = target.getPositionY();

        //Enemy position
        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt( Math.pow(personajeX-currentX,2) + Math.pow(personajeY-currentY,2));
        return distance;

    }

    public boolean isDead(){
        if (this.life > 0){
            return false;
        }
        return true;
    }


    public void receiveDamage(int damage){

        this.life-=damage;
        System.out.println("Enemy life: "+this.life);
    }

    public float getPositionX(){return this.x;}
    public float getPositionY(){return this.y;}

}
