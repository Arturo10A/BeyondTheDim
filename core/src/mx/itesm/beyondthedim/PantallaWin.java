package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Arturo on 27/11/17.
 */

public class PantallaWin extends Pantalla {

    private final Juego juego;
    private Texture texturafondo;
    private Stage escena;

    //Tiempo
    private float tiempo = 0;

    public PantallaWin(Juego juego) {
        this.juego =  juego;
    }


    private void cargarTexturas(){
        texturafondo = new Texture("Stage/wine.jpg");
    }

    private void crearEscena(){
        escena = new Stage(vista);

        TextureRegionDrawable backWall = new TextureRegionDrawable(new TextureRegion(texturafondo));
        com.badlogic.gdx.scenes.scene2d.ui.Image back = new com.badlogic.gdx.scenes.scene2d.ui.Image (backWall);
        back.setPosition(ANCHO/2-back.getWidth()/2,ALTO/2-back.getHeight()/2);

        escena.addActor(back);


    }


    @Override
    public void show() {
        cargarTexturas();
        crearEscena();

    }

    @Override
    public void render(float delta) {

        borrarPantalla(1,1,1);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        escena.draw();
        batch.end();

        tiempo += Gdx.graphics.getDeltaTime();

        if (tiempo >= 10){
            juego.setScreen(new PantallaAboutUs(juego));
        }

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
