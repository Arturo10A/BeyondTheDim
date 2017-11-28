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
    private float alto;
    private float ancho;


    public ObjetoEscenario(float x, float y, Texture textura){
        this.x = x;
        this.y = y;
        texturaObjeto = textura;
        sprite = new Sprite(textura);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
        alto = textura.getWidth();
        ancho = textura.getHeight();
    }


    public void setTexture(Texture textura){
        this.sprite = new Sprite(textura);
    }

    public void dibujar(SpriteBatch batch, float tiempo){
        batch.draw(sprite, x, y);
    }

    public void dibujar(SpriteBatch batch, float alto, float ancho){
        this.alto = alto;
        this.ancho = ancho;
        batch.draw(sprite, x, y, ancho, alto);
    }

    public float getPositionX(){
        return x;
    }

    public float getPositionY(){
        return y;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public void rota(float grado){
        this.sprite.rotate(grado);
    }
    public  void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getAlto(){
        return alto;
    }

    public float getAncho(){
        return ancho;
    }
}
