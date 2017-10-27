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


//Hola entre en ti

class PantallaSettings extends Pantalla {

    private final Juego juego;
    //Fondo de pantalla
    private Texture textureBackground;
    private Stage escenaSettings;
    //Textura botones
    private Texture textureGoBack;
    //
    private SpriteBatch batch;

    public void cargarTextura(){

        textureBackground = new Texture("fondoSettings.png");
        textureGoBack = new Texture("Botones/button_back_2.png");

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
        btnGoBack.setPosition(ANCHO*0.9f,(ALTO*0.9f)-btnGoBack.getHeight()*0.5f);

        btnGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego));
            }
        });


        //Agregar Actore -> siempre agregar el fondo primero
        escenaSettings.addActor(backImage);
        escenaSettings.addActor(btnGoBack);


    }

    public PantallaSettings(Juego juego) {
        this.juego = juego;
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
