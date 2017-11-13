package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

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

class PantallaJuego extends Pantalla {
    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //
    private float DX = 28;
    private int pasos = 20;
    private float timerPasos = 0;
    private boolean nivelTerminado = false;

    //Jett start
    private Personaje personaje;
    private int vidaPersonaje = 1000;
    //Jett Speed
    private int DX_PERSONAJE = 5;
    private int DY_PERSONAJE = 5;
    //
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    //Enemy block
    private int damageEnemigo = 10;
    ArrayList<Enemy> enemy_list = new ArrayList<Enemy>();
    //Escenario
    private Stage escenaJuego;
    private EscenaPausa escenaPausa;
    private Texture texturaBtnPausa; //Boton de regreso

    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Texto
    private Texto texto;

    //Variable of control
    private float timeBala;


    //Timers to control enemys
    private float time_enemy;


    //Life string


    //BALA
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;

    private SpacialBox obj1;

    //Music
    private Music music;
    private Sound shoot;

    public PantallaJuego(Juego juego) {
        this.juego = juego;
    }

    public void cargarTexturas() {
        texturaBtnPausa = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.png");
        textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.png");
    }

    private void cargarMusica(){
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3"));
        music.setLooping(true);
        music.play();

        shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));
    }
    /*
    public void createEnemy(float delta) {

        if (delta >= 10.0) {
            System.out.println("Enemy created");
            delta = 0;
        }

    }*/

    public void crearEscena() {

        //Escenario
        escenaJuego = new Stage(vista);

        //Bala
        bullets = new ArrayList<Bullet>();
        shootTimer = 0;

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
        gunJoystick.setBounds(ANCHO - 200, 0, 200, 200);
        //Joystick movimiento
        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(0, 0, 200, 200);
        //Listener joystick movimiento
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                //Control de Sprites
                if (pad.getKnobPercentX() > 0.20) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
                } else if (pad.getKnobPercentX() < -0.20) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
                } else if (pad.getKnobPercentX() == 0) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
                }
                //Restricciones de movimiento(paredes)
                //Right
                if (personaje.getPositionX() >= 1120 && movJoystick.getKnobPercentX() > 0) {

                    personaje.mover(-1, DY_PERSONAJE * pad.getKnobPercentY());
                }
                //Left
                else if (personaje.getPositionX() <= 116.42 && movJoystick.getKnobPercentX() < 0) {

                    personaje.mover(10, DY_PERSONAJE * pad.getKnobPercentY());

                }
                //TOP
                else if (personaje.getPositionY() >= 549.42 && movJoystick.getKnobPercentY() > 0) {

                    personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), -10);
                }
                //Bottom
                else if (personaje.getPositionY() <= 110.0 && movJoystick.getKnobPercentY() < 0) {

                    personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), 10);
                } else {
                    personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), DY_PERSONAJE * pad.getKnobPercentY());
                }
            }
        });
        //
        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1, 1, 1, 0.7f);
        //*******************************************************Boton GOBACK -> check variable and conflic agins problems*******************************************************
        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(ANCHO - btnPausa.getWidth() - 5, ALTO - btnPausa.getHeight() - 5);
        escenaJuego.addActor(btnPausa);
        //Listener boton goBack
        btnPausa.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Se pausa el juego
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                if (estado==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vista, batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
            }

        });
    }

    @Override
    public void show() {
        //Cargar escena
        cargarTexturas();

        crearEscena();
        cargarMusica();
        //Crear personaje
        personaje = new Personaje(ANCHO / 4, ALTO / 2, vidaPersonaje);
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        enemy_list.add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
        enemy_list.add(new Enemy(ANCHO - 300, ALTO / 4, 100, 1));
        enemy_list.add(new Enemy(ANCHO - 50, ALTO / 2, 100, 1));
        //
        Gdx.input.setInputProcessor(escenaJuego);
        //Gdx.input.setInputProcessor(new ProcesadorEventos());

        texto = new Texto();
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);
        //If timeBala equals 10 move the enemy to the current location of the character
        //Start damage

        //HUD
        batch.setProjectionMatrix(camara.combined);
        escenaJuego.draw();
        //*******************************************************Dibujar Objetos*******************************************************
        batch.begin();

        dibujarObjetos();

        batch.end();
        //*******************************************************Dibujar escena del juego*******************************************************
        batch.begin();
        music.setVolume(0.2f);
        music.play();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
        //***************Enemigos***************
        if(estado==EstadoJuego.JUGANDO) {
            logicaEnemigo(delta);
            //***************Balas***************
            logicaDisparo(delta);
            //***************Colision Bala/Enemigo***************
            sistemaColisionesBala();
        }
        //*******************************************************Ganar/Perder*******************************************************
        //***************Perder***************
        if (personaje.getLife() <= 0) {
            music.stop();
            music.dispose();
            juego.setScreen(new LoseScreen(juego));
        }
        //***************Ganar***************
        if (enemy_list.isEmpty()){
            textureEscenario = textureEscenarioAbierto;

            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                music.stop();
                music.dispose();
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego));
            }
        }
        if(estado==EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }
    }

    private void dibujarObjetos() {
        batch.draw(textureEscenario, Pantalla.ANCHO / 2 - textureEscenario.getWidth() / 2, Pantalla.ALTO / 2 - textureEscenario.getHeight() / 2);
        player = createObject(personaje.getPositionX(),personaje.getPositionY());
        obj1 = new SpacialBox(world,"LOCO",personaje.getPositionX(),personaje.getPositionY());

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //Enemigos
        for (Enemy ene : enemy_list) {
            ene.render(batch, Gdx.graphics.getDeltaTime());
        }
        //Vida
        String lifeString = "Vida: " + personaje.getLife();
        texto.mostrarMensaje(batch, lifeString, 98, Pantalla.ALTO / 1.03f);
        //Balas
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }

    }

    private void sistemaColisionesBala() {
        //*******************************************************Check colision system*******************************************************
        //Empezar en -1 y terminar en 0
        for (int j = enemy_list.size() - 1; j >=0 ; j--) {
            for (int i = bullets.size() - 1; i>=0; i--) {
                if (bullets.get(i).distance(enemy_list.get(j)) < 15) {
                    enemy_list.get(j).receiveDamage(20);
                    enemy_list.get(j).goBack();
                    bullets.remove(i);

                    if (enemy_list.get(j).isDead()) {
                        enemy_list.remove(j);
                        break;
                    }
                }
            }
        }
        //Cambiar timeBala a timer
        if (timeBala >= 100.0) {
            for (int i = 0; i < bullets.size() - 4; i++) {
                bullets.remove(i);
            }
            timeBala = 0;
        } else {
            timeBala++;
        }
    }

    private void logicaDisparo(float delta) {
        //*******************************************************Logica Disparo*******************************************************
        shootTimer += delta;
        Vector2 v = new Vector2(gunJoystick.getKnobPercentX(), gunJoystick.getKnobPercentY());
        float angle = v.angle();

        if (((angle > 337.5 && angle < 360) || (angle > 0 && angle < 22.5)) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), 1, 0));
            shoot.play();
        }
        if ((angle < 337.5 && angle > 292.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), 1, -1));
            shoot.play();
        }
        if ((angle < 292.5 && angle > 247.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), 0, -1));
            shoot.play();
        }
        if ((angle < 247.5 && angle > 202.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), -1, -1));
            shoot.play();
        }
        if ((angle < 202.5 && angle > 157.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), -1, 0));
            shoot.play();
        }
        if ((angle < 157.5 && angle > 112.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), -1, 1));
            shoot.play();
        }
        if ((angle < 112.5 && angle > 67.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), 0, 1));
            shoot.play();
        }
        if ((angle < 67.5 && angle > 22.5) && shootTimer >= SWT) {
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(), 1, 1));
            shoot.play();
        }
