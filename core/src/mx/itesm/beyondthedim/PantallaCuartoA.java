package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class PantallaCuartoA  extends Pantalla implements Niveles{

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //Jett start
    private Personaje personaje;
    private Personaje obstacle;
    private ShapeRenderer shape;
    private ShapeRenderer shape2;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Constructores
    public PantallaCuartoA(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
    }

    @Override
    public void crearEscena() {
        //Escenario
        escenaJuego = new Stage(vista);
        shape = new ShapeRenderer();
        shape2 = new ShapeRenderer();
        //*******************************************************Joysticks*******************************************************
        //Texturas
        Skin skin = new Skin();
        skin.add("padFondo", new Texture("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento", new Texture("Joystick/joystick_movimiento.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padFondo");
        estilo.knob = skin.getDrawable("padMovimiento");
        //Joystick pistola
        gunJoystick = new Touchpad(20, estilo);
        gunJoystick.setBounds(ANCHO - 200, 0, 200, 200);

        //Joystick movimiento
        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(0, 0, 200, 200);
        //Listener joystick movimiento
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                juego.controlJoystickMovimiento(batch, pad, movJoystick, obstacle);
            }
        });
        //
        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1, 1, 1, 0.7f);
        //*******************************************************Boton Pausa -> check variable and conflic agins problems*******************************************************
        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(ANCHO - btnPausa.getWidth() - 5, ALTO - btnPausa.getHeight() - 5);
        escenaJuego.addActor(btnPausa);
        //Listener boton pausa
        btnPausa.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Se pausa el juego
                //estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                juego.setEstadoJuego(EstadoJuego.PAUSADO);
            }

        });
    }

    //********************Cargar*******************
    @Override
    public void cargarTexturas() {
        texturaBtnPausa = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.jpg");
        textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.jpg");
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
    }

    @Override
    public void cargarMusica(){
        juego.setMusic(Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3")));
        juego.getMusic().setLooping(true);
    }

    @Override
    public void show() {
        //Cargar escena
        cargarTexturas();
        crearEscena();
        cargarMusica();
        if(juego.musicOn){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        obstacle = new Personaje(ANCHO / 2+100, ALTO / 2, 1000);
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        Gdx.input.setInputProcessor(escenaJuego);
    }
    //***********************************************Metodos de render**********************************************
    //
    private void logicaMovimiento(float delta) {
        Rectangle rp = personaje.getSprite().getBoundingRectangle();
        rp.setX(personaje.getPositionX()+17);
        rp.setY(personaje.getPositionY());
        rp.setWidth(30);
        rp.setHeight(20);

        //Boundings
        Rectangle ro = obstacle.getSprite().getBoundingRectangle();
        Rectangle muroN = new Rectangle(0,ALTO-120,ANCHO,120);
        Rectangle muroO = new Rectangle(0,0,120,ALTO);
        Rectangle muroS = new Rectangle(0,0,ANCHO,120);
        Rectangle muroEN = new Rectangle(1160,ALTO-300,120,300);
        Rectangle muroES = new Rectangle(1160,0,120,300);
        Rectangle puertaE = new Rectangle(1160,300,120,120);

        shape.setProjectionMatrix(camara.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);
        shape.rect(rp.getX(),rp.getY(),rp.getWidth(),rp.getHeight());

        shape2.setProjectionMatrix(camara.combined);
        shape2.begin(ShapeRenderer.ShapeType.Line);
        shape2.setColor(Color.BLUE);
        shape2.rect(puertaE.getX(),puertaE.getY(),puertaE.getWidth(),puertaE.getHeight());


        Vector2 v = new Vector2(movJoystick.getKnobPercentX(), movJoystick.getKnobPercentY());
        float ang = v.angle();
        double angle = ang*Math.PI/180.0;
        //*******************************************Logica enemigos*******************************************
        if (juego.getEnemy_list().isEmpty()){
            textureEscenario = textureEscenarioAbierto;
            puertaE.setSize(10);
            shape2.rect(puertaE.getX(),puertaE.getY(),puertaE.getWidth(),puertaE.getHeight());
            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego));
            }
        }
        //*******************************************************Logica enemigos*******************************************************{
        if(movJoystick.getKnobPercentX()!=0.000 && movJoystick.getKnobPercentY()!=0.000) {
            rp.setX(rp.getX()+ (float)(Math.cos(angle)*20));
            rp.setY(rp.getY()+ (float)(Math.sin(angle)*20));

            if((!rp.overlaps(muroN))&&(!rp.overlaps(ro))&&(!rp.overlaps(muroO))&&(!rp.overlaps(muroS))&(!rp.overlaps(muroEN))&&(!rp.overlaps(muroES))&&(!rp.overlaps(puertaE))   ){
                personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
            }
        }

        shape2.end();
        shape.end();
    }
    //

    @Override
    public void render(float delta) {
        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);
        //HUD
        batch.setProjectionMatrix(camara.combined);
        escenaJuego.draw();
        //Dibujar Objetos
        batch.begin();
        juego.dibujarObjetos(batch, textureEscenario, obstacle);
        batch.end();
        //Dibujar escena del juego
        batch.begin();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
        //Jugar
        jugar(delta);
        //Ganar/Perder
        perder();
        ganar();
        //Pausa
        pausa();
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


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
    }

    @Override
    public void ganar() {
        if (juego.getEnemy_list().isEmpty()){
            textureEscenario = textureEscenarioAbierto;

            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego));
            }
        }
    }

    @Override
    public void perder() {
        if (personaje.getLife() <= 0) {
            juego.setScreen(new LoseScreen(juego));
        }
    }

    @Override
    public void pausa() {
        if (juego.getEstadoJuego()==EstadoJuego.PAUSADO) {
            // Activar escenaPausa y pasarle el control
            if (juego.escenaPausa==null) {
                juego.escenaPausa = new EscenaPausa(vista, batch, juego, escenaJuego);
            }
            juego.escenaPausa.draw();
            Gdx.input.setInputProcessor(juego.escenaPausa);
        }
    }

    @Override
    public void jugar(float delta) {
        if(juego.getEstadoJuego()==EstadoJuego.JUGANDO) {
            batch.begin();
            logicaMovimiento(delta);
            batch.end();
            juego.logicaEnemigo(delta);
            //***************Balas***************
            juego.logicaDisparo(delta, gunJoystick, batch);
            //***************Colision Bala/Enemigo***************
            juego.sistemaColisionesBala();
        }
    }

    @Override
    public void generarLimites() {

    }
}
