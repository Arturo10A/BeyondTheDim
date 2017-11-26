package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Arturo on 20/11/17.
 */

public class Medicina extends Objeto {

    private TextureRegion texturaRegion;
    private Texture textura;
    float x;
    float y;
    private boolean estado;

    public Medicina(float x, float y){
        this.textura = new Texture("Objetos_varios/medicina.png");
        this.x = x;
        this.y = y;
        this.estado = false;
        texturaRegion = new TextureRegion(textura);
    }

    public boolean vida(Personaje personaje){
        double personajeX = personaje.getPositionX();
        double personajeY = personaje.getPositionY();
        double distance = Math.sqrt( Math.pow(personajeX-this.x,2) + Math.pow(personajeY-this.y,2));
        if (distance <= 70 && personaje.getLife() < 1000 && estado == false){
            personaje.recoverLife();
            System.out.println("vida recover");
            this.estado = true;
            return estado;

        }
        else
            return estado;
    }

    public void dib(Batch batch){
        batch.draw(textura,this.x,this.y);
    }
}
