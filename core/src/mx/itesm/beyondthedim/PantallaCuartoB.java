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

public class PantallaCuartoB extends Pantalla implements INiveles {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbiertoC;
    private Texture texturaEscenarioAbiertoD;
    private Texture texturaEscenarioAbiertoBoss;
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
    public PantallaCuartoB(Juego juego) {

        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoB(vista, camara);
        //Escenario
        escenaJuego = juego.getEscenaCuartoB();
        juego.setPantallaJuego(this);
        this.personaje.setPosition(80, 530);
        this.camara.position.set(70, 530, 0);
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
        //Escenario
        cpu1 = new ObjetoEscenario(275, 950, texturaCpu);
        cpu2 = new ObjetoEscenario(275, 113, texturaCpu);
        cpu2.sprite.flip(false, true);
        cpu3 = new ObjetoEscenario(846, 620, texturaCpu);
        cpu4 = new ObjetoEscenario(846, 470, texturaCpu);
        cpu4.sprite.flip(false, true);
        cpu5 = new ObjetoEscenario(1290, 950, texturaCpu);
        cpu6 = new ObjetoEscenario(1290, 113, texturaCpu);
        cpu6.sprite.flip(false, true);
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
        if(isAbiertoCuartoC){
            juego.getLimites().get(13).setSize(0);
        }
        if(isAbiertoCuartoD){
            juego.getLimites().get(13).setSize(0);
        }
        //Añadir enemigo
        crearEnemigos();
        System.out.println("Hoola");
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
        /*
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));

        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));*/
    }

    @Override
    public void ganar() {
        //Checar si los cuartos C y D han sido completados
        if (!juego.isCuartoCterminado && !juego.isCuartoDterminado) {
            if (personaje.getSprite().getBoundingRectangle().overlaps(juego.getLimites().get(3))) {
                textureEscenario = textureEscenarioAbiertoC;
                //Puerta C get(13)
                juego.getLimites().get(13).setSize(0);
                isAbiertoCuartoC = true;
            }
        } else if (juego.isCuartoCterminado && !juego.isCuartoDterminado) {
            textureEscenario = texturaEscenarioAbiertoD;
            //Puerta D
            juego.getLimites().get(14).setSize(0);
            isAbiertoCuartoD = true;
        } else if (juego.isCuartoCterminado && juego.isCuartoDterminado) {
            textureEscenario = texturaEscenarioAbiertoBoss;
            juego.getLimites().get(15).setSize(0);
            isAbiertoCuartoBoss = true;
        }
        //Checar cuando los cuartos estan abiertos
        if (isAbiertoCuartoC && personaje.getPositionX() >= 890 && personaje.getPositionX() <= 990 && personaje.getPositionY() > 950) {
            if (juego.isCuartoCIniciado) {
                juego.getCuartoC().setInicioPantallaC(juego);
            }
            juego.setScreen(juego.getCuartoC());
            //dispose();
            escenaJuego.clear();
            isAbiertoCuartoC = true;
        }
        if (isAbiertoCuartoD && personaje.getPositionX() >= 890 && personaje.getPositionX() <= 990 && personaje.getPositionY() < 113) {
            if (juego.isCuartoDIniciado) {
                juego.getCuartoD().setInicioPantallaD(juego);
            }
            juego.setScreen(juego.getCuartoD());
            //dispose();
            escenaJuego.clear();
            isAbiertoCuartoC = true;
        }
        if (isAbiertoCuartoBoss && personaje.getPositionX() >= 1820 && personaje.getPositionY() >= 480 && personaje.getPositionY() <= 580) {
            personaje.setPosition(ANCHO / 9.5f, ALTO / 2f);
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

            //juego.limitesGenerados = true;
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
        }
        else if(posX>ANCHO_B-ANCHO_B/3){    // Si está en la última mitad
        // La cámara se queda a media pantalla antes del fin del mundo  :)
            camaraPosX = ANCHO_B - ANCHO_B / 3;
            //System.out.println("Parte2");
        }
        else if(posX<ANCHO_B/3) { // La primera mitad
            camaraPosX = ANCHO_B/3;

            //System.out.println("Parte3");
        }
        if (posY >= ALTO_B / 3 && posY <= ALTO_B - ALTO_B / 3) {
            camaraPosY = posY;
        }
        else if (posY > ALTO_B-ALTO/3) {
            camaraPosY = ALTO_B - ALTO_B / 3;
        }
        else if (posY <ALTO_B/3) {
            camaraPosY = ALTO_B/3;
        }
        camara.position.set(camaraPosX,camaraPosY,0);
        camara.update();
    }

}
