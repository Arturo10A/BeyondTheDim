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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class PantallaJuegoLibre extends Pantalla implements INiveles {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private int ANCHO_B = 1920;
    private int ALTO_B = 1080;
    //Jett start
    private Personaje personaje;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaCpu;
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;
    //
    private int apariciones = 4;
    private int difficultyLevel = 1;
    private int randomNumX;
    private int randomNumY;

    //
    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;
    private ObjetoEscenario cpu4;
    private ObjetoEscenario cpu5;
    private ObjetoEscenario cpu6;
    //
    private boolean texturasCargadas = false;
    //
    private AssetManager manager;

    //Constructores
    public PantallaJuegoLibre(Juego juego) {

        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoLibre(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoLibre();
        juego.setPantallaJuego(this);
        this.personaje.setPosition(80, 530);
        this.camara.position.set(70, 530, 0);
        manager = juego.getAssetManager();
    }

    public void setInicioPantallaJuegoLibre(Juego juego) {
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoLibre(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoLibre();
        juego.setPantallaJuego(this);
    }

    @Override
    public void crearEscena() {
        //Escenario
        cpu1 = new ObjetoEscenario(275, 950, texturaCpu);
        cpu2 = new ObjetoEscenario(275, 90, texturaCpu);
        cpu2.sprite.flip(false, true);
        cpu3 = new ObjetoEscenario(846, 520, texturaCpu);
        cpu4 = new ObjetoEscenario(846, 470, texturaCpu);
        cpu3.sprite.flip(false, true);
        cpu5 = new ObjetoEscenario(1290, 950, texturaCpu);
        cpu6 = new ObjetoEscenario(1290, 90, texturaCpu);
        cpu6.sprite.flip(false, true);
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
        textureEscenario = manager.get("Stage/escenario_libre.jpg");
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        texturaCpu = manager.get("Objetos_varios/mesa_de_control_2.png");
        texturasCargadas = true;
    }

    @Override
    public void cargarMusica() {
        juego.setMusic((Music) manager.get("Music/bensound-extremeaction.mp3"));
        juego.getMusic().setLooping(true);
    }

    @Override
    public void show() {
        //Cargar escena
        System.out.println("Se hizo show");
        if (!texturasCargadas) {
            cargarTexturas();
        }
        crearEscena();
        if (!juego.musicaCargada) {
            cargarMusica();
        }
        generarLimites();

        if (juego.musicOn && !juego.getMusic().isPlaying()) {
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
            System.out.println("Entro");
        }
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        personaje.mover(0, 0);
        this.camara.position.set(70, 530, 0);
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
        if (juego.getEnemy_list().isEmpty() && apariciones >= 0) {
            difficultyLevel+=1;
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
        //System.out.println(personaje.sprite.getX()+ " " + personaje.sprite.getY());
        //System.out.println(camara.position);
    }


    @Override
    public void pause() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            juego.setEstadoJuego(EstadoJuego.PAUSADO);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {


        manager.unload("Joystick/joystick_movimiento.png");
        manager.unload("Joystick/joystick_fondo.png");
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        manager.unload("Objetos_varios/mesa_de_control_2.png");
        manager.unload("Stage/escenario_libre.jpg");
        manager.unload("Music/bensound-extremeaction.mp3");
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
        juego.vistaHUDEscenarioB.update(width, height);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static int nthPrime(int n) {
        int candidate, count;
        for(candidate = 2, count = 0; count < n; ++candidate) {
            if (isPrime(candidate)) {
                ++count;
            }
        }
        return candidate-1;
    }
    private static boolean isPrime(int n) {
        for(int i = 2; i < n; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void crearEnemigos() {

        int enemiesThisTime = nthPrime(difficultyLevel);
        for(int i=0;i<enemiesThisTime;i++){
            randomNumX = randInt((int)(ANCHO/16),(int)(ANCHO-(ANCHO/16)));
            randomNumY = randInt((int)(ALTO/9),(int)(ALTO-(ALTO/9)));
            if((randomNumX<personaje.getPositionX()+40 && randomNumX>personaje.getPositionX()-40)&& (randomNumY<personaje.getPositionY()+40 && randomNumY>personaje.getPositionY()-40)){
                if(randomNumX<0){
                    randomNumX-=40;
                }else{
                    randomNumX+=40;
                }
                if(randomNumY<0){
                    randomNumY-=40;
                }else{
                    randomNumY+=40;
                }
            }
            juego.getEnemy_list().add(new Enemy(randomNumX, randomNumY, 100, 1));
        }

    }


    @Override
    public void ganar() {
        //Checar si los cuartos C y D han sido completados

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
        juego.jugar(delta, batch, escenaJuego, gunJoystick);
    }

    @Override
    public void generarLimites() {
        if (juego.getLimites().isEmpty()) {
            juego.addLimites(cpu1.getSprite().getBoundingRectangle());
            juego.addLimites(cpu2.getSprite().getBoundingRectangle());
            juego.addLimites(cpu3.getSprite().getBoundingRectangle());
            juego.addLimites(cpu4.getSprite().getBoundingRectangle());
            juego.addLimites(cpu5.getSprite().getBoundingRectangle());
            juego.addLimites(cpu6.getSprite().getBoundingRectangle());
            //Muro Sur Oeste
            juego.addLimites(new Rectangle(102, 950, 820, 0));
            //Muro Sur Este
            juego.addLimites(new Rectangle(1092, 950, 820, 0));
            //Muro Oeste
            juego.addLimites(new Rectangle(102, 113, 0, 1900));
            //Muro Norte Oeste
            juego.addLimites(new Rectangle(102, 113, 820, 0));
            //Muro Norte Este
            juego.addLimites(new Rectangle(1092, 113, 820, 0));
            //Muro Este Norte
            juego.addLimites(new Rectangle(1852, 113, 0, 367));
            //Muro Este Sur
            juego.addLimites(new Rectangle(1852, 580, 0, 370));
            //Puerta C get(13)
            juego.addLimites(new Rectangle(912, 950, 110, 0));
            //Puerta D get(14)
            juego.addLimites(new Rectangle(912, 113, 110, 0));
            //Puerta Boss get(15)
            juego.addLimites(new Rectangle(1852, 470, 0, 110));
        }
    }

    private void actualizarCamara() {
        float posX = personaje.sprite.getX();
        float posY = personaje.sprite.getY();
        float camaraPosX = camara.position.x;
        float camaraPosY = camara.position.y;
        // Si está en la parte 'media'
        ANCHO_B = textureEscenario.getWidth();
        ALTO_B = textureEscenario.getHeight();
        if (posX >= ANCHO_B / 3 && posX <= ANCHO_B - ANCHO_B / 3) {
            // El personaje define el centro de la cámara
            //camara.position.set(posX, camara.position.y, 0);
            camaraPosX = posX;
            //System.out.println("Parte1");
        } else if (posX > ANCHO_B - ANCHO_B / 3) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camaraPosX = ANCHO_B - ANCHO_B / 3;
            //System.out.println("Parte2");
        } else if (posX < ANCHO_B / 3) { // La primera mitad
            camaraPosX = ANCHO_B / 3;
        }
        if (posY >= ALTO_B / 3 && posY <= ALTO_B - ALTO_B / 3) {
            camaraPosY = posY;
        } else if (posY > ALTO_B - ALTO / 3) {
            camaraPosY = ALTO_B - ALTO_B / 3;
        } else if (posY < ALTO_B / 3) {
            camaraPosY = ALTO_B / 3;
        }
        camara.position.set(camaraPosX, camaraPosY, 0);
        camara.update();
    }
}