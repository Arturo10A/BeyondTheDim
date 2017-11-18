package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
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

public abstract class Pantalla implements Screen {

    //Atributos disponible en todas las clases del proyecto
    public static  final float ANCHO = 1280;
    public static  final float ALTO = 720;
    //Atributos disponibles en las subclases
    //Todas las pantallas tiene una cámara y la vista
    protected OrthographicCamera camara;
    protected Viewport vista;
    //Todas las pantallas sibujan
    protected SpriteBatch batch;


    public Pantalla(){
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
        batch = new SpriteBatch();
    }

    protected void borrarPantalla(){
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    protected  void borrarPantalla(float r, float g, float b){
        Gdx.gl.glClearColor(r,g,b,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    //Borra la pantalla con el color RGB(r,g,b)
    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
        camara.viewportHeight=ALTO;
        camara.viewportWidth=ANCHO;
        camara.update();
    }


    @Override
    public void hide() {
        dispose();
        ///Libera los recursos asignados por cada pantalla
    }

    public void update(float delta){

    }

    public static class EscenaPausa extends Stage{

        public EscenaPausa(Viewport vista, SpriteBatch batch, final Juego juego, final Stage escenaJuego) {
            super(vista,batch);
            Pixmap pixmap = new Pixmap((int) (ANCHO), (int) (ALTO), Pixmap.Format.RGBA8888);
            pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.4f );
            pixmap.fillRectangle(0, 0,pixmap.getWidth(),pixmap.getHeight());
            Texture texturaRectangulo = new Texture(pixmap);
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(0, 0);
            this.addActor(imgRectangulo);

            // Salir
            Texture texturaBtnSalir = new Texture("Botones/button_inicio.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO/2-btnSalir.getWidth()/2, ALTO*0.2f);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    juego.getMusic().stop();
                    juego.musicaCargada = false;
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReintentar = new Texture("Botones/button_back_2.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReintentar));
            ImageButton btnReintentar = new ImageButton(trdReintentar);
            btnReintentar.setPosition(ANCHO/2-btnReintentar.getWidth()/2, ALTO*0.5f);
            btnReintentar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    juego.setEstadoJuego(EstadoJuego.JUGANDO);
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(escenaJuego);
                }
            });
            this.addActor(btnReintentar);
        }
    }




}
