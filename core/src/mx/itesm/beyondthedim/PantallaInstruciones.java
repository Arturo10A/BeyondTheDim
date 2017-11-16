package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

class PantallaInstruciones extends Pantalla  {

    private final Juego juego;

    //Texturas de botones
    private Texture texturaGoBackButton;

    private Array<Sprite> fondos;
    private Sprite imagen;
    private float tiempo;
    private int index;

    //Textura Fondo de pantalla
    private Texture texturaBack;
    //
    private Music music;

    private Stage escenaInstruciones;


    public PantallaInstruciones(Juego juego, Music music) {
        this.juego = juego;
        this.music = music;
    }

    @Override
    public void show() {

        cargarTexturas();
        crearEscena();

        Gdx.input.setInputProcessor(escenaInstruciones);

        fondos = new Array<Sprite>();
        cargarFondos();

        tiempo = 0;
        index = 0;
    }

    public void cargarTexturas(){
        texturaGoBackButton = new Texture("Botones/button_back_2.png");
        //texturaBack = new Texture("Stage/fondo_instruciones.png");
    }


    public void crearEscena(){

        escenaInstruciones = new Stage(vista);

        /*
        TextureRegionDrawable trdBack = new TextureRegionDrawable(new TextureRegion(texturaBack));
        Image backImage = new Image(trdBack);
        backImage.setPosition(ANCHO/2-backImage.getWidth()/2,ALTO/2-backImage.getHeight()/2);*/


        //Boton go Back
        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaGoBackButton));
        ImageButton buttonGoBack = new ImageButton(trdGoBack);
        buttonGoBack.setPosition(ANCHO*0.85f,(ALTO*0.80f)-buttonGoBack.getHeight()*0.2f);

        buttonGoBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego, music));
            }
        });


        //Agregar Actores a la pantalla
        //escenaInstruciones.addActor(backImage);
        escenaInstruciones.addActor(buttonGoBack);
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        escenaInstruciones.draw();
        batch.end();

        batch.begin();
        fondos.get(index).draw(batch);
        batch.end();
        tiempo+= Gdx.graphics.getDeltaTime();
        //Frame index
        if(tiempo>=0.1f){
            index++;
            tiempo=0;
        }
        //Restart frames
        if(index== fondos.size){
            index=0;
        }

    }

    private void cargarFondos(){

        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_02.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_03.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_04.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_05.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_06.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_07.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_08.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_09.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_10.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_11.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_12.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_13.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_14.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_15.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_16.jpg"));
        fondos.add(imagen);
        imagen = new Sprite(new Texture("Stage/Instrucciones/instrucciones_17.jpg"));
        fondos.add(imagen);


        for(Sprite imagen: fondos){
            imagen.setPosition(0,0);
            imagen.setSize(ANCHO,ALTO);
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
