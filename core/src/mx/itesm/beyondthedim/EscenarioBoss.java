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
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Arturo on 03/11/17.
 */

public class EscenarioBoss extends  Pantalla {

    private final Juego juego;

    //Jett start
    private  Personaje personaje;
    private int vidaPersonaje = 1000;
    //Jett Speed
    private int DX_PERSONAJE=5;
    private int DY_PERSONAJE =5;
    //Arreglo de balas
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private float timeBala;

    //BOSS
    private boss boss;
    float Bosstimer = 0;
    double bossShot = 100;
    double bossShotFollow = 0.0;
    //Arreglo balas de Jefe
    private ArrayList<Bullet> BossBullets;

    //Escenario y Texturas
    private Texture texturaBtnGoBack;
    private Texture textureEscenario;
    private Stage escenaJuego;
    //Escena de Pausa

    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;

    //Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;







    private Texto texto;

    private Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));


    public EscenarioBoss(Juego juego){this.juego = juego;}

    private void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.png");
    }

    private void crearEscena(){
        escenaJuego = new Stage(vista);


        bullets = new ArrayList<Bullet>();
        shootTimer=0;
        BossBullets = new ArrayList<Bullet>();

        logicaJoystick();

        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnGoBack));
        ImageButton btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(ANCHO-btnPausa.getWidth()-5,ALTO-btnPausa.getHeight()-5);
        escenaJuego.addActor(btnPausa);


        btnPausa.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Se pausa el juego


                // Activar escenaPausa y pasarle el control
                if (escenaPausa == null){
                    escenaPausa = new EscenaPausa(vista,batch);
                }
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;



            }

        });
    }

    private void logicaJoystick() {
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
    }

    @Override
    public void show() {

        cargarTexturas();
        crearEscena();

        personaje = new Personaje(ANCHO/4,ALTO/2, vidaPersonaje);
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Gdx.input.setInputProcessor(escenaJuego);

        boss = new boss(ANCHO/2,ALTO/2,100);


        texto = new Texto();
    }

    private void dibujarEscena() {
        batch.begin();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
    }

    private void dibujarObjetos() {
        batch.begin();
        batch.draw(textureEscenario, Pantalla.ANCHO/2- textureEscenario.getWidth()/2, Pantalla.ALTO/2- textureEscenario.getHeight()/2);

        if (boss.getLife() > 0){
            boss.dibujar(batch);
        }

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //Vida

        if (personaje.getLife() > 0) {
            String lifeString = "Vida: " + personaje.getLife();
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
        }else {
            String lifeString = "vida 0";
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
            juego.setScreen(new LoseScreen(juego));
        }


        //Balas
        for (Bullet bullet: bullets){
            bullet.render(batch);
        }

        //Balas de jefe
        for (Bullet bullet: BossBullets){
            bullet.render(batch);
        }

        batch.end();
    }

    private void logicaDisparo(float delta) {
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
    }

    private void bossSpecialAtack(float delta, Personaje target, boss boss){

        Bosstimer += delta;
        bossShotFollow += delta;


        if (boss.getLife() < 80){
            bossShot = 3.0;
        }
        else if(boss.getLife() < 60){
            bossShot = 2.0;
        }
        else if(boss.getLife() < 30){
            bossShot = 1.0;
        }

        else if(boss.getLife() < 10){
            bossShot = 0.1;
        }

        if (Bosstimer >= bossShot) {
            Bosstimer = 0;
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, 0));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, -1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 0, -1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, -1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, 0));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, 1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 0, 1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 0, 1));
            BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, 1));
        }


        //Diparar al objetivo mientras lo sigue
        if (bossShotFollow > 0.2){
            bossShotFollow = 0;
            if (boss.getPositionX() < personaje.getPositionX() && boss.getPositionY() < personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, 1));
            }
            if (boss.getPositionX() > personaje.getPositionX() && boss.getPositionY() > personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, -1));
            }
            if (boss.getPositionX() > personaje.getPositionX() && boss.getPositionY() < personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, 1));
            }
            if (boss.getPositionX() < personaje.getPositionX() && boss.getPositionY() > personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, -1));
            }

            if (boss.getPositionX() > personaje.getPositionX() && boss.getPositionY() == personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), -1, 0));
            }

            if (boss.getPositionX() < personaje.getPositionX() && boss.getPositionY() == personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 1, 0));
            }
            if (boss.getPositionX() == personaje.getPositionX() && boss.getPositionY() > personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 0, -1));
            }
            if (boss.getPositionX() == personaje.getPositionX() && boss.getPositionY() < personaje.getPositionY()) {
                BossBullets.add(new Bullet(boss.getPositionX(), boss.getPositionY(), 0, 1));
            }
        }
        ArrayList<Bullet> BoosbulletsRemove = new ArrayList<Bullet>();

        for(Bullet bullet : BossBullets){
            bullet.update(delta);
            if (bullet.removeB){
                BoosbulletsRemove.add(bullet);
            }
        }
        BossBullets.removeAll(BoosbulletsRemove);

        for (int i = 0; i < BossBullets.size(); i++) {
            if (BossBullets.get(i).distanceJett(personaje) < 25){
                System.out.println("** JETT EN PROBLEMAS **");
                personaje.damage(1);
                BossBullets.remove(i);
            }
        }
    }

    private void collisiones(boss Boss){


        for (int bala = 0; bala < bullets.size(); bala++) {
            if (bullets.get(bala).distanceBoss(Boss) < 100){
                Boss.Damege(1);
                bullets.remove(bala);
            }
        }


    }


    @Override
    public void render(float delta) {


        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        //HUD
        batch.setProjectionMatrix(camara.combined);

        escenaJuego.draw();

        if (estado == EstadoJuego.JUGANDO){

            //boss.teleport(personaje);
            if (boss.getLife() > 0){

                boss.atack(personaje);
                for (Bullet bullet: bullets) {
                    boss.isUnderAtack(bullet,personaje);
                }
                collisiones(boss);
                bossSpecialAtack(delta, personaje, boss);

            }
            else {
                BossBullets.clear();
            }


            dibujarObjetos();
            dibujarEscena();
            logicaDisparo(delta);


            if(timeBala >= 100.0){
                for (int i = 0; i < bullets.size()-4; i++) {
                    bullets.remove(i);
                }
                timeBala = 0;
            }else {
                timeBala++;
            }
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
