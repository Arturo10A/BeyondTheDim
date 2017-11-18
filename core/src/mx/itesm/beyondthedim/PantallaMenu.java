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

    //Nave
    private Nave nave;
    private float anchoNave;
    private float altoNave;
    float posAltoNave = 0.20f;

    private Texture fondoPantalla;
    private boolean playAnimacionNave = false;

    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }
    private void cargarNave() {
        nave = new Nave(ANCHO/2-280/2,ALTO*posAltoNave);
        nave.setEstadoMovimiento(Objeto.EstadoMovimiento.ACTIVO);
    }

    private void cargarMusica(){
        //music = Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-slowmotion.mp3"));
        juego.setMusic(Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-slowmotion.mp3")));
        //music.setLooping(true);
        //music.playAnimacionNave();
    }

    private void cargarTexturas() {
        texturaBtnJugar = new Texture("Botones/button_play.png");
        texturaBtnAyuda = new Texture("Botones/button_about-us.png");
        texturaBtnSettings = new Texture("Botones/button_settings.png");
        //texturaBtnInstructions = new Texture("Botones/button_instructions.png");
        fondoPantalla = new Texture("Stage/MenuFondo.jpg");
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
        btnPlay.setPosition(ANCHO/2-(btnPlay.getWidth()/2),ALTO*0.20f);
        btnPlay.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.getMusic().stop();
                Gdx.app.log("clicked","***PANTALLA JUEGO***");
                playAnimacionNave = true;
                nave.setEstadoMovimiento(Objeto.EstadoMovimiento.PROPULSOR);
                juego.setEstadoJuego(EstadoJuego.JUGANDO);
                juego.iniciarJuego(ANCHO,ALTO);

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
        //Boton aboutUS
        TextureRegionDrawable trdAboutUs = new TextureRegionDrawable(new TextureRegion(texturaBtnAyuda));
        ImageButton btnAboutUs = new ImageButton(trdAboutUs);
        btnAboutUs.setPosition(ANCHO*0.039f,ALTO*0.423f);

        btnAboutUs.addListener( new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***AYUDA PANTALLA***");
                juego.setScreen(new PantallaAboutUs(juego));
            }


        });
        escenaMenu.addActor(btnAboutUs);

    }

    @Override
    public void show() {

        cargarTexturas(); //Carga imagenes
        crearEcenaMenu(); //Crea la escena
        cargarNave();
        if(!juego.musicaCargada) {
            cargarMusica();
        }
        if(juego.musicOn){
            juego.getMusic().setLooping(true);
            juego.getMusic().play();
        }
        Gdx.input.setInputProcessor(escenaMenu);

        batch = new SpriteBatch();
        texto = new Texto();
    }

    @Override
    public void render(float delta) {
        borrarPantalla(1.0f,1.0f,1.0f);
        batch.setProjectionMatrix(camara.combined);
        escenaMenu.draw();
        if(!playAnimacionNave) {
            altoNave = 0;
            altoNave = 0;
            batch.begin();
            nave.dibujar(batch, Gdx.graphics.getDeltaTime(), anchoNave, altoNave);
            batch.end();
        }else{
            altoNave += 1f;
            anchoNave += 1f;
            posAltoNave += (altoNave*altoNave)*0.0001f;
            batch.begin();
            nave.setPosition(ANCHO/2-nave.ANCHO/2,ALTO*posAltoNave);
            nave.dibujar(batch, Gdx.graphics.getDeltaTime(), anchoNave, altoNave);
            batch.end();
        }
        if(nave.ANCHO < 0 || nave.ALTO < 0){
            juego.getMusic().stop();
            juego.setScreen(new PantallaTutorial(juego));
            this.dispose();
        }
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
