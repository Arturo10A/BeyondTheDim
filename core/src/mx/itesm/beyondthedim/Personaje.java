package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class Personaje extends Objeto{

    private TextureRegion jettTextureCompleta;
    TextureRegion[][] texturaPersonaje;
    private Animation<TextureRegion> spriteAnimado;
    protected EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;
    private float timerAnimacion;
    private float speed =2;
    private float x;
    private float y;
    private int life;

    public  Personaje(float x, float y){
        this.x = x;
        this.y = y;
        this.life = 10;
        jettTextureCompleta = new TextureRegion(new Texture("Personaje/jett_completo.png"));

        //Divide en 4 frames 63x100
        texturaPersonaje = jettTextureCompleta.split(63,100);

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


    public Personaje(float x, float y, int life){
        this.life = life;
        this.x = x;
        this.y = y;
        jettTextureCompleta = new TextureRegion(new Texture("Personaje/jett_completo.png"));

        //Divide en 4 frames 63x100
        texturaPersonaje = jettTextureCompleta.split(63,100);

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

    public void setTexture(TextureRegion textura){

    }

    //return the current life of the character
    public int getLife(){
        return this.life;
    }

    // return the current life after recive a attack
    public int damage(double damage){
        this.life -= damage;
        return this.life;
    }

    public void dibujar(SpriteBatch batch, float tiempo){
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += tiempo;
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion, true);
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

    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento, SpriteBatch batch, float tiempo) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public float getPositionX(){
        return x;
    }

    public float getPositionY(){
        return y;
    }



    public  void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }


    public void mover (float dx, float dy){
        x += dx*speed;
        y += dy*speed;
        sprite.setX(x);
        sprite.setY(y);
    }

    public void recolectarObjetos(){

    }




}
