package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.audio.Music;

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

class PantallaJuego extends Pantalla{
    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;
    //
    private float DX = 28;
    private int pasos = 20;
    private float timerPasos = 0;

    //Jett start
    private  Personaje personaje;
    private int vidaPersonaje = 1000;
    //Jett Speed
    private int DX_PERSONAJE=5;
    private int DY_PERSONAJE =5;


    //Enemy block
    private Enemy enemy;
    private int DX_ENEMY = 100;
    private int DY_ENMEY = 100;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private int damageEnemigo = 10;
    ArrayList<Enemy> enemy_list = new ArrayList<Enemy>();
    //Escenario
    private Stage escenaJuego;
    private Texture texturaBtnGoBack; //Boton de regreso

    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Texto
    private Texto texto;

    //Variable of control
    private float timeBala;


    //Timers to control enemys
    private float time_enemy;


    //Random
    private int numero;

    //Life string

    private String lifeString;


    //BALA
    ArrayList<Bullet> bullets;
    public static final float SWT = 0.3f;
    float shootTimer;


    //Music
    Music music = Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3"));
    Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));

    public PantallaJuego(Juego juego) {
        this.juego = juego;
    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.png");
        //textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.png");
    }

    public void crearEscena(){

        //Escenario
        escenaJuego = new Stage(vista);

        //Bala
        bullets = new ArrayList<Bullet>();
        shootTimer=0;

        //*******************************************************Joysticks*******************************************************
        //Texturas
        Skin skin = new Skin();
        skin.add("padFondo", new Texture("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento",new Texture("Joystick/joystick_movimiento.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padFondo");
        estilo.knob = skin.getDrawable("padMovimiento");
        //Joystick pistola
        gunJoystick = new Touchpad(20, estilo);
        gunJoystick.setBounds(ANCHO-200,0,200,200);
        //Joystick movimiento
        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(0, 0, 200, 200);
        //Listener joystick movimiento
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                //Control de Sprites
                if(pad.getKnobPercentX()>0.20) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
                }else if(pad.getKnobPercentX()<-0.20){
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
                }else if(pad.getKnobPercentX()==0){
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
                }
                //Restricciones de movimiento(paredes)
                //Right
                if (personaje.getPositionX() >= 1105.23 && movJoystick.getKnobPercentX() > 0){

                    personaje.mover(-10, DY_PERSONAJE*pad.getKnobPercentY());
                }
                //Left
                else if (personaje.getPositionX() <= 116.42 && movJoystick.getKnobPercentX() < 0){

                    personaje.mover(10, DY_PERSONAJE*pad.getKnobPercentY());

                }
                //TOP
                else if (personaje.getPositionY() >= 549.42 && movJoystick.getKnobPercentY() > 0){

                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),-10);
                }
                //Bottom
                else if (personaje.getPositionY() <= 110.0 && movJoystick.getKnobPercentY() < 0){

                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),10);
                }

                else {
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(), DY_PERSONAJE*pad.getKnobPercentY());
                }
            }
        });
        //
        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1,1,1,0.7f);
        //*******************************************************Boton GOBACK -> check variable and conflic agins problems*******************************************************
        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaBtnGoBack));
        ImageButton btnGoBack = new ImageButton(trdGoBack);
        btnGoBack.setPosition(ANCHO-btnGoBack.getWidth()-5,ALTO-btnGoBack.getHeight()-5);
        escenaJuego.addActor(btnGoBack);
        //Listener boton goBack
        btnGoBack.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaMenu(juego));
            }

        });
    }

    @Override
    public void show() {
        //Cargar escena
        cargarTexturas();
        crearEscena();
        //Crear personaje
        personaje = new Personaje(ANCHO/4,ALTO/2, vidaPersonaje);
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        enemy_list.add(new Enemy(ANCHO-200, ALTO/2, 100, damageEnemigo));
        //
        Gdx.input.setInputProcessor(escenaJuego);
        //Gdx.input.setInputProcessor(new ProcesadorEventos());

        texto = new Texto();
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        //If timeBala equals 10 move the enemy to the current location of the character
        //Start damage

        //HUD
        batch.setProjectionMatrix(camara.combined);
        escenaJuego.draw();
        //*******************************************************Dibujar Objetos*******************************************************
        batch.begin();
        batch.draw(textureEscenario,Pantalla.ANCHO/2- textureEscenario.getWidth()/2, Pantalla.ALTO/2- textureEscenario.getHeight()/2);
        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //Enemigos
        for (Enemy ene:enemy_list) {
            ene.render(batch);
        }
        //Vida
        lifeString = "Vida: "+personaje.getLife();
        texto.mostrarMensaje(batch,lifeString,98,Pantalla.ALTO/1.03f);
        //Balas
        for (Bullet bullet: bullets){
            bullet.render(batch);
        }

        batch.end();
        //*******************************************************Dibujar escena del juego*******************************************************
        batch.begin();
        music.play();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
        //*******************************************************Logica enemigos*******************************************************{
        for (Enemy ene : enemy_list) {
            ene.atack(personaje);
        }
        for (Enemy ene : enemy_list) {
            ene.doDamage(this.personaje);
        }
        //*******************************************************Logica Disparo*******************************************************
        shootTimer += delta;
        Vector2 v   = new Vector2(gunJoystick.getKnobPercentX(), gunJoystick.getKnobPercentY());
        float angle = v.angle();

        if(((angle > 337.5 && angle < 360) || (angle>0 && angle < 22.5)) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),1,0));
            shoot.play();
        }
        if((angle < 337.5 && angle > 292.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),1,-1));
            shoot.play();
        }
        if((angle < 292.5 && angle > 247.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),0,-1));
            shoot.play();
        }
        if((angle < 247.5 && angle > 202.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),-1,-1));
            shoot.play();
        }
        if((angle < 202.5 && angle > 157.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),-1,0));
            shoot.play();
        }
        if((angle < 157.5 && angle > 112.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),-1,1));
            shoot.play();
        }
        if((angle < 112.5 && angle > 67.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),0,1));
            shoot.play();
        }
        if((angle < 67.5 && angle > 22.5) && shootTimer>=SWT){
            shootTimer = 0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),1,1));
            shoot.play();
        }


        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets){
            bullet.update(delta);
            if (bullet.removeB){
                bulletsRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsRemove);
        //*******************************************************Check colision system*******************************************************

        for (int i = 0; i <= bullets.size()-1 ; i++) {
            for (int j = 0; j <= enemy_list.size()-1 ; j++) {
                if (bullets.get(i).distance(enemy_list.get(j) ) < 15 ){
                    System.out.println("true");
                    enemy_list.get(j).receiveDamage(20);
                    enemy_list.get(j).goBack();
                    bullets.remove(bullets.get(i));

                    if (enemy_list.get(j).isDead()){
                        enemy_list.remove(enemy_list.get(j));
                    }
                }
            }
        }

        if(timeBala >= 100.0){
            for (int i = 0; i < bullets.size()-4; i++) {
                bullets.remove(i);
            }

            System.out.println("****borrado****");
            System.out.println(timeBala);
            timeBala = 0;

        }else {
            timeBala++;
            System.out.println(timeBala);
        }
        //*******************************************************Ganar/Perder*******************************************************
        //Perder
        if (personaje.getLife() <= 0) {
            music.stop();
            music.dispose();
            juego.setScreen(new LoseScreen(juego));
        }
        //Ganar
        if(enemy_list.isEmpty()){
            textureEscenario = new Texture("Stage/fondo_nivel_uno_abierto.png");

        }
        if(personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320){
            juego.setScreen(new PantallaMenu(juego));
        }

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

    }


}
