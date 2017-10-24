package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Arturo on 05/09/17.
 */

class PantallaJuego extends Pantalla{

    private final Juego juego;
    private Texture textureEcenario;  //Imagen del ecenario

    private float DX = 28;
    private int pasos = 20;
    private float timerPasos = 0;

    //Jett start
    private  Personaje personaje;

    //Jett Speed
    private int DX_PERSONAJE=10;
    private int DY_PERSONAJE =10;


    //Enemy block
    private Enemy enemy;
    private int DX_ENEMY = 100;
    private int DY_ENMEY = 100;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    ArrayList<Enemy> enemy_list = new ArrayList<Enemy>(20);




    private Stage escenaJuego;
    private Texture texturaBtnGoBack; //Boton de regreso

    //Joystick
    private Touchpad joystick;
    private Touchpad gunJoy;

    //Texto
    private Texto texto;

    //Variable of control
    private float time;


    //Timers to control enemys
    private float time_enemy;


    //Random

    private int numero;


    //BALA
    ArrayList<Bullet> bullets;
    public static final float SWT = 0.3f;
    float shootTimer;


    public PantallaJuego(Juego juego) {


        this.juego = juego;
    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("button_pause.png");

    }

    public void crearEscena(){

        escenaJuego = new Stage(vista);

        //bala
        bullets = new ArrayList<Bullet>();
        shootTimer=0;

        //Joystick
        Skin skin = new Skin();
        skin.add("padFondo", new Texture("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento",new Texture("Joystick/joystick_movimiento.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padFondo");
        estilo.knob = skin.getDrawable("padMovimiento");

        gunJoy = new Touchpad(20, estilo);
        gunJoy.setBounds(ANCHO-200,0,200,200);

        joystick = new Touchpad(20, estilo);
        joystick.setBounds(0, 0, 200, 200);
        joystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;

                //personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(), DY_PERSONAJE*pad.getKnobPercentY());

                //Right
                if (personaje.getPositionX() >= 1105.23 && joystick.getKnobPercentX() > 0){

                    personaje.mover(-10, DY_PERSONAJE*pad.getKnobPercentY());
                }
                //Left
                else if (personaje.getPositionX() <= 116.42 && joystick.getKnobPercentX() < 0){

                    personaje.mover(10, DY_PERSONAJE*pad.getKnobPercentY());
                }
                //TOP
                else if (personaje.getPositionY() >= 549.42 && joystick.getKnobPercentY() > 0){

                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),-10);
                }
                //Bottom
                else if (personaje.getPositionY() <= 110.0 && joystick.getKnobPercentY() < 0){
                    System.out.println("Personaje Mayor a 110.23");
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),10);
                }

                else {
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(), DY_PERSONAJE*pad.getKnobPercentY());
                }
            }
        });

        escenaJuego = new Stage(vista);
        escenaJuego.addActor(joystick);
        escenaJuego.addActor(gunJoy);
        joystick.setColor(1,1,1,0.7f);

        //Boton GOBACK -> check variable and conflic agins problems

        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaBtnGoBack));
        ImageButton btnGoBack = new ImageButton(trdGoBack);

        btnGoBack.setPosition(ANCHO-btnGoBack.getWidth()-5,ALTO-btnGoBack.getHeight()-5);

        escenaJuego.addActor(btnGoBack);

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

        cargarTexturas();
        crearEscena();

        textureEcenario = new Texture("Stage/fondo_nivel_uno.png");
        textureEcenario = new Texture("Stage/fondo_nivel_uno.png");
        personaje = new Personaje(ANCHO/4,ALTO/2, 100);

        //Class enemy test
        enemy_list.add(new Enemy(0, 0, 100, 20));
        enemy_list.add(new Enemy(20, 50, 100, 30));
        enemy_list.add(new Enemy(70, 90, 100, 40));
        enemy_list.add(new Enemy(180, 680,100, 50));
        enemy_list.add(new Enemy(600, 500,100, 100));


        Gdx.input.setInputProcessor(escenaJuego);
        //Gdx.input.setInputProcessor(new ProcesadorEventos());

        texto = new Texto();
    }

    @Override
    public void render(float delta) {



        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        //If time equals 10 move the enemy to the current location of the character
        //Start damage

        //HUD
        batch.setProjectionMatrix(camara.combined);
        escenaJuego.draw();
        batch.begin();
        batch.end();

        time += Gdx.graphics.getDeltaTime();
        if (time >= 1){

            /*enemy.atack(personaje);
            enemy1.atack(personaje);
            enemy2.atack(personaje);
            enemy3.atack(personaje);
            enemy4.atack(personaje);*/

            for (Enemy ene:
                 enemy_list) {
                ene.atack(personaje);
            }

            for (Enemy ene:
                    enemy_list) {
                ene.doDamage(personaje);
            }


            for (int i=0; i <= enemy_list.size()-1; i++){

                for (int j=0; j<= bullets.size()-1; j++){
                    enemy_list.get(i).resiveDamage(bullets.get(j));

                }

            }

            /*enemy.doDamage(personaje);
            enemy2.doDamage(personaje);
            enemy3.doDamage(personaje);
            enemy4.doDamage(personaje);*/



            // If the life of jett is equal 0 youy return to LoseScreen.

            if (personaje.getLife() <= 0){
                System.out.println("You die");
                juego.setScreen(new LoseScreen(juego));
            }

            time = 0;

            //System.out.println("X: "+personaje.getPositionX()+" Y: "+ personaje.getPositionY());

        }

        // SHOOTING LOGIC
        shootTimer+=delta;
        if(gunJoy.getKnobPercentY() > gunJoy.getKnobPercentX() && (gunJoy.getKnobPercentX() < 0.5f && gunJoy.getKnobPercentX()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),0,1));

        }
        if(gunJoy.getKnobPercentY() < -gunJoy.getKnobPercentX() && (gunJoy.getKnobPercentX() < 0.5f && gunJoy.getKnobPercentX()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),0,-1));
        }

        if(gunJoy.getKnobPercentX() > gunJoy.getKnobPercentY() && (gunJoy.getKnobPercentY() < 0.5f && gunJoy.getKnobPercentY()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),1,0));
        }
        if(gunJoy.getKnobPercentX() < -gunJoy.getKnobPercentY() && (gunJoy.getKnobPercentY() < 0.5f && gunJoy.getKnobPercentY()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),-1,0));
        }
        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets){
            bullet.update(delta);
            if (bullet.removeB){
                bulletsRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsRemove);

        if (bullets.isEmpty()){
            System.out.println("No hay balas");
        }else{
            //System.out.println("Hay balas");
        }


        //If 10 second are alredy past we create a new enemy
        time_enemy += Gdx.graphics.getDeltaTime();



        batch.begin();

        batch.draw(textureEcenario,Pantalla.ANCHO/2-textureEcenario.getWidth()/2, Pantalla.ALTO/2-textureEcenario.getHeight()/2);

        //Drawing jett
        personaje.render(batch);

        //Drawing test of class enemy

        for (Enemy ene:
                enemy_list) {
            ene.render(batch);
        }

        /*enemy.render(batch);
        enemy1.render(batch);
        enemy2.render(batch);
        enemy3.render(batch);
        enemy4.render(batch);*/

        texto.mostrarMensaje(batch,"Vida: " + personaje.getLife(),95,Pantalla.ALTO/1.07f);

        for (Bullet bullet: bullets){
            bullet.render(batch);
        }

        batch.end();

        batch.begin();
        escenaJuego.draw();
        batch.end();

        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();


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

    private class ProcesadorEventos implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {

            if (keycode == Input.Keys.LEFT){

                if (personaje.getPositionX() <= 145.f){

                }else{
                    personaje.mover(-DX_PERSONAJE,0);
                }

            }if (keycode == Input.Keys.RIGHT){

                if (personaje.getPositionX() >= 1070.0){

                }else{
                    personaje.mover(DX_PERSONAJE,0);
                }

            }if (keycode == Input.Keys.DOWN){

                if (personaje.getPositionY() <= 110.0){}else{
                    personaje.mover(0,-DY_PERSONAJE);
                }

            }if (keycode == Input.Keys.UP){

                if (personaje.getPositionY() >= 585.0){}
                else {
                    personaje.mover(0,DY_PERSONAJE);
                }

            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
