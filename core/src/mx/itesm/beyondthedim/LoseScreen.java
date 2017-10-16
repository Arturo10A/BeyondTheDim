package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Arturo on 16/10/17.
 */

public class LoseScreen extends Pantalla{

    private final Juego juego;
    private Texture background;
    private Texture goBackButtonTexture;

    private Texto texto;


    public LoseScreen(Juego juego){
        this.juego = juego;
    }


    public void cargarTexturas(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        texto.mostrarMensaje(batch,"You Lose", Pantalla.ANCHO/2, Pantalla.ALTO/2);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
