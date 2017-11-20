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

public class PantallaCuartoB extends Pantalla implements INiveles {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;
    private Texture texturaCpu;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;
    //
    private int apariciones = 4;
    //
    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;
    private ObjetoEscenario cpu4;
    private ObjetoEscenario cpu5;
    private ObjetoEscenario cpu6;
    private ObjetoEscenario cpu7;


    //Constructores
    public PantallaCuartoB(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoB(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoB();
        juego.setPantallaJuego(this);
    }

    @Override
    public void crearEscena() {
        //Escenario

        cpu1 = new ObjetoEscenario(-45,770, texturaCpu);
        cpu2 = new ObjetoEscenario(-45 ,-67, texturaCpu);
        cpu2.sprite.flip(false,true);
        cpu3 = new ObjetoEscenario(526, 440, texturaCpu);
        cpu4 = new ObjetoEscenario(526,290, texturaCpu);
        cpu4.sprite.flip(false,true);
        cpu5 = new ObjetoEscenario(970 ,770, texturaCpu);
        cpu6 = new ObjetoEscenario(970, -67, texturaCpu);
        cpu6.sprite.flip(false,true);
        escenaJuego = juego.getEscenaCuartoB();
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
        juego.getObjetos().add(cpu1);
        juego.getObjetos().add(cpu2);
        juego.getObjetos().add(cpu3);
        juego.getObjetos().add(cpu4);
        juego.getObjetos().add(cpu5);
        juego.getObjetos().add(cpu6);
    }



    //********************Cargar*******************
    @Override
    public void cargarTexturas() {
        textureEscenario = new Texture("Stage/escenarioB_cerrado.jpg");
        textureEscenarioAbierto = new Texture("Stage/escenarioB_cerrado.jpg");
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        texturaCpu = new Texture("Objetos_varios/mesa_de_control_2.png");
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
        cpu4.sprite.flip(false,true);
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        System.out.println("Hoola");
        personaje.mover(0,0);
        this.camara.position.set(-250,350,0);
        camara.update();
        Gdx.input.setInputProcessor(escenaJuego);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0, 0, 0);
        actualizarCamara();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        juego.dibujarObjetos(batch, textureEscenario);
        batch.end();
        if(juego.getEnemy_list().isEmpty() && apariciones >= 0){
            crearEnemigos();
            apariciones--;
        }
        //Dibujar Objetos
        //batch.setProjectionMatrix(juego.camaraHUDEscenarioB.combined);
        batch.begin();
        escenaJuego.draw();
        batch.end();
        //Dibujar escena del juego
        //escenaJuego.act(Gdx.graphics.getDeltaTime());
        //Jugar
        jugar(delta);
        //Ganar/Perder
        perder();
        ganar();
        //Pausa
        pausa();
        System.out.println(personaje.sprite.getX()+ " " + personaje.sprite.getY());
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
    public void resize(int width, int height) {
        vista.update(width, height);
        juego.vistaHUDEscenarioB.update(width, height);
    }


    @Override
    public void crearEnemigos() {
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
    }

    @Override
    public void ganar() {
        if (juego.getEnemy_list().isEmpty()){
            textureEscenario = textureEscenarioAbierto;
            juego.getLimites().get(5).setSize(10);
            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                juego.setScreen(new PantallaCuartoC(juego));
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
        if(juego.getLimites().isEmpty()){
            juego.addLimites(cpu1.getSprite().getBoundingRectangle());
            juego.addLimites(cpu2.getSprite().getBoundingRectangle());
            juego.addLimites(cpu3.getSprite().getBoundingRectangle());
            juego.addLimites(cpu4.getSprite().getBoundingRectangle());
            juego.addLimites(cpu5.getSprite().getBoundingRectangle());
            juego.addLimites(cpu6.getSprite().getBoundingRectangle());
            juego.addLimites(new Rectangle(-250, 770, 820, 0));
            juego.addLimites(new Rectangle(670, 770, 820, 0));
            juego.addLimites(new Rectangle(-250, -67, 0, 1900));
            juego.addLimites(new Rectangle(-250,-67, 820, 0));
            juego.addLimites(new Rectangle(670, -67, 820, 0));
            juego.addLimites(new Rectangle(1500, -67, 0, 367));
            juego.addLimites(new Rectangle(1500, 400, 0, 370));

            //juego.limitesGenerados = true;
        }
    }

    private void actualizarCamara() {
        float posX = personaje.sprite.getX();
        float posY = personaje.sprite.getY();
        /*// Si está en la parte 'media'
        if (posX>=ANCHO/4 && posX<=ANCHO-ANCHO/4) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, (int)posY, 0);
            System.out.println("Parte1");
        } else if (posX>ANCHO-ANCHO/4) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            if(posY>=ALTO/4 && posY<=ALTO-ALTO/4){
                camara.position.set(ANCHO-ANCHO/4,(int)posY, 0);
            }
            System.out.println("Parte2");
        } else if ( posX<ANCHO/4 ) { // La primera mitad
            if(posY>=ALTO/4 && posY<=ALTO-ALTO/4){
                camara.position.set(ANCHO/4,(int)posY, 0);
            }
            System.out.println("Parte3");
        }*/
        camara.position.set((int)posX,(int)posY, 0);
        camara.update();
    }


}
