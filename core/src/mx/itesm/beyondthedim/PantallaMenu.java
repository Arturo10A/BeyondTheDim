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
 * Created by Arturo on 25/08/17.
 */

class PantallaMenu extends Pantalla {

    private final Juego juego;

    //Contenedor de los botones
    private Stage escenaMenu;

    //Texturas de los botones
    private Texture texturaBtnJugar;
    private Texture texturaBtnAyuda;
    private Texture texturaBtnSettings;
    private Texture texturaBtnInstructions;
    private Texto texto;

    private Texture fondoPantalla;

    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        cargarTexturas(); //Carga imagenes
        crearEcenaMenu(); //Crea la escena
        Gdx.input.setInputProcessor(escenaMenu);

        batch = new SpriteBatch();
        texto = new Texto();

    }

    private void cargarTexturas() {
        texturaBtnJugar = new Texture("Botones/button_play.png");
        texturaBtnAyuda = new Texture("Botones/button_about-us.png");
        texturaBtnSettings = new Texture("Botones/button_settings.png");
        texturaBtnInstructions = new Texture("Botones/button_instructions.png");
        fondoPantalla = new Texture("MenuFondo.png");
    }

    private void crearEcenaMenu() {

        escenaMenu = new Stage(vista);
        //Fondo de pantalla
        TextureRegionDrawable backWall = new TextureRegionDrawable(new TextureRegion(fondoPantalla));
        Image back = new Image(backWall);
        back.setPosition(ANCHO/2-back.getWidth()/2,ALTO/2-back.getHeight()/2);
        escenaMenu.addActor(back);

        //Boton jugar
        TextureRegionDrawable trdPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnPlay = new ImageButton(trdPlay);
        btnPlay.setPosition(ANCHO/2-(btnPlay.getWidth()/2),ALTO*0.23f);
        btnPlay.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***PANTALLA JUEGO***");
                juego.setScreen(new PantallaJuego(juego));
            }
        });
        escenaMenu.addActor(btnPlay);

        //Boton Settings
        TextureRegionDrawable trdSettings = new TextureRegionDrawable(new TextureRegion(texturaBtnSettings));
        ImageButton btnSettings = new ImageButton(trdSettings);
        btnSettings.setPosition(ANCHO-(ANCHO*0.114f),ALTO*0.423f);

        btnSettings.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***AYUDA Settings***");
                juego.setScreen(new PantallaSettings(juego));
            }
        });
        escenaMenu.addActor(btnSettings);

        //Boton instruciones
        TextureRegionDrawable trdIntruction = new TextureRegionDrawable(new TextureRegion(texturaBtnInstructions));
        ImageButton btnIntruction = new ImageButton(trdIntruction);
        btnIntruction.setPosition(ANCHO/2-btnIntruction.getWidth()/2,ALTO*0.15f);

        btnIntruction.addListener( new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***AYUDA Intruciones***");
                juego.setScreen(new PantallaInstruciones(juego));
            }


        });
        escenaMenu.addActor(btnIntruction);

        //Boton aboutUS
        TextureRegionDrawable trdAboutUs = new TextureRegionDrawable(new TextureRegion(texturaBtnAyuda));
        ImageButton btnAboutUs = new ImageButton(trdAboutUs);
        btnAboutUs.setPosition(ANCHO*0.039f,ALTO*0.423f);

        btnAboutUs.addListener( new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***AYUDA PANTALLA***");
                juego.setScreen(new PantallaAyuda(juego));
            }


        });
        escenaMenu.addActor(btnAboutUs);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(1.0f,1.0f,1.0f);
        batch.setProjectionMatrix(camara.combined);



        batch.begin();
        escenaMenu.draw();
        batch.end();

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
