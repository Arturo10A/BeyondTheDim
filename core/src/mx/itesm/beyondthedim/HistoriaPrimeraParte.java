package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by Arturo on 03/11/17.
 */

class HistoriaPrimeraParte extends Pantalla {

    private final Juego juego;
    private Stage escena;

    private Texture fondo;
    private Texture loading;

    private float time = 0;


    //Music musica = Gdx.audio.newMusic(path);

    public HistoriaPrimeraParte(Juego juego){this.juego = juego;}

    private void cargarTexturas(){

        //Asignar textura correspondiente

        //fondo = new Texture();
    }

    private  void crearEscena(){
        escena = new Stage(vista);

        TextureRegionDrawable fondoTRD = new TextureRegionDrawable( new TextureRegion(fondo) );
        //Image fondoPantalla = new Image(fondoTRD);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        borrarPantalla(1.0f,1.0f,1.0f);
        batch.setProjectionMatrix(camara.combined);

        time += Gdx.graphics.getDeltaTime();

        if (time > 10){
            /*Carga siguente nivel*/
        }

        batch.begin();
        //Reproducir Musica
        escena.draw();
        batch.end();

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
