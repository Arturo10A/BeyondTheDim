package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Arturo on 08/09/17.
 */

class Personaje extends Objeto{

    private TextureRegion jettTextureCompleta;
    private Animation<TextureRegion> spriteAnimado;
    private float timerAnimacion;
    private float  x;
    private  float y;
    private int life;


    public Personaje(float x, float y, int life){

        this.x = x;
        this.y = y;
        this.life = life;
        jettTextureCompleta = new TextureRegion(new Texture("Personaje/jett_completo.png"));

        //Divide en 4 frames 63x100
        TextureRegion[][] texturaPersonaje = jettTextureCompleta.split(63,100);

        //Se crea la animacion con tiempo de 0.25 segundos entre frames
        spriteAnimado = new Animation(0.1f, texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1]);

        //Animación infinita
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    //return the current life of the character
    public int getLife(){
        return this.life;
    }

    // return the current life after recive a attack
    public int damage(int damage){
        this.life -= damage;
        return this.life;
    }

    public void render(SpriteBatch batch, float tiempo){

        timerAnimacion+= tiempo;
        batch.draw(spriteAnimado.getKeyFrame(timerAnimacion, true), x,y);

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
        /*
        if(this.getPositionX()>1280*0.11 && this.getPositionX()<1280-(1280*0.12)) {
            x += dx;
        }else{
            if(this.getPositionX()>1280-(1280*0.12)){
                x-=2;
            }
            if(this.getPositionX()<1280*0.12) {
                x += 2;
            }
        }
        if(this.getPositionY()>720*0.21 && this.getPositionY()<720-(720*0.21)) {
            y += dy;
        }else{
            if(this.getPositionX()>720-(1280*0.21)){
                y-=2;
            }
            if(this.getPositionX()<720*0.21) {
                y += 2;
            }
        }*/
    }

}
