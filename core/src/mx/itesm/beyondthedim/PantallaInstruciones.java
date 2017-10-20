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
 * Created by Arturo on 15/09/17.
 */

class PantallaInstruciones extends Pantalla  {

    private final Juego juego;

    //Texturas de botones
    private Texture texturaGoBackButton;

    //Textura Fondo de pantalla
    private Texture texturaBack;


    private Stage escenaInstruciones;





    public PantallaInstruciones(Juego juego) {
        this.juego = juego;

    }

    @Override
    public void show() {

        cargarTexturas();
        crearEscena();

        Gdx.input.setInputProcessor(escenaInstruciones);


        batch = new SpriteBatch();


    }

    public void cargarTexturas(){
        texturaGoBackButton = new Texture("button_back.png");
        texturaBack = new Texture("fondo_instruciones.png");
    }


    public void crearEscena(){

        escenaInstruciones = new Stage(vista);

        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(texturaBack));
        Image backImage = new Image(trdBack);
        backImage.setPosition(ANCHO/2-backImage.getWidth()/2,ALTO/2-backImage.getHeight()/2);


        //Boton go Back

        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaGoBackButton));
        ImageButton buttonGoBack = new ImageButton(trdGoBack);
        buttonGoBack.setPosition(ANCHO-60-buttonGoBack.getWidth(),ALTO-buttonGoBack.getHeight()*2);

        buttonGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego));
            }
        });


        //Agregar Actores a la pantalla
        escenaInstruciones.addActor(backImage);
        escenaInstruciones.addActor(buttonGoBack);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        escenaInstruciones.draw();
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
