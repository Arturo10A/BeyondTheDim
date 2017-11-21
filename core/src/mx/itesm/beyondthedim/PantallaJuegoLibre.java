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
    private Texture textureEscenarioAbiertoC;
    private Texture texturaEscenarioAbiertoD;
    private Texture texturaEscenarioAbiertoBoss;
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
    //
    private ObjetoEscenario cpu1;
    private ObjetoEscenario cpu2;
    private ObjetoEscenario cpu3;
    private ObjetoEscenario cpu4;
    private ObjetoEscenario cpu5;
    private ObjetoEscenario cpu6;
    private ObjetoEscenario cpu7;
    //
    protected boolean isAbiertoCuartoC = false;
    protected boolean isAbiertoCuartoD = false;
    protected boolean isAbiertoCuartoBoss = false;
    private boolean texturasCargadas = false;

    //Constructores
    public PantallaJuegoLibre(Juego juego) {

        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoB(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoB();
        juego.setPantallaJuego(this);
        this.personaje.setPosition(-250, 350);
        this.camara.position.set(-250, 350, 0);
    }

    public void setInicioPantallaB(Juego juego) {
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoB(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoB();
        juego.setPantallaJuego(this);
    }

    @Override
    public void crearEscena() {
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
        textureEscenarioAbiertoC = new Texture("Stage/escenarioB_abiertoC.jpg");
        texturaEscenarioAbiertoD = new Texture("Stage/escenarioB_abiertoD.jpg");
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        texturaCpu = new Texture("Objetos_varios/mesa_de_control_2.png");
        texturaEscenarioAbiertoBoss = new Texture("Stage/escenarioB_abiertoBoss.jpg");
        texturasCargadas = true;
    }

    @Override
    public void cargarMusica() {
        juego.setMusic(Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3")));
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
        cargarMusica();
        generarLimites();
        if (juego.musicOn) {
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        cpu4.sprite.flip(false, true);
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        crearEnemigos();
        System.out.println("Hoola");
        personaje.mover(0, 0);
        this.camara.position.set(-250, 350, 0);
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
        textureEscenarioAbiertoC.dispose();
        texturaEscenarioAbiertoD.dispose();
        texturaEscenarioAbiertoBoss.dispose();
        //Checar
        escenaJuego.dispose();
        texturaCpu.dispose();*/
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
        //Checar si los cuartos C y D han sido completados
        if (!juego.isCuartoCterminado && !juego.isCuartoDterminado) {
            if (personaje.getSprite().getBoundingRectangle().overlaps(juego.getLimites().get(3))) {
                textureEscenario = textureEscenarioAbiertoC;
                //Puerta C get(13)
                juego.getLimites().get(13).setSize(10, 0);
                isAbiertoCuartoC = true;
            }
        } else if (juego.isCuartoCterminado && !juego.isCuartoDterminado) {
            textureEscenario = texturaEscenarioAbiertoD;
            //Puerta D
            juego.getLimites().get(14).setSize(10, 0);
            isAbiertoCuartoD = true;
        } else if (juego.isCuartoCterminado && juego.isCuartoDterminado) {
            textureEscenario = texturaEscenarioAbiertoBoss;
            juego.getLimites().get(15).setSize(0, 10);
            isAbiertoCuartoBoss = true;
        }
        //Checar cuando los cuartos estan abiertos
        if (isAbiertoCuartoC && personaje.getPositionX() >= 570 && personaje.getPositionX() <= 670 && personaje.getPositionY() > 770) {
            if (juego.isCuartoCIniciado) {
                juego.getCuartoC().setInicioPantallaC(juego);
            }
            juego.setScreen(juego.getCuartoC());
            //dispose();
            escenaJuego.clear();
            isAbiertoCuartoC = true;
        }
        if (isAbiertoCuartoD && personaje.getPositionX() >= 570 && personaje.getPositionX() <= 670 && personaje.getPositionY() < -67) {
            if (juego.isCuartoDIniciado) {
                juego.getCuartoD().setInicioPantallaD(juego);
            }
            juego.setScreen(juego.getCuartoD());
            //dispose();
            escenaJuego.clear();
            isAbiertoCuartoC = true;
        }
        if (isAbiertoCuartoBoss && personaje.getPositionX() >= 1500 && personaje.getPositionY() >= 300 && personaje.getPositionY() <= 400) {
            juego.setScreen(new PantallaCuartoEscenarioBoss(juego));
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
        if (juego.getLimites().isEmpty()) {
            //Muro Sur Oeste
            juego.addLimites(new Rectangle(-250, 770, 820, 0));
            //Muro Sur Este
            juego.addLimites(new Rectangle(670, 770, 820, 0));
            //Muro Oeste
            juego.addLimites(new Rectangle(-250, -67, 0, 1900));
            //Muro Norte Oeste
            juego.addLimites(new Rectangle(-250, -67, 820, 0));
            //Muro Norte Este
            juego.addLimites(new Rectangle(670, -67, 820, 0));
            //Muro Este Norte
            juego.addLimites(new Rectangle(1500, -67, 0, 367));
            //Muro Este Sur
            juego.addLimites(new Rectangle(1500, 400, 0, 370));
            //Puerta C get(13)
            juego.addLimites(new Rectangle(560, -67, 110, 0));
            //Puerta D get(14)
            juego.addLimites(new Rectangle(560, 770, 110, 0));
            //Puerta Boss get(15)
            juego.addLimites(new Rectangle(1500, 290, 0, 110));

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
        camara.position.set((int) posX, (int) posY, 0);
        camara.update();
    }
}