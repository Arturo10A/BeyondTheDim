package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.awt.Shape;
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

public class PantallaTutorial  extends Pantalla implements INiveles {

    //Arreglo de balas
    private float timeBala;
    //Escenario y Texturas
    private Texture texturaBtnSkip;
    //Escena de Pausa
    //Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    //Texto a mostrar
    private Texto texto;
    //Sonidos



    //Timer dialogos
    private int timerDialogo;
    //**************************************************************

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;


    private ArrayList<ObjetoEscenario> objetos = new ArrayList<ObjetoEscenario>();

    //Cuadros de dialogo
    private Texture cuadro1;
    private Texture cuadro2;
    private Texture cuadro3;
    private Texture cuadro4;
    private Texture cuadro5;
    private Texture cuadro6;

    //Icono de vida
    private Texture vidaIcono;

    private AssetManager manager;
    //Constructores
    public PantallaTutorial(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoTutorial(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoTutorial();
        juego.setPantallaJuego(this);
        manager = juego.getAssetManager();
    }

    @Override
    public void crearEscena() {
        //Escenario
        escenaJuego = juego.getEscenaCuartoTutorial();
        //*******************************************************Joysticks*******************************************************
        //Texturas
        Skin skin = new Skin();
        skin.add("padFondo",manager.get("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento", manager.get("Joystick/joystick_movimiento.png"));

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

        TextureRegionDrawable trdSkip = new TextureRegionDrawable(new TextureRegion(texturaBtnSkip));
        ImageButton btnSkip = new ImageButton(trdSkip);
        btnSkip.setPosition(ANCHO-btnSkip.getWidth(),ALTO-btnSkip.getHeight());
        btnSkip.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event,x,y);
                juego.setScreen(new PantallaCargando(juego, Pantallas.CUARTO_A));
            }
        });
        escenaJuego.addActor(btnSkip);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
    }


    //********************Cargar*******************
    @Override
    public void cargarTexturas() {
        texturaBtnSkip =  manager.get("Botones/forward.png");
        textureEscenario =  manager.get("Stage/tutorial.jpg");
        vidaIcono =  manager.get("iconLife.png");
        cuadro1 =  manager.get("test.png");
        cuadro2 =  manager.get("test2.png");
        cuadro3 =  manager.get("test3.png");
        cuadro4 =  manager.get("test4.png");
        cuadro5 =  manager.get("test5.png");
        cuadro6 = manager.get("test6.png");
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
        generarLimites();
        if(juego.musicOn){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        //personaje.sprite.getBoundingRectangle(
        texto = new Texto();
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        camara.update();
        Gdx.input.setInputProcessor(escenaJuego);
    }

    private void dibujarObjetos() {
        batch.begin();
        batch.draw(textureEscenario, Pantalla.ANCHO/2- textureEscenario.getWidth()/2, Pantalla.ALTO/2- textureEscenario.getHeight()/2);
        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //Vida
        if (personaje.getLife() > 0) {
            String lifeString = "" + personaje.getLife();
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
        }else {
            String lifeString = "0";
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
            juego.setScreen(new PantallaPerder(juego));
        }
        //Balas
        for (Bullet bullet: juego.getBullets()){
            bullet.render(batch);
        }
        batch.draw(vidaIcono,20,Pantalla.ALTO-vidaIcono.getHeight());
        batch.draw(cuadro1,Pantalla.ANCHO/2- cuadro1.getWidth()/2,Pantalla.ALTO - cuadro1.getHeight());
        batch.end();
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        //HUD
        batch.setProjectionMatrix(camara.combined);

        escenaJuego.draw();

        timerDialogo += 1;

        if (timerDialogo >= 120 && timerDialogo < 300){
            cuadro1 = cuadro2;
        }

        if (timerDialogo >= 300 && timerDialogo < 400){
            cuadro1 = cuadro3;
        }

        if (cuadro1 == cuadro3 && personaje.getEstadoMovimiento() != Objeto.EstadoMovimiento.QUIETO){
            cuadro1 = cuadro4;
        }

        if (juego.getBullets().size() > 1 && cuadro1 == cuadro4){
            cuadro1 = cuadro5;
        }

        if(cuadro1==cuadro5 && timerDialogo > 600){
            cuadro1 = cuadro6;
        }
        /*
        if (timerDialogo >= 1000){
            juego.setScreen(new PantallaCuartoD(juego));
        }*/
        System.out.println(timerDialogo);
        if (estado == EstadoJuego.JUGANDO){
            dibujarObjetos();
            dibujarEscena();
            this.jugar(delta);
        }
    }

    private void dibujarEscena() {
        batch.begin();
        escenaJuego.draw();
        personaje.dibujar(batch);

        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());

        escenaJuego.draw();

    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        manager.unload("Joystick/joystick_movimiento.png");
        manager.unload("Joystick/joystick_fondo.png");
        manager.unload("Botones/forward.png");
        manager.unload("Stage/tutorial.jpg");
        manager.unload("iconLife.png");
        manager.unload("test.png");
        manager.unload("test2.png");
        manager.unload("test3.png");
        manager.unload("test4.png");
        manager.unload("test5.png");
        manager.unload("test6.png");
        manager.unload("Music/bensound-extremeaction.mp3");
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
            juego.setScreen(new PantallaPerder(juego));
        }
    }

    @Override
    public void pausa() {
        juego.pausa(vista, batch, escenaJuego, camara);
    }

    @Override
    public void jugar(float delta) {
        juego.jugar(delta, batch, escenaJuego, gunJoystick);
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

}