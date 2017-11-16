package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
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
    private Personaje obstacle;
    private ShapeRenderer shape;
    private ShapeRenderer shape2;
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

    //Historia
    private Texture texturaItemHistoria;

    //BALA
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private boolean cambiarDireccion = true;

    private Texture cuadroDialogo;
    private Texture cuadroDialogo2;


    //Music
    private Music music;
    private Sound shoot;

    public PantallaJuego(Juego juego, Personaje personaje) {
        this.juego = juego;
        this.personaje = personaje;
    }

    public PantallaJuego(Juego juego){
        this.juego = juego;
    }

    public void cargarTexturas() {
        texturaBtnPausa = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.jpg");
        textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.jpg");
        texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        cuadroDialogo = new Texture("test.png");
        cuadroDialogo2 = new Texture ("test2.png");
    }

    private void cargarMusica(){
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3"));
        music.setLooping(true);
        music.play();
        shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));
    }

    public void crearEscena() {

        //Escenario
        escenaJuego = new Stage(vista);

        //Bala
        bullets = new ArrayList<Bullet>();
        shootTimer = 0;

        shape = new ShapeRenderer();

        shape2 = new ShapeRenderer();

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
        movJoystick.moveBy(10,20);
        //Listener joystick movimiento
//        movJoystick.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Touchpad pad = (Touchpad) actor;
//
//                //Control de Sprites
//                if(cambiarDireccion) {
//                    if (pad.getKnobPercentX() > 0.20) {
//                        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
//                    } else if (pad.getKnobPercentX() < -0.20) {
//                        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
//                    } else if (pad.getKnobPercentX() == 0) {
//                        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
//                    }
//                }
//                //Restricciones de movimiento(paredes)
//                //Right
//                if (((personaje.getPositionX() >= 1120 && personaje.getPositionY() > 400) && movJoystick.getKnobPercentX() > 0)) {
//
//                    personaje.mover(-2, DY_PERSONAJE * pad.getKnobPercentY());
//                }
//                if ((personaje.getPositionX() >= 1120 && personaje.getPositionY() > 400) && movJoystick.getKnobPercentX() > 0) {
//
//                    personaje.mover(-1, DY_PERSONAJE * pad.getKnobPercentY());
//                }
//                //Left
//                else if (personaje.getPositionX() <= 116.42 && movJoystick.getKnobPercentX() < 0) {
//
//                    personaje.mover(10, DY_PERSONAJE * pad.getKnobPercentY());
//
//                }
//                //TOP
//                else if (personaje.getPositionY() >= 549.42 && movJoystick.getKnobPercentY() > 0) {
//
//                    personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), -10);
//                }
//                //Bottom
//                else if (personaje.getPositionY() <= 110.0 && movJoystick.getKnobPercentY() < 0) {
//
//                    personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), 10);
//                } else {
//
//                    Rectangle rp = personaje.getSprite().getBoundingRectangle();
//
//
//                    Rectangle ro = obstacle.getSprite().getBoundingRectangle();
//
//                    Gdx.app.log("Choque",rp.toString()+","+ro.toString());
//                    rp.setX(rp.getX()+10);
//                    if(! rp.overlaps(ro)){
//                        Gdx.app.log("CHOQUE", "SI PUEDE CAMINAR");
//                        personaje.mover(DX_PERSONAJE * pad.getKnobPercentX(), DY_PERSONAJE * pad.getKnobPercentY());
//                    } else{
//                        Gdx.app.log("Choque ","NO SE PUEDE");
//                    }
//
//
//
//                }
//            }
//        });
        //
        escenaJuego = new Stage(vista);
        escenaJuego.addActor(movJoystick);
        escenaJuego.addActor(gunJoystick);
        movJoystick.setColor(1, 1, 1, 0.7f);
        //*******************************************************Boton Pausa -> check variable and conflic agins problems*******************************************************
        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(ANCHO - btnPausa.getWidth() - 5, ALTO - btnPausa.getHeight() - 5);
        escenaJuego.addActor(btnPausa);
        //Listener boton pausa
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

        obstacle = new Personaje(ANCHO / 2+100, ALTO / 2, vidaPersonaje);
        //personaje.sprite.getBoundingRectangle(
        personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        //Añadir enemigo
        enemy_list.add(new Enemy(ANCHO - 200, ALTO / 2, 100, 1));
       // enemy_list.add(new Enemy(ANCHO - 300, ALTO / 4, 100, 1));
       // enemy_list.add(new Enemy(ANCHO - 50, ALTO / 2, 100, 1));
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
            batch.begin();
            logicaMovimiento(delta);

            batch.end();
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

        System.out.println(Gdx.graphics.getDeltaTime());

        if (Gdx.graphics.getDeltaTime() >= 10){
            cuadroDialogo = cuadroDialogo2;
        }





        //***************Ganar***************
        if (enemy_list.isEmpty()){
            textureEscenario = textureEscenarioAbierto;

            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                music.stop();
                music.dispose();
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego,personaje));
            }
        }
        if(estado==EstadoJuego.PAUSADO){
            escenaPausa.draw();
        }
    }

    private void dibujarObjetos() {
        batch.draw(textureEscenario, Pantalla.ANCHO / 2 - textureEscenario.getWidth() / 2, Pantalla.ALTO / 2 - textureEscenario.getHeight() / 2);

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());
        //System.out.println("Sprite Jett en X: "+ personaje.sprite.getBoundingRectangle().getX());
        //System.out.println("Sprite Jett en Y: "+ personaje.sprite.getBoundingRectangle().getY());
        obstacle.dibujar(batch, Gdx.graphics.getDeltaTime());

        batch.draw(cuadroDialogo,Pantalla.ANCHO/2- cuadroDialogo.getWidth()/2,Pantalla.ALTO - cuadroDialogo.getHeight());




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
        //Items
        batch.draw(texturaItemHistoria, ANCHO*0.80f, ALTO*0.17f,
                texturaItemHistoria.getWidth()*0.20f,texturaItemHistoria.getHeight()*0.20f);

    }

    private void sistemaColisionesBala() {
        //*******************************************************Check colision system*******************************************************
        //Empezar en -1 y terminar en 0
        for (int j = enemy_list.size() - 1; j >=0 ; j--) {
            for (int i = bullets.size() - 1; i>=0; i--) {
                if (bullets.get(i).distance(enemy_list.get(j)) < 50) {
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
        shootTimer += delta;
        //Disparo derecha
        if(gunJoystick.getKnobPercentX() > 0.50 && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX()+17, personaje.getPositionY()+28,1,gunJoystick.getKnobPercentY()));
            shoot.play();
        }
        if(gunJoystick.getKnobPercentX() < -0.50 && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX()+17, personaje.getPositionY()+28,-1,gunJoystick.getKnobPercentY()));
            shoot.play();
        }
        if(gunJoystick.getKnobPercentY() > 0.50 && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX()+17, personaje.getPositionY()+28,gunJoystick.getKnobPercentX(),1));
            shoot.play();
        }
        if(gunJoystick.getKnobPercentY() < -0.50 && shootTimer>=SWT){
            shootTimer=0;
            bullets.add(new Bullet(personaje.getPositionX()+17, personaje.getPositionY()+28,gunJoystick.getKnobPercentX(),-1));
            shoot.play();
        }
        if(gunJoystick.getKnobPercentY() == 0 && gunJoystick.getKnobPercentX()==0){
            cambiarDireccion = true;
        }else{
            cambiarDireccion = false;
            if(personaje.getEstadoMovimiento()!= Objeto.EstadoMovimiento.QUIETO) {
                if (gunJoystick.getKnobPercentX() > 0.20) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
                } else if (gunJoystick.getKnobPercentX() < -0.20) {
                    personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
                }
            }
        }

        //bullets.removeAll(bulletsRemove);
        for(int i = bullets.size()-1;i>=0;i--){
            bullets.get(i).update(delta);
            if(bullets.get(i).removeB){
                bullets.remove(i);
            }
        }
        //Gdx.app.log("Logica balas", "Tamaño" + bullets.size());
    }


    private void logicaMovimiento(float delta) {
        Rectangle rp = personaje.getSprite().getBoundingRectangle();
        Rectangle ro = obstacle.getSprite().getBoundingRectangle();

        shape.setProjectionMatrix(camara.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);

        Vector2 v = new Vector2(movJoystick.getKnobPercentX(), movJoystick.getKnobPercentY());

        float ang = v.angle();
        double angle = ang*Math.PI/180.0;
        //*******************************************************Logica enemigos*******************************************************{
        if(movJoystick.getKnobPercentX()!=0.000 && movJoystick.getKnobPercentY()!=0.000) {

            Gdx.app.log("Choque",rp.toString()+","+ro.toString());
            rp.setX(personaje.getPositionX()+ (float)(Math.cos(angle)*10));
            rp.setY(personaje.getPositionY()+ (float)(Math.sin(angle)*10));

            shape.rect(personaje.getPositionX()+ (float)(Math.cos(angle)*10), personaje.getPositionY()+ (float)(Math.sin(angle)*10),40,70);

            System.out.println("rp x = "+ rp.getX()+" y"+ rp.getY());
            System.out.println("ro x = "+ ro.getX()+" y"+ ro.getY());
            if(! rp.overlaps(ro)){
                Gdx.app.log("CHOQUE", "SI PUEDE CAMINAR");
                personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
            } else{
                Gdx.app.log("Choque ","NO SE PUEDE");
            }

        }

        shape.end();
        shape2.end();
    }

    private void logicaEnemigo(float delta) {
        float enemyPosAncho = 0;
        float enemyPosAlto = 0;
        for (Enemy ene : enemy_list) {
            ene.attack(personaje, enemyPosAncho, enemyPosAlto);
            enemyPosAncho += ene.sprite.getWidth() / 2;
            enemyPosAlto += ene.sprite.getHeight() / 2;
            ene.doDamage(this.personaje);
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
            super(vista,batch);
            Pixmap pixmap = new Pixmap((int) (ANCHO), (int) (ALTO), Pixmap.Format.RGBA8888);
            pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.4f );
            pixmap.fillRectangle(0, 0,pixmap.getWidth(),pixmap.getHeight());
            Texture texturaRectangulo = new Texture(pixmap);
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(0, 0);
            this.addActor(imgRectangulo);

            // Salir
            Texture texturaBtnSalir = new Texture("Botones/button_inicio.png");
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
