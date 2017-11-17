package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

class PantallaAboutUs extends Pantalla {

    private final Juego juego;

    //Texturas
    private Texture ecenario;  //Imagen del ecenario
    private Texture texturaFondoPantallaAyudad; //Fondo del ecenario
    private Texture texturaBotonCasco;
    private Texture texturaRodrigo;
    private Texture texturaMonserrat;
    private Texture texturaArturo;
    private Texture texturaJorge;
    private Texto texto; // Variable que asignara todos los textos que deasemos mostrar en la pantalla
    //Escenarios
    private Stage escenaAyuda; /* Variable encargada de dibujar todo nuestro escenario */
    private EscenaInfo escenaInfoRo;
    private EscenaInfo escenaInfoAr;
    private EscenaInfo escenaInfoMo;
    private EscenaInfo escenaInfoJo;
    private boolean dibujarEscenaInfo=false;
    private Texture texturaBtnGoBack; //Boton de regresp
    //private Music music;
    private char turno;



    public PantallaAboutUs(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        cargarTexturas();
        crearEcenaAyuda();

        Gdx.input.setInputProcessor(escenaAyuda);

        //Boto que deve de ser sustituido
        batch = new SpriteBatch();
        texto = new Texto();

    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_back_2.png");
        texturaBotonCasco = new Texture("Botones/button_casco_aboutUs.png");
        //Fondo para este ecenario
        texturaFondoPantallaAyudad = new Texture("Stage/about_pantalla.jpg");
        //Registros
        texturaRodrigo = new Texture("Stage/AboutUs/registro_rodrigo.png");
        texturaMonserrat = new Texture("Stage/AboutUs/registro_monse.png");
        texturaArturo = new Texture("Stage/AboutUs/registro_arturo.png");
        texturaJorge = new Texture("Stage/AboutUs/registro_alexis.png");
    }

    public void crearEcenaAyuda(){

        escenaAyuda = new Stage(vista);

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

        //Textura botones casco
        TextureRegionDrawable trdCasco = new TextureRegionDrawable(new TextureRegion(texturaBotonCasco));

        //Boton Rodrigo
        ImageButton btnCascoRodrigo = new ImageButton(trdCasco);

        btnCascoRodrigo.setPosition(ANCHO*0.12f,ALTO*0.57f);
        btnCascoRodrigo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Rodrigo");
                turno = 'r';
                if (escenaInfoRo==null) {
                    escenaInfoRo = new PantallaAboutUs.EscenaInfo(vista, batch, texturaRodrigo);
                }
                Gdx.input.setInputProcessor(escenaInfoRo);
                dibujarEscenaInfo = true;
            }
        });

        //Boton Monserrat
        ImageButton btnCascoMonse = new ImageButton(trdCasco);
        btnCascoMonse.setPosition(ANCHO*0.31f,ALTO*0.20f);
        btnCascoMonse.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Monse");
                turno = 'm';
                if (escenaInfoMo==null) {
                    escenaInfoMo = new PantallaAboutUs.EscenaInfo(vista, batch, texturaMonserrat);
                }
                Gdx.input.setInputProcessor(escenaInfoMo);
                dibujarEscenaInfo = true;
            }
        });


        //Boton Jorge
        ImageButton btnCascoJorge = new ImageButton(trdCasco);
        btnCascoJorge.setPosition(ANCHO*0.54f,ALTO*0.57f);
        btnCascoJorge.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Jorge");
                turno = 'j';
                if (escenaInfoJo==null) {
                    escenaInfoJo = new PantallaAboutUs.EscenaInfo(vista, batch, texturaJorge);
                }
                Gdx.input.setInputProcessor(escenaInfoJo);
                dibujarEscenaInfo = true;
            }
        });

        //Boton Arturo
        ImageButton btnCascoArturo = new ImageButton(trdCasco);
        btnCascoArturo.setPosition(ANCHO*0.73f,ALTO*0.20f);
        btnCascoArturo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Arturo");
                turno = 'a';
                if (escenaInfoAr==null) {
                    escenaInfoAr = new PantallaAboutUs.EscenaInfo(vista, batch, texturaArturo);
                }
                Gdx.input.setInputProcessor(escenaInfoAr);
                dibujarEscenaInfo = true;
            }
        });
        
        //Fondo de pantalla
        TextureRegionDrawable backWall = new TextureRegionDrawable(new TextureRegion(texturaFondoPantallaAyudad));
        Image back = new Image(backWall);
        back.setPosition(ANCHO/2-back.getWidth()/2,ALTO/2-back.getHeight()/2);

        //Add actors
        escenaAyuda.addActor(back);
        escenaAyuda.addActor(btnGoBack);
        escenaAyuda.addActor(btnCascoRodrigo);
        escenaAyuda.addActor(btnCascoMonse);
        escenaAyuda.addActor(btnCascoArturo);
        escenaAyuda.addActor(btnCascoJorge);
    }



    @Override
    public void render(float delta) {
        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        //Dibujamos los elementos graficos
        batch.begin();
        escenaAyuda.draw();
        batch.end();

        //Registros equipo
        if(dibujarEscenaInfo){
            switch(turno){
                case 'r':
                    escenaInfoRo.draw();
                    break;
                case 'a':
                    escenaInfoAr.draw();
                    break;
                case 'j':
                    escenaInfoJo.draw();
                    break;
                case 'm':
                    escenaInfoMo.draw();
                    break;
            }
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
        texturaFondoPantallaAyudad.dispose();
        texturaBotonCasco.dispose();
        texturaRodrigo.dispose();
        texturaMonserrat.dispose();
        texturaArturo.dispose();
        texturaJorge.dispose();
    }

    private class EscenaInfo extends Stage {

        public EscenaInfo(Viewport vista, SpriteBatch batch, Texture texture) {
            super(vista,batch);
            Pixmap pixmap = new Pixmap((int) (ANCHO), (int) (ALTO), Pixmap.Format.RGBA8888);
            pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.4f );
            pixmap.fillRectangle(0, 0,pixmap.getWidth(),pixmap.getHeight());
            Texture texturaRectangulo = new Texture(pixmap);
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(0, 0);
            this.addActor(imgRectangulo);

            //Registro
            Image textureRegistro = new Image(texture);
            textureRegistro.setPosition(0,0);
            this.addActor(textureRegistro);

            // Regresar
            Texture texturabtnBack = new Texture("Botones/button_back_2.png");
            TextureRegionDrawable trdBackInfo = new TextureRegionDrawable(new TextureRegion(texturabtnBack));
            ImageButton btnBackInfo = new ImageButton(trdBackInfo);
            btnBackInfo.setPosition(ANCHO*0.85f,(ALTO*0.80f)-btnBackInfo.getHeight()*0.2f);
            btnBackInfo.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.input.setInputProcessor(escenaAyuda);
                    dibujarEscenaInfo = false;
                }

            });
            this.addActor(btnBackInfo);
        }
    }

}
