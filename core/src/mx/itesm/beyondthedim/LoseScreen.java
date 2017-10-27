package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class LoseScreen extends Pantalla{

    private final Juego juego;
    //Fondo
    private Texture background;
    private Stage escenaLoseScreen;
    //Textura botones
    private Texture backButtonInicio;
    private Texture backButtonReload;
    //Texto
    private Texto texto;

    public LoseScreen(Juego juego){
        this.juego = juego;
    }

    public void cargarTexturas(){
        background = new Texture("Stage/fondo_lose.png");
        backButtonInicio = new Texture("Botones/button_inicio.png");
        backButtonReload = new Texture("Botones/button_back_2.png");
    }

    public void crearEcena(){

        escenaLoseScreen = new Stage(vista);

        //FONDO PANTALLA
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(background));
        Image backImage = new Image(trdBack);
        backImage.setPosition(ANCHO/2-backImage.getWidth()/2,ALTO/2-backImage.getHeight()/2);

        //Boton inicio (menu principal)
        TextureRegionDrawable trdInicio = new TextureRegionDrawable(new TextureRegion(backButtonInicio));
        ImageButton btnInicio = new ImageButton(trdInicio);
        btnInicio.setPosition(ANCHO*0.15f,ALTO*0.1f);
        btnInicio.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***Inicio***");
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        //Boton inicio (menu principal)
        TextureRegionDrawable trdReload = new TextureRegionDrawable(new TextureRegion(backButtonReload));
        ImageButton btnGoBack = new ImageButton(trdReload);
        btnGoBack.setPosition(ANCHO*0.7f,ALTO*0.1f);
        btnGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***Reload***");
                juego.setScreen(new PantallaJuego(juego));
            }
        });

        //Agregar Actore -> siempre agregar el fondo primero
        escenaLoseScreen.addActor(backImage);
        escenaLoseScreen.addActor(btnInicio);
        escenaLoseScreen.addActor(btnGoBack);
    }

    @Override
    public void show() {

        cargarTexturas();
        crearEcena();

        Gdx.input.setInputProcessor(escenaLoseScreen);

        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        //
        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);
        //texto.mostrarMensaje(batch,"You Died", Pantalla.ANCHO/2, Pantalla.ALTO/2);
        //
        batch.begin();
        escenaLoseScreen.draw();
        batch.end();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}
}
