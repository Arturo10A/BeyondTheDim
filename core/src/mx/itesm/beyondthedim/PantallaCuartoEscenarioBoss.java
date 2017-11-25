package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * Created by Arturo on 03/11/17.
 */

public class PantallaCuartoEscenarioBoss extends  Pantalla implements INiveles{

    private final Juego juego;
    //Jett start
    private  Personaje personaje;
    //Jett Speed
    private int DX_PERSONAJE = 5;
    private int DY_PERSONAJE = 5;
    //Arreglo de balas
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private float timeBala;
    //BOSS
    private Boss boss;
    float Bosstimer = 0;
    double bossShot = 100;
    double bossShotFollow = 0.0;
    //Arreglo balas de Jefe
    private ArrayList<BulletBoss> bossBullets;
    //Escenario y Texturas
    private Texture texturaBtnGoBack;
    private Texture textureEscenario;
    private Stage escenaJuego;
    //Escena de Pausa
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;
    //Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    //Texto a mostrar
    private Texto texto;
    //Sonidos
    private Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));

    private Texture vidaIcono;
    private boolean texturasCargadas;

    public PantallaCuartoEscenarioBoss(Juego juego){
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.setPantallaJuego(this);
        juego.iniciarCuartoBossFinal(vista, camara);
        texturasCargadas = false;
    }

    @Override
    public void crearEnemigos() {

    }

    @Override
    public void ganar() {

    }

    @Override
    public void perder() {
        if (personaje.getLife() <= 0) {
            juego.getMusic().stop();
            juego.musicaCargada = false;
            juego.setScreen(new PantallaPerder(juego));
        }
    }

    @Override
    public void pausa() {
        juego.pausa(vista, batch, escenaJuego, camara);
    }

    @Override
    public void jugar(float delta) {
        juego.jugarBossFinal(delta, batch, escenaJuego, gunJoystick, boss);
    }

    @Override
    public void generarLimites() {
        if(juego.getLimites().isEmpty()){
            //MURO NORTE
            juego.addLimites(new Rectangle(0, ALTO - 120, ANCHO, 120));
            //MURO OESTE
            juego.addLimites(new Rectangle(0, 0, 120, ALTO));
            //MURO SUR
            juego.addLimites(new Rectangle(0, 0, ANCHO, 120));
            //MURO ESTE NORTE
            juego.addLimites(new Rectangle(1160, ALTO - 300, 120, 300));
            //MURO ESTE SUR
            juego.addLimites(new Rectangle(1160, 0, 120, 300));
            //PUERTA ESTE
            juego.addLimites(new Rectangle(1160, 300, 120, 120));
            //juego.limitesGenerados = true;
        }
    }

    @Override
    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/escenario_final.png");
        vidaIcono = new Texture("iconLife.png");
        texturasCargadas = true;
    }

    @Override
    public void cargarMusica() {

    }

    @Override
    public void crearEscena(){
        bullets = new ArrayList<Bullet>();
        shootTimer=0;
        escenaJuego = juego.getEscenaCuartoBossFinal();
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
        gunJoystick.setBounds(Pantalla.ANCHO - 200, 0, 200, 200);

        //Joystick movimiento
        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(0, 0, 200, 200);
        movJoystick.setColor(1, 1, 1, 0.7f);
        //Listener joystick movimiento
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                juego.conMovPadGrande(batch, pad, movJoystick);
            }
        });
        //****************************************Boton Pausa -> check variable and conflic agins problems*********************************************
        //Listener boton pausa
        juego.getBtnPausa().addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setEstadoJuego(EstadoJuego.PAUSADO);
            }
        });
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        escenaJuego.addActor(juego.getBtnPausa());
    }

    @Override
    public void show() {
        //Cargar escena
        System.out.println("Se hizo show");
        if(!texturasCargadas){
            cargarTexturas();
        }
        crearEscena();
        cargarMusica();
        generarLimites();
        if(juego.musicOn){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        camara.update();
        boss = new Boss(ANCHO/2,ALTO/2,100);
        juego.boss = boss;
        texto = new Texto();
        boss.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Gdx.input.setInputProcessor(escenaJuego);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        //HUD
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        juego.dibujarObjetos(batch, textureEscenario);
        batch.end();
        batch.begin();
        escenaJuego.draw();
        batch.end();
        //Dibujar escena del juego
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        //Jugar
        jugar(delta);
        //Ganar/Perder
        perder();
        ganar();
        //Pausa
        pausa();

        jugar(delta);
        ganar();

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