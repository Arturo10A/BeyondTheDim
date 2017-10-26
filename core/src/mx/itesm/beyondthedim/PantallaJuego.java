package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
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

import java.util.ArrayList;

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
    ArrayList<Enemy> enemy_list = new ArrayList<Enemy>();




    private Stage escenaJuego;
    private Texture texturaBtnGoBack; //Boton de regreso

    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Texto
    private Texto texto;

    //Variable of control
    private float time;


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


    public PantallaJuego(Juego juego) {
        this.juego = juego;
    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_pause.png");

    }

    public void crearEscena(){

        escenaJuego = new Stage(vista);

        //bala
        bullets = new ArrayList<Bullet>();
        shootTimer=0;

        //Joysticks
        Skin skin = new Skin();
        skin.add("padFondo", new Texture("Joystick/joystick_fondo.png"));
        skin.add("padMovimiento",new Texture("Joystick/joystick_movimiento.png"));

        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("padFondo");
        estilo.knob = skin.getDrawable("padMovimiento");

        gunJoystick = new Touchpad(20, estilo);
        gunJoystick.setBounds(ANCHO-200,0,200,200);

        movJoystick = new Touchpad(20, estilo);
        movJoystick.setBounds(0, 0, 200, 200);
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                System.out.println("YUHU es"+ movJoystick.getKnobPercentX());

                //personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(), DY_PERSONAJE*pad.getKnobPercentY());

                //Right
                if (personaje.getPositionX() >= 1105.23 && movJoystick.getKnobPercentX() > 0){
                    System.out.println("Personaje Menor a 1105.23");
                    personaje.mover(-10, DY_PERSONAJE*pad.getKnobPercentY());
                }
                //Left
                else if (personaje.getPositionX() <= 116.42 && movJoystick.getKnobPercentX() < 0){
                    System.out.println("Personaje Mayor a 116.42");
                    personaje.mover(10, DY_PERSONAJE*pad.getKnobPercentY());
                }
                //TOP
                else if (personaje.getPositionY() >= 549.42 && movJoystick.getKnobPercentY() > 0){
                    System.out.println("Personaje Menor a 549.42");
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),-10);
                }
                //Bottom
                else if (personaje.getPositionY() <= 110.0 && movJoystick.getKnobPercentY() < 0){
                    System.out.println("Personaje Mayor a 110.23");
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(),10);
                }

                else {
                    personaje.mover(DX_PERSONAJE*pad.getKnobPercentX(), DY_PERSONAJE*pad.getKnobPercentY());
                }
            }
        });

        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1,1,1,0.7f);

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
        personaje = new Personaje(ANCHO/4,ALTO/2, 100000);

        //Class enemy test
        enemy_list.add(new Enemy(25, 80, 1, 0));


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

        /*
        time += Gdx.graphics.getDeltaTime();
        if (time >= 1){



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





            // If the life of jett is equal 0 youy return to LoseScreen.

            if (personaje.getLife() <= 0){
                System.out.println("You die");
                juego.setScreen(new LoseScreen(juego));
            }

            time = 0;

            //System.out.println("X: "+personaje.getPositionX()+" Y: "+ personaje.getPositionY());

        }*/

        for (Enemy ene: enemy_list) {
            ene.atack(personaje);
        }

        // SHOOTING LOGIC
        shootTimer+=delta;
        if(gunJoystick.getKnobPercentY() > gunJoystick.getKnobPercentX() &&
                (gunJoystick.getKnobPercentX() < 0.5f && gunJoystick.getKnobPercentX()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),gunJoystick.getKnobPercentX(),gunJoystick.getKnobPercentY()));
        }
        if(gunJoystick.getKnobPercentY() < -gunJoystick.getKnobPercentX() && (gunJoystick.getKnobPercentX() < 0.5f && gunJoystick.getKnobPercentX()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            //bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),0,-1));
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),gunJoystick.getKnobPercentX(),gunJoystick.getKnobPercentY()));
        }

        if(gunJoystick.getKnobPercentX() > gunJoystick.getKnobPercentY() && (gunJoystick.getKnobPercentY() < 0.5f && gunJoystick.getKnobPercentY()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            //bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),1,0));
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),gunJoystick.getKnobPercentX(),gunJoystick.getKnobPercentY()));
        }
        if(gunJoystick.getKnobPercentX() < -gunJoystick.getKnobPercentY() && (gunJoystick.getKnobPercentY() < 0.5f && gunJoystick.getKnobPercentY()> -0.5f) && shootTimer>=SWT){
            shootTimer=0;
            //bullets.add(new Bullet(personaje.getPositionX(),personaje.getPositionY(),-1,0));
            bullets.add(new Bullet(personaje.getPositionX(), personaje.getPositionY(),gunJoystick.getKnobPercentX(),gunJoystick.getKnobPercentY()));
        }
        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets){
            bullet.update(delta);
            if (bullet.removeB){
                bulletsRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsRemove);


        //If 10 second are alredy past we create a new enemy
        time_enemy += Gdx.graphics.getDeltaTime();


        //Drawing jett:::::::::SE cambio de posicion, si hay error al descomentar mover a donde esta ¨sin begin y end¨¨¨¨


        batch.begin();

        batch.draw(textureEcenario,Pantalla.ANCHO/2-textureEcenario.getWidth()/2, Pantalla.ALTO/2-textureEcenario.getHeight()/2);
        personaje.render(batch, Gdx.graphics.getDeltaTime());
        //¨¨¨¨

        //Drawing test of class enemy

        for (Enemy ene:enemy_list) {
            ene.render(batch);
        }


        lifeString = "Vida: "+personaje.getLife();

        //quitar comentario
        texto.mostrarMensaje(batch,lifeString,98,Pantalla.ALTO/1.03f);

        for (Bullet bullet: bullets){
            bullet.render(batch);
        }

        batch.end();

        //Check colision system



            for (int i = 0; i <= bullets.size()-1 ; i++) {
                for (int j = 0; j <= enemy_list.size()-1 ; j++) {

                   if (bullets.get(i).distance(enemy_list.get(j) ) < 15 ){
                       System.out.println("true");
                       enemy_list.get(j).resiveDamage();
                       bullets.remove(bullets.get(i));

                       if (enemy_list.get(j).isDead()){
                           enemy_list.remove(enemy_list.get(j));
                       }


                   }
                }
            }



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


}
