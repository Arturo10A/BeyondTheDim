package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */



class PantallaCargando implements Screen {

    private final Juego juego;
    private Texture texturafondo;
    private SpriteBatch batch;

    //Camara
    private OrthographicCamera camara;
    private Viewport vista;

    //Tiempo
    private float tiempo = 0;

    public PantallaCargando(Juego juego) {
        this.juego =  juego;
    }

    @Override
    public void show() {
        inicializarCamara();

        texturafondo = new Texture("Logo/logo.png");
        batch = new SpriteBatch();


    }

    private void inicializarCamara() {
        //Camara
        camara = new OrthographicCamera(1280,720);
        camara.position.set((1380-825)/2 ,(720-331)/1,0);
        camara.update();
        vista = new FitViewport(640,360, camara);
    }

    @Override
    public void render(float delta) {

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturafondo,0,0);
        batch.end();

        //Tiempo

        tiempo += Gdx.graphics.getDeltaTime();

        if (tiempo >= 1){

            juego.setScreen(new PantallaMenu(juego));

        }

    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

        vista.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
