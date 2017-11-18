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
 * Created by Arturo on 15/11/17.
 */

public class PantallaTutorial extends  Pantalla {

    private final Juego juego;
    //Jett start
    private  Personaje personaje;
    //Jett Speed
    private int DX_PERSONAJE = 5;
    private int DY_PERSONAJE = 5;
    //Arreglo de balas
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private float timeBala;
    //Escenario y Texturas
    private Texture texturaBtnGoBack;
    private Texture texturaBtnSkip;
    private Texture textureEscenario;
    private Stage escenaJuego;
    //Escena de Pausa
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;
    //Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    //Texto a mostrar
    private Texto texto;
    //Sonidos
    private Sound shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));

    //Icono de vida
    private Texture vidaIcono;


    //Cuadros de dialogo
    private Texture cuadro1;
    private Texture cuadro2;
    private Texture cuadro3;
    private Texture cuadro4;
    private Texture cuadro5;
    private Texture cuadro6;
    //Timer dialogos
    private int timerDialogo;


    public PantallaTutorial(Juego juego){
        this.juego = juego;
    }

    private void cargarTexturas(){
        texturaBtnGoBack = new Texture("Botones/button_pause.png");
        texturaBtnSkip = new Texture("Botones/button_back_2.png");
        textureEscenario = new Texture("Stage/tutorial.jpg");
        vidaIcono = new Texture("iconLife.png");
        cuadro1 = new Texture("test.png");
        cuadro2 = new Texture("test2.png");
        cuadro3 = new Texture("test3.png");
        cuadro4 = new Texture("test4.png");
        cuadro5 = new Texture("test5.png");
        cuadro6 = new Texture("test6.png");
    }

    private void crearEscena(){
        escenaJuego = new Stage(vista);
        bullets = new ArrayList<Bullet>();
        shootTimer=0;
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

        TextureRegionDrawable trdSkip = new TextureRegionDrawable(new TextureRegion(texturaBtnSkip));
        ImageButton btnSkip = new ImageButton(trdSkip);
        btnSkip.setPosition(ANCHO-btnSkip.getWidth(),ALTO-btnSkip.getHeight());
        btnSkip.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event,x,y);
                juego.setScreen(new PantallaCuartoA(juego));
                dispose();
            }
        });
        escenaJuego.addActor(btnSkip);
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

        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1,1,1,0.7f);
    }

    @Override
    public void show() {
        cargarTexturas();
        crearEscena();

        personaje = new Personaje(ANCHO / 4, ALTO / 2, 1000);
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);

        Gdx.input.setInputProcessor(escenaJuego);
        personaje = new Personaje(ANCHO / 4, ALTO / 2, 1000);
        texto = new Texto();
    }

    private void dibujarEscena() {
        batch.begin();
        escenaJuego.draw();
        personaje.dibujar(batch);

        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());

        escenaJuego.draw();


    }

    private void dibujarObjetos() {
        batch.begin();
        batch.draw(textureEscenario, Pantalla.ANCHO/2- textureEscenario.getWidth()/2, Pantalla.ALTO/2- textureEscenario.getHeight()/2);



        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //Vida
        if (personaje.getLife() > 0) {
            String lifeString = "" + personaje.getLife();
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
        }else {
            String lifeString = "0";
            texto.mostrarMensaje(batch, lifeString,98,Pantalla.ALTO/1.03f);
            juego.setScreen(new PantallaPerder(juego));
        }
        //Balas
        for (Bullet bullet: bullets){
            bullet.render(batch);
        }

        batch.draw(vidaIcono,20,Pantalla.ALTO-vidaIcono.getHeight());

        batch.draw(cuadro1,Pantalla.ANCHO/2- cuadro1.getWidth()/2,Pantalla.ALTO - cuadro1.getHeight());

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




    @Override
    public void render(float delta) {


        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        //HUD
        batch.setProjectionMatrix(camara.combined);

        escenaJuego.draw();

        timerDialogo += Gdx.graphics.getDeltaTime();

        timerDialogo += 1;
        if (timerDialogo >= 120 && timerDialogo < 300){
            cuadro1 = cuadro2;
        }

        if (timerDialogo >= 300 && timerDialogo < 400){
            cuadro1 = cuadro3;
        }

        if (timerDialogo >= 400 && timerDialogo < 500 && personaje.getEstadoMovimiento() != Objeto.EstadoMovimiento.QUIETO){
            cuadro1 = cuadro4;
        }

        if (timerDialogo >= 500 && timerDialogo < 600 &&bullets.size() > 1){
            cuadro1 = cuadro5;
        }

        if(timerDialogo >= 600 && timerDialogo < 2000){
            cuadro1 = cuadro6;
        }

        if (timerDialogo >= 1000){

            juego.setScreen(new PantallaCuartoA(juego));


        }

        if (estado == EstadoJuego.JUGANDO){

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
            Texture texturaBtnSalir = new Texture("Botones/button_pause.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO/2-btnSalir.getWidth()/2, ALTO*0.2f);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    juego.setScreen(new PantallaCuartoA(juego));
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
