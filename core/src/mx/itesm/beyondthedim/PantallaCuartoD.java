package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
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
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class PantallaCuartoD extends Pantalla implements INiveles {


    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    private ArrayList<ObjetoEscenario> objetos = new ArrayList<ObjetoEscenario>();

    //Icono de vida
    private Texture vidaIcono;

    private ObjetoEscenario redBed;
    private ObjetoEscenario blueBed;
    private ObjetoEscenario greenBed;
    private ObjetoEscenario seaBed;
    private ObjetoEscenario extintor;
    private ObjetoEscenario PC;

    private Texture texturaRedBed;
    private Texture texturaBlueBed;
    private Texture texturaSeaBed;
    private Texture texturaGreenBed;
    private Texture texturaExt;
    private Texture texturaPC;

    //Constructores
    public PantallaCuartoD(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoD(vista, camara);
        juego.setPantallaJuego(this);
    }


    public void setInicioPantallaD(Juego juego){
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoD(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoD();
        juego.setPantallaJuego(this);
    }

    @Override
    public void crearEscena() {
        //Escenario
        redBed = new ObjetoEscenario(ANCHO-(ANCHO/5),ALTO/3, texturaRedBed);
        blueBed = new ObjetoEscenario(ANCHO/12,(ALTO/2)+(ALTO/12), texturaBlueBed);
        greenBed = new ObjetoEscenario(ANCHO-(ANCHO/5),(ALTO/2)+(ALTO/12), texturaGreenBed);
        seaBed = new ObjetoEscenario(ANCHO/12,ALTO/3, texturaSeaBed);
        PC = new ObjetoEscenario(ANCHO/3,(ALTO/2)+(ALTO+3), texturaPC);
        extintor = new ObjetoEscenario((ANCHO/2)+ANCHO/8,(ALTO/2)+(ALTO+3), texturaExt);

        juego.getObjetos().add(redBed);
        juego.getObjetos().add(blueBed );
        juego.getObjetos().add(greenBed);
        juego.getObjetos().add(seaBed );
        juego.getObjetos().add(PC);
        juego.getObjetos().add(extintor);

        escenaJuego = juego.getEscenaCuartoD();
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
        //Listener joystick movimientov
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
    @Override
    public void cargarTexturas() {
        textureEscenario = new Texture("Stage/stageD_Close.png");
        textureEscenarioAbierto = new Texture("Stage/stageD_Open.png");

        texturaRedBed = new Texture("Objetos_varios/cama_3_2.png");
        texturaBlueBed = new Texture("Objetos_varios/cama_2_2.png");
        texturaSeaBed = new Texture("Objetos_varios/cama_4_2.png");
        texturaGreenBed = new Texture("Objetos_varios/cama_1_2.png");
        texturaPC = new Texture("Objetos_varios/computadora_2.png");
        texturaExt = new Texture("Objetos_varios/extintor.png");
        vidaIcono = new Texture("iconLife.png");

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
        juego.conMovPadGrande2(batch, movJoystick);
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
        textureEscenario.dispose();
        //Escenario
        escenaJuego.dispose();
        //Icono de vida
        vidaIcono.dispose();
        texturaRedBed.dispose();
        texturaBlueBed.dispose();
        texturaSeaBed.dispose();
        texturaGreenBed.dispose();*/
    }


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
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
            juego.getMusic().stop();
            juego.musicaCargada = false;
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
        if(juego.getLimites().isEmpty()){
            juego.addLimites(new Rectangle(0, ALTO - 120, ANCHO, 120));
            juego.addLimites(new Rectangle(0, 0, 120, ALTO));
            juego.addLimites(new Rectangle(0, 0, ANCHO, 120));
            juego.addLimites(new Rectangle(1160, 0, 120, ALTO));

            juego.addLimites(new Rectangle(ANCHO-(ANCHO/5),ALTO/3,90,180));
            juego.addLimites(new Rectangle(ANCHO/12,(ALTO/2)+(ALTO/12),90,180));
            juego.addLimites(new Rectangle(ANCHO-(ANCHO/5),(ALTO/2)+(ALTO/12),90,180));
            juego.addLimites(new Rectangle(ANCHO/12,ALTO/3,90,180));


            //juego.limitesGenerados = true;
        }
    }
}

