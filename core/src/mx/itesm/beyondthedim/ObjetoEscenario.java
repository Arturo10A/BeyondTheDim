package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class ObjetoEscenario extends Objeto {
    private TextureRegion jettTextureCompleta;
    Texture texturaObjeto;
    private Animation<TextureRegion> spriteAnimado;
    protected EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;
    private float x;
    private float y;


    public ObjetoEscenario(float x, float y, Texture textura){
        this.x = x;
        this.y = y;
        texturaObjeto = textura;
        sprite = new Sprite(texturaObjeto);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    public void setTexture(Texture textura){
        this.texturaObjeto = textura;
    }

    public void dibujar(SpriteBatch batch, float tiempo){
        batch.draw(texturaObjeto, x, y);
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
}