/*
        //Modificar
        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.removeB) {
                bulletsRemove.add(bullet);
            }
        }*/
        //bullets.removeAll(bulletsRemove);
        for(int i = bullets.size()-1;i>=0;i--){
            bullets.get(i).update(delta);
            if(bullets.get(i).removeB){
                bullets.remove(i);
            }
        }
        Gdx.app.log("Logica balas", "Tamaño" + bullets.size());
    }


    private void logicaMovimiento(float delta) {
        //*******************************************************Logica enemigos*******************************************************{
        System.out.println("x: "+movJoystick.getKnobPercentX());
        System.out.println("y: "+movJoystick.getKnobPercentY());
        if(movJoystick.getKnobPercentX()!=0.000000000 && movJoystick.getKnobPercentY()!=0.000000000) {
            System.out.println("ENTRA");
            personaje.mover(DX_PERSONAJE * movJoystick.getKnobPercentX(), DY_PERSONAJE * movJoystick.getKnobPercentY());
        }
    }

    private void logicaEnemigo(float delta) {
        //*******************************************************Logica enemigos*******************************************************{
        float enemyPosAncho = 0;
        float enemyPosAlto = 0;
        for (Enemy ene : enemy_list) {
            ene.attack(personaje, enemyPosAncho, enemyPosAlto);
            enemyPosAncho += ene.sprite.getWidth() / 2;
            enemyPosAlto += ene.sprite.getHeight() / 2;
            ene.doDamage(this.personaje);
        }

        //createEnemy(delta);
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

    private class EscenaPausa extends Stage {

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            // Crear triángulo transparente
            //ESTo se tiene que cambiar!!!!!!!!!!!!!!!!!!!!
            Pixmap pixmap = new Pixmap((int) (ANCHO * 0.7f), (int) (ALTO * 0.8f), Pixmap.Format.RGBA8888);
            pixmap.setColor(255, 102, 102, 0.65f);
            pixmap.fillTriangle(0, pixmap.getHeight(), pixmap.getWidth(), pixmap.getHeight(), pixmap.getWidth() / 2, 0);
            Texture texturaTriangulo = new Texture(pixmap);
            pixmap.dispose();
            Image imgTriangulo = new Image(texturaTriangulo);
            imgTriangulo.setPosition(0.15f * ANCHO, 0.1f * ALTO);
            this.addActor(imgTriangulo);

            // Salir
            Texture texturaBtnSalir = new Texture("Objetos_varios/btnSalir.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO/2-btnSalir.getWidth()/2, ALTO*0.2f);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    music.stop();
                    juego.setScreen(new PantallaMenu(juego, null));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReintentar = new Texture("Botones/button_back_2.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReintentar));
            ImageButton btnReintentar = new ImageButton(trdReintentar);
            btnReintentar.setPosition(ANCHO/2-btnReintentar.getWidth()/2, ALTO*0.5f);
            btnReintentar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estado = EstadoJuego.JUGANDO;
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(escenaJuego);
                }
            });
            this.addActor(btnReintentar);

        }



    }


}
