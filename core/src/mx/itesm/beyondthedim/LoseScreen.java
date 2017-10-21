package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Arturo on 16/10/17.
 */

public class LoseScreen extends Pantalla{

    private final Juego juego;
    private Texture background;
    private Texture backButtonInicio;
    private Texture backButtonReoload;

    private Texto texto;

    public LoseScreen(Juego juego){
        this.juego = juego;
    }

    public void cargarTexturas(){
        background = new Texture("fondoSettings.png");
        backButtonInicio = new Texture("button_inicio.png");
        backButtonReoload = new Texture("button_reload.png");
    }

    @Override
    public void show() {

        //Gdx.input.setInputProcessor(escenaLose);

    }

    @Override
    public void render(float delta) {


        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        texto.mostrarMensaje(batch,"You Died", Pantalla.ANCHO/2, Pantalla.ALTO/2);
        batch.end();



    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}
}
