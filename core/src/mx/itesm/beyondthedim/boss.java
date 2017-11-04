package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Arturo on 02/11/17.
 */

public class boss extends Objeto {

    private TextureRegion bossTexture;
    TextureRegion[][] texturaPersonaje;
    private Animation<TextureRegion> spriteAnimation;
    private float timerAnimation;
    private float x;
    private float y;
    private int life;

    public boss(float x, float y, int life){
        this.x    = x;
        this.y    = y;
        this.life = life;

        bossTexture = new TextureRegion(new Texture("Personaje/jett_completo.png"));


        texturaPersonaje = bossTexture.split(63,100);

        spriteAnimation = new Animation(0.1f, texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1]);

        spriteAnimation.setPlayMode(Animation.PlayMode.LOOP);


        timerAnimation = 0;

        sprite.setPosition(x,y);    // Posición inicial

    }


    public int getLife(){
        return this.life;
    }

    public int Damege(int Damage){
        return (this.life -= Damage);
    }

    public void atack (Personaje personaje){
        //Do something
    }

    public void specialAtack (){
        //Do something
    }

    public void teleport(Personaje personaje){
        //Do something
    }



}
