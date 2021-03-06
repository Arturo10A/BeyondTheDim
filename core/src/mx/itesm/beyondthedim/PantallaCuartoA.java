package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

public class PantallaCuartoA  extends Pantalla implements INiveles {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Icono de vida
    private Texture vidaIcono;

    private AssetManager manager;

    //Constructores
    public PantallaCuartoA(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoA(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoA();
        juego.setPantallaJuego(this);
        manager = juego.getAssetManager();
    }

    public void setInicioPantallaA(Juego juego) {
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoA(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoA();
        juego.setPantallaJuego(this);
    }

    @Override
    public void crearEscena() {
        //Escenario
        escenaJuego = juego.getEscenaCuartoA();
        //*******************************************************Joysticks*******************************************************
        //Texturas

        Gdx.input.setInputProcessor(this.escenaJuego);
        Gdx.input.setCatchBackKey(true);

        Skin skin = new Skin();
        skin.add("padFondo", manager.get("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento", manager.get("Joystick/joystick_movimiento.png"));
        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padFondo");
        estilo.knob = skin.getDrawable("padMovimiento");
        //Joystick pistola
        gunJoystick = new Touchpad(20, estilo);
        gunJoystick.setBounds(Pantalla.ANCHO - 210, 0, 200, 200);
        //Joystick movimiento
        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(10, 0, 200, 200);
        movJoystick.setColor(1, 1, 1, 0.7f);
        //Listener joystick movimiento
        /*
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                //juego.conMovPadGrande(batch, pad, movJoystick);
            }
        });*/
        //****************************************Boton Pausa -> check variable and conflic agins problems*********************************************
        //Listener boton pausa
        juego.getBtnPausa().addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setEstadoJuego(EstadoJuego.PAUSADO);
                System.out.println(camara.position);
            }
        });

        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        escenaJuego.addActor(juego.getBtnPausa());
    }


    //********************Cargar*******************
    @Override
    public void cargarTexturas() {
        textureEscenario = manager.get("Stage/fondo_nivel_uno_cerrado.jpg");
        textureEscenarioAbierto = manager.get("Stage/fondo_nivel_uno_abierto.jpg");
        vidaIcono = manager.get("iconLife.png");
    }
    @Override
    public void cargarMusica(){
        juego.setMusic((Music) manager.get("Music/bensound-extremeaction.mp3"));
        juego.getMusic().setLooping(true);
    }
    @Override
    public void show() {
        //Cargar escena

        cargarTexturas();
        crearEscena();
        if(!juego.musicaCargada){
            cargarMusica();
        }
        generarLimites();
        if(juego.musicOn && !juego.getMusic().isPlaying()){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
            System.out.println("Entro");
        }
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        camara.update();
        Gdx.input.setInputProcessor(escenaJuego);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);
        //HUD
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        juego.dibujarObjetos(batch, textureEscenario);
        batch.draw(vidaIcono,20,Pantalla.ALTO-vidaIcono.getHeight());

        batch.end();

        //Dibujar Objetos
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


    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        /*
        //Imagen del ecenario
        textureEscenario.dispose();
        textureEscenarioAbierto.dispose();
        //Checar
        escenaJuego.dispose();
        vidaIcono.dispose();*/
        manager.unload("Joystick/joystick_movimiento.png");
        manager.unload("Joystick/joystick_fondo.png");
        manager.unload("Stage/fondo_nivel_uno_cerrado.jpg");
        manager.unload("Stage/fondo_nivel_uno_abierto.jpg");
        manager.unload("iconLife.png");
        manager.unload("Music/bensound-extremeaction.mp3");
    }


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
    }

    @Override
    public void ganar() {

        if (juego.getEnemy_list().isEmpty()) {
            textureEscenario = textureEscenarioAbierto;
            juego.getLimites().get(5).setSize(0);

            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() > 315 && personaje.getPositionY() < 420) {
                juego.setScreen(new PantallaCargando(juego, Pantallas.CUARTO_B));
                escenaJuego.clear();
            }

        }
    }

    @Override
    public void perder() {
        if (personaje.getLife() <= 0) {
            juego.getMusic().stop();
            juego.musicaCargada = false;
            juego.setScreen(new PantallaPerder(juego, Pantallas.CUARTO_A));
        }
    }

    @Override
    public void pausa() {
        juego.pausa(vista, batch, escenaJuego, camara);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setEstadoJuego(EstadoJuego.PAUSADO);
            System.out.println(camara.position);
        }
    }

    @Override
    public void jugar(float delta) {
        juego.jugar(delta, batch, escenaJuego, gunJoystick, movJoystick);
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
