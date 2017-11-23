package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Arturo on 02/11/17.
 */

public class Boss extends Objeto {
    private TextureRegion bossTexture;
    //TextureRegion texturaPersonaje;
    TextureRegion [][] texturaPersonaje;
    public Animation<TextureRegion> spriteAnimationBoss;
    public float timerAnimation;
    protected float x;
    protected float y;
    private int life;
    private int teleportCont = 0;

    protected Personaje.EstadoMovimiento estadoMovimiento = Personaje.EstadoMovimiento.QUIETO;

    public Boss(float x, float y, int life){
        this.x    = x;
        this.y    = y;
        this.life = life;
        Texture textureTemp = new Texture("Enemigos/boss_completo.png");
        bossTexture = new TextureRegion(textureTemp);
        texturaPersonaje = bossTexture.split(65,116);
        spriteAnimationBoss = new Animation(0.2f, texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1]);
        spriteAnimationBoss.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimation = 0;
        sprite = new Sprite(texturaPersonaje[0][0]);
        sprite.setPosition(x,y);    // Posición inicial

    }


    public int getLife(){
        return this.life;
    }

    public int Damege(int Damage){
        System.out.println("Boss Life: " + this.life);
        this.x = this.x - 1;
        return (this.life -= Damage);
    }

    public void atack(Personaje target){
        //Do something
        //target.damage(100);

        this.x +=  ((float) ((target.getPositionX() - this.getPositionX()) * 0.02));
        this.y +=  ((float) ((target.getPositionY() - this.getPositionY()) * 0.02));
        this.setEstadoMovimiento(target.getEstadoMovimiento());

        if (distancePersonaje(target) < 100){
            target.damage(1);
        }
        this.x +=  ((float) ((target.getPositionX() - this.getPositionX()) * 0.02));
        this.y +=  ((float) ((target.getPositionY() - this.getPositionY()) * 0.02));

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

    public void dibujar(SpriteBatch batch, float tiempo) {

        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimation += tiempo;
                // Frame que se dibujará
                TextureRegion region = spriteAnimationBoss.getKeyFrame(timerAnimation, true);
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,x,y);
                break;
            case QUIETO:
            case INICIANDO:
                batch.draw(texturaPersonaje[0][0], x, y);
                break;
        }
    }

    public void setEstadoMovimiento(Boss.EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }


}
