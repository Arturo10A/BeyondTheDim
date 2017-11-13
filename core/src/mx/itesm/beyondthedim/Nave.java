package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class Nave extends Objeto {

    private TextureRegion naveTextureCompleta;
    private TextureRegion[][] texturaNave;
    private Animation<TextureRegion> spriteAnimado;
    protected Personaje.EstadoMovimiento estadoMovimiento = EstadoMovimiento.ACTIVO;
    private float timerAnimacion;
    private float x;
    private float y;
    //Medidas nave sprite
    protected float ANCHO = 1400/5; //280
    protected float ALTO = 225;

    public Nave(float x, float y){
        this.x = x;
        this.y = y;
        naveTextureCompleta = new TextureRegion(new Texture("Objetos_varios/nave_completa.png"));

        //Divide en 4 frames 280*225
        texturaNave = naveTextureCompleta.split(280,225);

        //Se crea la animacion con tiempo de 0.25 segundos entre frames
        spriteAnimado = new Animation(0.1f, texturaNave[0][3], texturaNave[0][2], texturaNave[0][1], texturaNave[0][0]);

        //Animación infinita
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaNave[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    public void dibujar(SpriteBatch batch, float tiempo, float ancho, float alto){
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case ACTIVO:
                timerAnimacion += tiempo;
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion, true);
                batch.draw(region,x,y);
                break;
            case PROPULSOR:
                ANCHO-=ancho;
                ALTO-=alto;
                batch.draw(texturaNave[0][4], x, y, ANCHO, ALTO);
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

    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public void mover (float dx, float dy){
        x += dx;
        y += dy;
        sprite.setX(x);
        sprite.setY(y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
