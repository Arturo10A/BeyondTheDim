package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
    private Personaje obstacle;
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnPausa;

    //Constructores
    public PantallaCuartoA(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
        juego.iniciarCuartoA(vista);
    }

    @Override
    public void crearEscena() {
        //Escenario
        escenaJuego = juego.getEscenaCuartoA();
        //*******************************************************Joysticks*******************************************************
        //Listener joystick movimiento
        juego.getMovJoystick().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                juego.controlJoystickMovimiento(batch, pad, obstacle, camara);
            }
        });
        //
        escenaJuego.addActor(juego.getMovJoystick());
        escenaJuego.addActor(juego.getGunJoystick());
        //****************************************Boton Pausa -> check variable and conflic agins problems*********************************************
        //Listener boton pausa
        juego.getBtnPausa().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setEstadoJuego(EstadoJuego.PAUSADO);
            }
        });
        escenaJuego.addActor(juego.getBtnPausa());
    }

    //********************Cargar*******************
    @Override
    public void cargarTexturas() {
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.jpg");
        textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.jpg");
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
        obstacle = new Personaje(ANCHO / 2+100, ALTO / 2, 1000);
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
        escenaJuego.draw();
        //Dibujar Objetos
        batch.begin();
        juego.dibujarObjetos(batch, textureEscenario, obstacle);
        batch.end();
        //Dibujar escena del juego
        batch.begin();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
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
        juego.getEnemy_list().add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
    }

    @Override
    public void ganar() {
        if (juego.getEnemy_list().isEmpty()){
            textureEscenario = textureEscenarioAbierto;
            juego.getLimites().get(5).setSize(10);
            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                juego.setScreen(new PantallaCuartoB(juego));
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
        juego.jugar(delta, batch, escenaJuego);
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
