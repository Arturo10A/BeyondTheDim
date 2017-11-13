package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Arturo on 02/11/17.
 */

public class boss extends Objeto {

    Texture bossTexture;
    TextureRegion texturaPersonaje;
    public Animation<TextureRegion> spriteAnimation;
    public float timerAnimation;
    protected float x;
    protected float y;
    private int life;
    private int teleportCont = 0;

    public boss(float x, float y, int life){
        this.x    = x;
        this.y    = y;
        this.life = life;

        bossTexture      = new Texture("Personaje/jett.png");

        //texturaPersonaje = bossTexture.split(63,100);
        //spriteAnimation = new Animation(0.1f, texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1]);
        //spriteAnimation.setPlayMode(Animation.PlayMode.LOOP);
        //timerAnimation = 0;
       //sprite.setPosition(x,y);    // Posición inicial

    }


    public int getLife(){
        return this.life;
    }

    public int Damege(int Damage){
        System.out.println("Boss Life: " + this.life);
        this.x = this.x - 1;
        return (this.life -= Damage);
    }

    public void atack (Personaje target){
        //Do something
        //target.damage(100);

        this.x +=  ((float) ((target.getPositionX() - this.getPositionX()) * 0.02));
        this.y +=  ((float) ((target.getPositionY() - this.getPositionY()) * 0.02));

        if (distancePersonaje(target) < 100){
            target.damage(0.1);
        }

    }

    public void isUnderAtack(Bullet bullet, Personaje target){

       float bulletX = bullet.getPositionX();
       float bulletY = bullet.getPositionY();
       float x = target.getPositionX();
       float y = target.getPositionY();

        if (teleportCont < 3 && this.life < 10) {

            if (distance(bullet) <= 100) {
                if (bulletX > x) {
                    this.x = bulletX - 102;
                } else if (bulletX < x) {
                    this.x = bulletX + 102;
                } else if (bulletY > y) {
                    this.y = bulletY - 101;
                } else if (bulletY < y) {
                    this.y = bulletY + 101;
                }
            }

            teleportCont += 1;

        }
        else if (teleportCont >= 3 && teleportCont <= 10 && this.life < 10){
            teleportCont += 1;
        }else {
            teleportCont = 0;
        }
    }


    public void teleport(Personaje personaje){
        //Do something
        /*x = personaje.getPositionX();
        y = personaje.getPositionY();

        this.x = x+80;
        this.y = y;*/
    }

    protected float getPositionX(){
        return this.x;
    }

    protected float getPositionY(){
        return this.y;
    }

    public boolean collisonBullet(Bullet bullet){

        if (distance(bullet) < 100)
            return true;
        return false;
    }

    public double distance(Bullet bullet){

        //character position
        double bulletX = bullet.getPositionX();
        double bulletY = bullet.getPositionY();

        //Enemy position
        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt(Math.pow(bulletX-currentX,2) + Math.pow(bulletY-currentY,2));
        return distance;

    }

    public double distancePersonaje(Personaje target){

        //character position
        double personajeX = target.getPositionX();
        double personajeY = target.getPositionY();

        //Enemy position
        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt(Math.pow(personajeX-currentX,2) + Math.pow(personajeY-currentY,2));
        return distance;

    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(bossTexture,x,y);
    }

}
