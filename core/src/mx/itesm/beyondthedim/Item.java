package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class Item extends Objeto {

    /*Si requiere animacion
    private TextureRegion itemTextureCompleta;
    TextureRegion[][] texturaItem;
    private Animation<TextureRegion> spriteAnimado;
    protected Personaje.EstadoMovimiento estadoMovimiento = Personaje.EstadoMovimiento.QUIETO;
    private float timerAnimacion;
    */
    private float x;
    private float y;
    private Texture textura;

    public Item(float x, float y, TextureRegion textura){

        this.x = x;
        this.y = y;

    }

}
