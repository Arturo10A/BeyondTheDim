package mx.itesm.beyondthedim;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */



class PantallaCargando extends Pantalla {

    private final Juego juego;
    private Texture texturafondo;
    private Stage escena;

    //Tiempo
    private float tiempo = 0;

    public PantallaCargando(Juego juego) {
        this.juego =  juego;
    }


    private void cargarTexturas(){
        texturafondo = new Texture("Logo/xd.png");
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

        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        escena.draw();
        batch.end();

        tiempo += Gdx.graphics.getDeltaTime();

        if (tiempo >= 1){
            juego.setScreen(new PantallaMenu(juego));
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
