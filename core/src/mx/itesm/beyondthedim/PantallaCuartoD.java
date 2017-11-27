package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

    private AssetManager manager;

    //Constructores
    public PantallaCuartoD(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoD(vista, camara);
        juego.setPantallaJuego(this);
        manager = juego.getAssetManager();
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
        Gdx.input.setInputProcessor(this.escenaJuego);
        Gdx.input.setCatchBackKey(true);
        //*******************************************************Joysticks*******************************************************
        //Texturas
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
        //Listener joystick movimientov

        //Listener joystick movimiento
        /*
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                juego.conMovPadGrande(batch, pad, movJoystick);
            }
        });*/
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
        textureEscenario = manager.get("Stage/escenarioD_abierto.jpg");

        texturaRedBed = manager.get("Objetos_varios/cama_3_2.png");
        texturaBlueBed = manager.get("Objetos_varios/cama_2_2.png");
        texturaSeaBed = manager.get("Objetos_varios/cama_4_2.png");
        texturaGreenBed = manager.get("Objetos_varios/cama_1_2.png");
        texturaPC = manager.get("Objetos_varios/computadora_2.png");
        texturaExt = manager.get("Objetos_varios/extintor.png");
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
        juego.pausa(vista, batch, escenaJuego, camara);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setEstadoJuego(EstadoJuego.PAUSADO);
            System.out.println(camara.position);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        manager.unload("Joystick/joystick_movimiento.png");
        manager.unload("Joystick/joystick_fondo.png");
        manager.unload("Stage/escenarioD_abierto.jpg");
        manager.unload("Objetos_varios/cama_3_2.png");
        manager.unload("Objetos_varios/cama_2_2.png");
        manager.unload("Objetos_varios/cama_4_2.png");
        manager.unload("Objetos_varios/cama_1_2.png");
        manager.unload("Objetos_varios/computadora_2.png");
        manager.unload("Objetos_varios/extintor.png");
        manager.unload("iconLife.png");
        manager.unload("Music/bensound-extremeaction.mp3");
    }


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
        juego.getEnemy_list().add(new Enemy( 200, ALTO / 4, 300, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 400, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO , 400, 1));
    }

    @Override
    public void ganar() {
        if (personaje.getPositionX() >= 560 && personaje.getPositionX() <= 680 && personaje.getPositionY() >= 590) {
            juego.getPersonaje().setPosition(915,120);
            juego.getCuartoB().setInicioPantallaB(juego);
            juego.setScreen(new PantallaCargando(juego, Pantallas.CUARTO_B));
            escenaJuego.clear();
        }
        if(!juego.getLimites().isEmpty()){
            if(personaje.getSprite().getBoundingRectangle().overlaps(juego.getLimites().get(1))){
                juego.isCuartoDterminado = true;
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
        juego.pausa(vista, batch, escenaJuego, camara);
    }

    @Override
    public void jugar(float delta) {
        juego.jugar(delta, batch, escenaJuego, gunJoystick, movJoystick);
    }

    @Override
    public void generarLimites() {
        if(juego.getLimites().isEmpty()){
            juego.addLimites(redBed.getSprite().getBoundingRectangle());
            juego.addLimites(blueBed.getSprite().getBoundingRectangle());
            juego.addLimites(greenBed.getSprite().getBoundingRectangle());
            juego.addLimites(seaBed.getSprite().getBoundingRectangle());
            juego.addLimites(PC.getSprite().getBoundingRectangle());
            juego.addLimites(extintor.getSprite().getBoundingRectangle());
            //MURO NORTE
            juego.addLimites(new Rectangle(90, 111, ANCHO, 0));
            //MURO OESTE
            juego.addLimites(new Rectangle(90, 111, 0, ALTO));
            //MURO Este
            juego.addLimites(new Rectangle(1110, 111, 0, ALTO));
            //MURO Norte ESTE
            juego.addLimites(new Rectangle(690, 590, ANCHO, 73));
            //MURO Norte OESTE
            juego.addLimites(new Rectangle(90, 590, 470, 73));
            //PUERTA OESTE
            juego.addLimites(new Rectangle(90, 111, 0, ALTO));


            //juego.limitesGenerados = true;
        }
    }
}

