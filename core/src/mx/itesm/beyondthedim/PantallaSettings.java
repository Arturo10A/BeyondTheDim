package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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


//Hola entre en ti

class PantallaSettings extends Pantalla {

    private final Juego juego;
    //Fondo de pantalla
    private Texture textureBackground;
    private Stage escenaSettings;
    //Textura botones
    private Texture textureGoBack;
    private Texture textureButtonMusicOn;
    private Texture textureButtonMusicOff;
    private Texture textureButtonMusic;
    //
    private SpriteBatch batch;
    private Music music;
    private boolean musicOn = true;

    public PantallaSettings(Juego juego, Music music) {
        this.juego = juego;
        this.music = music;
    }

    public void cargarTextura(){

        textureBackground = new Texture("Stage/fondo_estrellas.png");
        textureGoBack = new Texture("Botones/button_back_2.png");
        textureButtonMusicOff = new Texture("Botones/settings_button_off.png");
        textureButtonMusicOn = new Texture("Botones/settings_button_on.png");
        textureButtonMusic = new Texture("Botones/button_music.png");
        //textureButtonMusic = new Texture("Botones/settings_button_on.png");

    }

    public void crearEcena(){

        escenaSettings = new Stage(vista);

        //FONDO PANTALLA
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(textureBackground));
        Image backImage = new Image(trdBack);
        backImage.setPosition(ANCHO/2-backImage.getWidth()/2,ALTO/2-backImage.getHeight()/2);

        //Go BACK
        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(textureGoBack));
        ImageButton btnGoBack = new ImageButton(trdGoBack);
        btnGoBack.setPosition(ANCHO*0.85f,(ALTO*0.80f)-btnGoBack.getHeight()*0.2f);

        btnGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego, music));
            }
        });

        //Boton música
        TextureRegionDrawable trdMusic = new TextureRegionDrawable(new TextureRegion(textureButtonMusic));
        ImageButton btnMusic = new ImageButton(trdMusic);
        btnMusic.setPosition(ANCHO/2-btnMusic.getWidth()/2,ALTO/2-btnMusic.getHeight()/2);

        btnMusic.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(musicOn){
                    music.pause();
                    textureButtonMusic = textureButtonMusicOff;
                    musicOn = false;
                }else{
                    textureButtonMusic = textureButtonMusicOn;
                    music.play();
                    musicOn = true;
                }
            }
        });

        //Agregar Actore -> siempre agregar el fondo primero
        escenaSettings.addActor(backImage);
        escenaSettings.addActor(btnGoBack);
        escenaSettings.addActor(btnMusic);


    }

    @Override
    public void show() {
        cargarTextura();
        crearEcena();

        Gdx.input.setInputProcessor(escenaSettings);

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        escenaSettings.draw();
        batch.draw(textureButtonMusic, ANCHO/2-textureButtonMusic.getWidth()/2,ALTO/2-textureButtonMusic.getHeight()/2);
        batch.end();

        batch.begin();
        batch.draw(textureButtonMusic, ANCHO/2-textureButtonMusic.getWidth()/2,ALTO/2-textureButtonMusic.getHeight()/2);
        batch.end();

        if(musicOn){
            textureButtonMusic = textureButtonMusicOn;
        }else{
            textureButtonMusic = textureButtonMusicOff;
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
