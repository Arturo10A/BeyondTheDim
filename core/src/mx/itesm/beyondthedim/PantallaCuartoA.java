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
import com.badlogic.gdx.math.collision.BoundingBox;
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

class PantallaCuartoA extends Pantalla {

    //Imagen del ecenario
    private final Juego juego;
    private Texture textureEscenario;
    private Texture textureEscenarioAbierto;

    //Jett start
    private Personaje personaje;
    private Personaje obstacle;
    private ShapeRenderer shape;
    private ShapeRenderer shape2;
    //
    private int vidaPersonaje = 1000;
    //Enemy block
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
    //Historia
    private Texture texturaItemHistoria;
    //BALA
    private ArrayList<Bullet> bullets;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private boolean cambiarDireccion = true;
    //Music
    //private Music music;
    private Sound shoot;

    //Constructores
    public PantallaCuartoA(Juego juego) {
        this.juego = juego;
        this.personaje = juego.getPersonaje();
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
        //Listener joystick movimiento
        movJoystick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad) actor;
                //Control de Sprites
                juego.controlJoystickMovimiento(batch, pad, movJoystick, cambiarDireccion, obstacle);
            }
        });
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
                //estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                juego.setEstadoJuego(EstadoJuego.PAUSADO);
                if (juego.getEstadoJuego()==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vista, batch, juego, escenaJuego);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
            }

        });
    }

    //********************Cargar*******************
    public void cargarTexturas() {
        texturaBtnPausa = new Texture("Botones/button_pause.png");
        textureEscenario = new Texture("Stage/fondo_nivel_uno_cerrado.jpg");
        textureEscenarioAbierto = new Texture("Stage/fondo_nivel_uno_abierto.jpg");
        texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
    }
    private void cargarMusica(){
        juego.setMusic(Gdx.audio.newMusic(Gdx.files.internal("Music/bensound-extremeaction.mp3")));
        juego.getMusic().setLooping(true);

        shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));
    }
    @Override
    public void show() {
        //Cargar escena
        cargarTexturas();
        crearEscena();
        cargarMusica();
        if(juego.musicOn){
            juego.getMusic().setVolume(0.2f);
            juego.getMusic().play();
        }
        //Crear personaje


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
    //***********************************************Metodos de render**********************************************
    private void dibujarObjetos() {
        batch.draw(textureEscenario, Pantalla.ANCHO / 2 - textureEscenario.getWidth() / 2, Pantalla.ALTO / 2 - textureEscenario.getHeight() / 2);

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());

        System.out.println("Sprite Jett en X: "+ personaje.sprite.getBoundingRectangle().getX());
        System.out.println("Sprite Jett en Y: "+ personaje.sprite.getBoundingRectangle().getY());

        obstacle.dibujar(batch, Gdx.graphics.getDeltaTime());

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
    //
    private void sistemaColisionesBala() {
        //**************************************Check colision system***************************************
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
    //
    private void logicaDisparo(float delta) {
        //****************************************Logica Disparo*****************************************
        shootTimer += delta;
        //Disparo derecha
        //System.out.println(gunJoystick.getKnobPercentY());

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

        for(int i = bullets.size()-1;i>=0;i--){
            bullets.get(i).update(delta);
            if(bullets.get(i).removeB){
                bullets.remove(i);
            }
        }
    }
    //
    private void logicaMovimiento(float delta) {
        Rectangle rp = personaje.getSprite().getBoundingRectangle();
        rp.setX(personaje.getPositionX()+17);
        rp.setY(personaje.getPositionY());
        rp.setWidth(30);
        rp.setHeight(20);

        //Boundings
        Rectangle ro = obstacle.getSprite().getBoundingRectangle();
        Rectangle muroN = new Rectangle(0,ALTO-120,ANCHO,120);
        Rectangle muroO = new Rectangle(0,0,120,ALTO);
        Rectangle muroS = new Rectangle(0,0,ANCHO,120);
        Rectangle muroEN = new Rectangle(1160,ALTO-300,120,300);
        Rectangle muroES = new Rectangle(1160,0,120,300);
        Rectangle puertaE = new Rectangle(1160,300,120,120);

        shape.setProjectionMatrix(camara.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);
        shape.rect(rp.getX(),rp.getY(),rp.getWidth(),rp.getHeight());

        shape2.setProjectionMatrix(camara.combined);
        shape2.begin(ShapeRenderer.ShapeType.Line);
        shape2.setColor(Color.BLUE);
        shape2.rect(puertaE.getX(),puertaE.getY(),puertaE.getWidth(),puertaE.getHeight());


        Vector2 v = new Vector2(movJoystick.getKnobPercentX(), movJoystick.getKnobPercentY());
        float ang = v.angle();
        double angle = ang*Math.PI/180.0;
        //*******************************************Logica enemigos*******************************************
        if (enemy_list.isEmpty()){
            textureEscenario = textureEscenarioAbierto;
            puertaE.setSize(10);
            shape2.rect(puertaE.getX(),puertaE.getY(),puertaE.getWidth(),puertaE.getHeight());
            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego));
            }
        }
        //*******************************************************Logica enemigos*******************************************************{
        if(movJoystick.getKnobPercentX()!=0.000 && movJoystick.getKnobPercentY()!=0.000) {
            rp.setX(rp.getX()+ (float)(Math.cos(angle)*20));
            rp.setY(rp.getY()+ (float)(Math.sin(angle)*20));

            if((!rp.overlaps(muroN))&&(!rp.overlaps(ro))&&(!rp.overlaps(muroO))&&(!rp.overlaps(muroS))&(!rp.overlaps(muroEN))&&(!rp.overlaps(muroES))&&(!rp.overlaps(puertaE))   ){
                personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
            }
        }

        shape2.end();
        shape.end();
    }
    //
    private void logicaEnemigo(float delta) {
        //****************************************Logica enemigos********************************************{
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
    public void render(float delta) {
        borrarPantalla(0, 0, 0);
        batch.setProjectionMatrix(camara.combined);
        //HUD
        batch.setProjectionMatrix(camara.combined);
        escenaJuego.draw();
        //*******************************************Dibujar Objetos*******************************************
        batch.begin();
        dibujarObjetos();
        batch.end();
        //*****************************************Dibujar escena del juego******************************************
        batch.begin();
        escenaJuego.draw();
        batch.end();
        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();
        //***************Enemigos***************
        if(juego.getEstadoJuego()==EstadoJuego.JUGANDO) {
            batch.begin();
            logicaMovimiento(delta);
            batch.end();
            logicaEnemigo(delta);
            //***************Balas***************
            logicaDisparo(delta);
            //***************Colision Bala/Enemigo***************
            sistemaColisionesBala();
        }
        //*****************************************Ganar/Perder**************************************
        //***************Perder***************
        if (personaje.getLife() <= 0) {
            juego.setScreen(new LoseScreen(juego));
        }
        //***************Ganar***************
        if (enemy_list.isEmpty()){
            textureEscenario = textureEscenarioAbierto;

            if (personaje.getPositionX() >= 1090 && personaje.getPositionY() < 330 && personaje.getPositionY() > 320) {
                //juego.setScreen(new PantallaMenu(juego, false));
                juego.setScreen(new EscenarioBoss(juego));
            }
        }
        if(juego.getEstadoJuego()==EstadoJuego.PAUSADO){
            escenaPausa.draw();
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




}
