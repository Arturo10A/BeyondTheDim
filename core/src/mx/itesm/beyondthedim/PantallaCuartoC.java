package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Medicina
    private Medicina medicina;

    private Texture cpu;
    private Texture cpu_der;
    private Texture cpu_central;
    //Icono de vida
    private Texture vidaIcono;

    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;
    //
    private AssetManager manager;




    //Constructores
    public PantallaCuartoC(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        //this.personaje.setPosition(ANCHO/2,60);
        juego.iniciarCuartoC(vista, camara);
        camara.position.set(640,360,0);
        //Escenario
        escenaJuego = juego.getEscenaCuartoC();
        juego.setPantallaJuego(this);
        manager = juego.getAssetManager();
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
                System.out.println(camara.position);
            }
        });

        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        escenaJuego.addActor(juego.getBtnPausa());
    }

    //********************Cargar*******************
    public void cargarTexturas() {
        textureEscenario = manager.get("Stage/escenarioC_abierto.jpg");

        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        vidaIcono = manager.get("iconLife.png");
        cpu  = manager.get("Objetos_varios/cpu_izq.png");
        cpu_der = manager.get("Objetos_varios/cpu_der.png");
        cpu_central = manager.get("Objetos_varios/cpu_central.png");
    }
    @Override
    public void cargarMusica(){
        juego.setMusic((Music) manager.get("Music/bensound-extremeaction.mp3"));
        juego.getMusic().setLooping(true);
        juego.musicaCargada = true;
    }
    @Override
    public void show() {

        //Cargar escena
        cargarTexturas();
        crearEscena();
        if(!juego.musicaCargada){
            cargarMusica();
        }

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
        if (!medicina.vida(personaje)) {
            medicina.dib(batch);
        }
        batch.draw(vidaIcono,20,Pantalla.ALTO-vidaIcono.getHeight());
        //System.out.println("X:"+personaje.getPositionX() +" Y:"+personaje.getPositionY());
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
        manager.unload("Joystick/joystick_movimiento.png");
        manager.unload("Joystick/joystick_fondo.png");
        manager.unload("Stage/escenarioC_abierto.jpg");

        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        manager.unload("iconLife.png");
        manager.unload("Objetos_varios/cpu_izq.png");
        manager.unload("Objetos_varios/cpu_der.png");
        manager.unload("Objetos_varios/cpu_central.png");
        manager.unload("Music/bensound-extremeaction.mp3");
    }


    @Override
    public void crearEnemigos() {

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 4, 400, 2));
        juego.getEnemy_list().add(new Enemy(ANCHO - 300, ALTO / 6, 200, 2));
        juego.getEnemy_list().add(new Enemy(ANCHO - 400, ALTO / 8, 800, 1));
        juego.getEnemy_list().add(new Enemy(ANCHO - 500, ALTO - 200, 100, 2));
        juego.getEnemy_list().add(new Enemy(ANCHO - 600, ALTO -100, 500, 2));
        juego.getEnemy_list().add(new Enemy(ANCHO - 700, ALTO / 2, 200, 2));
        juego.getEnemy_list().add(new Enemy(ANCHO - 800, ALTO / 2, 400, 1));

    }

    @Override
    public void ganar() {
        if (personaje.getPositionX() >= 560 && personaje.getPositionX() <= 680 && personaje.getPositionY() <= 111) {
            juego.getPersonaje().setPosition(915,935);
            juego.getCuartoB().setInicioPantallaB(juego);
            juego.setScreen(new PantallaCargando(juego, Pantallas.CUARTO_B));
            escenaJuego.clear();
        }
        if(!juego.getLimites().isEmpty()){
            if(personaje.getSprite().getBoundingRectangle().overlaps(juego.getLimites().get(1))){
                juego.isCuartoCterminado = true;
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
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setEstadoJuego(EstadoJuego.PAUSADO);
            System.out.println(camara.position);
        }
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

