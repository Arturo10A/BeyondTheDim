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

class PantallaAboutUs extends Pantalla {

    private final Juego juego;
    private Texture ecenario;  //Imagen del ecenario
    private Texture texturaFondoPantallaAyudad; //Fondo del ecenario
    private Texto texto; // Variable que asignara todos los textos que deasemos mostrar en la pantalla
    private Stage ecenaAyuda; /* Variable encargada de dibujar todo nuestro escenario */
    private Texture texturaBtnGoBack; //Boton de regresp



    public PantallaAboutUs(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        cargarTexturas();
        crearEcenaAyuda();

        Gdx.input.setInputProcessor(ecenaAyuda);

        //Boto que deve de ser sustituido
        batch = new SpriteBatch();
        texto = new Texto();

    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_back_2.png");
        //Fondo para este ecenario
        texturaFondoPantallaAyudad = new Texture("Stage/about_pantalla.png");
    }

    public void crearEcenaAyuda(){

        ecenaAyuda = new Stage(vista);

        //Boton goBack
        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaBtnGoBack));
        ImageButton btnGoBack = new ImageButton(trdGoBack);

        btnGoBack.setPosition(ANCHO*0.85f,(ALTO*0.80f)-btnGoBack.getHeight()*0.2f);
        btnGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        //Fondo de pantalla
        TextureRegionDrawable backWall = new TextureRegionDrawable(new TextureRegion(texturaFondoPantallaAyudad));
        Image back = new Image(backWall);
        back.setPosition(ANCHO/2-back.getWidth()/2,ALTO/2-back.getHeight()/2);

        //Add actors
        ecenaAyuda.addActor(back);
        ecenaAyuda.addActor(btnGoBack);
    }



    @Override
    public void render(float delta) {
        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        //Dibujamos los elementos graficos
        batch.begin();
        ecenaAyuda.draw();
        batch.end();

        //Dibujamos los elementos tipo texto de badLogic
        batch.begin();

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
