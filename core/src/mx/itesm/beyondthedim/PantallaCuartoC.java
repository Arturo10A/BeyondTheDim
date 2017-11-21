package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
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

import java.util.ArrayList;

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
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;
    //Jett Speed
    private int DX_PERSONAJE = 5;
    private int DY_PERSONAJE = 5;

    //Medicina
    private Medicina medicina;

    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;

    //Icono de vida
    private Texture vidaIcono;



    private Texture cpu  = new Texture("Objetos_varios/cpu_izq.png");
    private Texture cpu_der = new Texture("Objetos_varios/cpu_der.png");
    private Texture cpu_central = new Texture("Objetos_varios/cpu_central.png");

    //Constructores
    public PantallaCuartoC(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        //this.personaje.setPosition(ANCHO/2,60);
        juego.iniciarCuartoC(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoC();
        juego.setPantallaJuego(this);
    }

    public void setInicioPantallaC(Juego juego){
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoC(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoC();
        juego.setPantallaJuego(this);
    }

    @Override
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

        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        escenaJuego.addActor(juego.getBtnPausa());
    }

    //********************Cargar*******************
    public void cargarTexturas() {
        textureEscenario = new Texture("Stage/escenarioC_abierto.jpg");

        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
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

        cpu1 = new ObjetoEscenario(-10,110, cpu);
        cpu2 = new ObjetoEscenario(ANCHO-230 ,110, cpu_der);
        cpu3 = new ObjetoEscenario(ANCHO/3, ALTO-140, cpu_central);
        //cpu4 = new ObjetoEscenario(ANCHO-200,ALTO/4, cpu);
        medicina = new Medicina(ANCHO/2,ALTO/2);

        juego.getObjetos().add(cpu1);
        juego.getObjetos().add(cpu2);
        juego.getObjetos().add(cpu3);
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
        juego.dibujarObjetos(batch, textureEscenario);
        if (!medicina.vida(personaje)) {
            medicina.dib(batch);
        }
        batch.draw(vidaIcono,20,Pantalla.ALTO-vidaIcono.getHeight());
        System.out.println("X:"+personaje.getPositionX() +" Y:"+personaje.getPositionY());
        medicina.vida(personaje);
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
        /*
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 400, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 300, ALTO / 2, 200, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 400, ALTO / 2, 800, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 500, ALTO / 2, 100, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 600, ALTO / 2, 500, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 700, ALTO / 2, 200, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 800, ALTO / 2, 400, 1));
        */
    }

    @Override
    public void ganar() {
        if (personaje.getPositionX() >= 560 && personaje.getPositionX() <= 680 && personaje.getPositionY() <= 111) {
            juego.getPersonaje().setPosition(595,760);
            juego.getCuartoB().setInicioPantallaB(juego);
            juego.setScreen(juego.getCuartoB());
            dispose();
            escenaJuego.clear();
        }
        if(personaje.getSprite().getBoundingRectangle().overlaps(juego.getLimites().get(1))){
            juego.isCuartoCterminado = true;
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
        if(juego.getLimites().isEmpty()){
            juego.addLimites(cpu1.getSprite().getBoundingRectangle());
            juego.addLimites(cpu2.getSprite().getBoundingRectangle());
            juego.addLimites(cpu3.getSprite().getBoundingRectangle());
            //MURO NORTE
            juego.addLimites(new Rectangle(90, 590, ANCHO, 0));
            //MURO OESTE
            juego.addLimites(new Rectangle(90, 111, 0, ALTO));
            //MURO Este
            juego.addLimites(new Rectangle(1110, 111, 0, ALTO));
            //MURO SUR ESTE
            juego.addLimites(new Rectangle(690, 38, ANCHO, 73));
            //MURO SUR OESTE
            juego.addLimites(new Rectangle(90, 38, 470, 73));
            //PUERTA OESTE
            juego.addLimites(new Rectangle(90, 111, 0, ALTO));
            //juego.limitesGenerados = true;
            //juego.limitesGenerados = true;
        }
    }
}

