package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import javax.xml.soap.Text;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class PantallaCuartoC extends Pantalla implements INiveles {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //Jett start
    private Personaje personaje;
    private Personaje obstacle;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;


    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;
    private ObjetoEscenario cpu4;


    private Texture cpu  = new Texture("Objetos_varios/cpu_izq.png");
    private Texture cpu_der = new Texture("Objetos_varios/cpu_der.png");

    private ArrayList<ObjetoEscenario> objetos = new ArrayList<ObjetoEscenario>(5);

    //Constructores
    public PantallaCuartoC(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        this.personaje.setPosition(ANCHO/2,60);
        juego.iniciarCuartoC(vista);
        //Escenario
        escenaJuego = juego.getEscenaCuartoC();
    }

    public void crearEscena() {
        //Escenario
        escenaJuego = juego.getEscenaCuartoC();
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
                juego.controlJoystickMovimiento(batch, pad, movJoystick, obstacle, camara);
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

    //********************Cargar*******************
    public void cargarTexturas() {
        textureEscenario = new Texture("Stage/escenarioC.jpg");
        textureEscenarioAbierto = new Texture("Stage/escenarioCabierto.jpg");
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
        obstacle = new Personaje(ANCHO / 2+100, ALTO / 2, 1);

        cpu1 = new ObjetoEscenario(-10,110, cpu);
        cpu2 = new ObjetoEscenario(-10 ,380, cpu_der);
        //cpu3 = new ObjetoEscenario(ANCHO-400, ALTO/2, cpu);
        //cpu4 = new ObjetoEscenario(ANCHO-200,ALTO/4, cpu);

        objetos.add(cpu1);
        objetos.add(cpu2);
        //objetos.add(cpu3);
        //objetos.add(cpu4);

        generarLimites();
        if(juego.musicOn){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        Gdx.input.setInputProcessor(escenaJuego);
}

    @Override
    public void render(float delta) {
        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);
        //HUD
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        juego.dibujarObjetos(batch, textureEscenario, obstacle, objetos);
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

    }


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 400, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 300, ALTO / 2, 200, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 400, ALTO / 2, 800, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 500, ALTO / 2, 100, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 600, ALTO / 2, 500, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 700, ALTO / 2, 200, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 800, ALTO / 2, 400, 1));
    }

    @Override
    public void ganar() {
        if (juego.getEnemy_list().isEmpty()){
            textureEscenario = textureEscenarioAbierto;
            juego.getLimites().get(5).setSize(10);
            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                juego.setScreen(new PantallaCuartoB(juego));
                dispose();
                escenaJuego.clear();
            }
        }
    }

    @Override
    public void perder() {
        if (personaje.getLife() <= 0) {
            juego.setScreen(new PantallaPerder(juego));

        }
    }

    @Override
    public void pausa() {
        juego.pausa(vista, batch, escenaJuego);
    }

    @Override
    public void jugar(float delta) {
        juego.jugar(delta, batch, escenaJuego, gunJoystick);
    }

    @Override
    public void generarLimites() {
        if(!juego.limitesGenerados){
            juego.addLimites(obstacle.getSprite().getBoundingRectangle());
            juego.addLimites(new Rectangle(0, ALTO - 120, ANCHO, 120));
            juego.addLimites(new Rectangle(0, 0, 120, ALTO));
            juego.addLimites(new Rectangle(0, 0, ANCHO, 120));
            juego.addLimites(new Rectangle(1160, ALTO - 300, 120, 300));
            juego.addLimites(new Rectangle(1160, 0, 120, 300));
            juego.addLimites(new Rectangle(1160, 300, 120, 120));

            juego.limitesGenerados = true;
        }
    }
}

