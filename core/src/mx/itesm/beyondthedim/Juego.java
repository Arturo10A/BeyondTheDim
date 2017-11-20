package mx.itesm.beyondthedim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class Juego extends Game {

	//Asset Manager
	private final AssetManager assetManager;
	//Variables del juego
    private Personaje personaje;
    private int vidaPersonaje;
    private ArrayList<Bullet> bullets;
    private Sound shoot;
    private static final float SWT = 0.3f;
    private float shootTimer;
    private float timeBala;
	private EstadoJuego estadoJuego;
    private boolean cambiarDireccion = true;
	private Music music;
    protected boolean musicOn = true;
    protected boolean musicaCargada = false;
	protected boolean juegoIniciado = false;
    private float enemyPosAncho = 0;
    private float enemyPosAlto = 0;
    private Texto texto;
    protected PantallaPausa escenaPausa;
    //Enemy block
    private ArrayList<Enemy> enemy_list = new ArrayList<Enemy>();
    //Historia
    private Texture texturaItemHistoria;
    //Limites
    private ArrayList<Rectangle> limites = new ArrayList<Rectangle>();
    //Objetos
    private ArrayList<ObjetoEscenario> objetos = new ArrayList<ObjetoEscenario>();
    //public boolean limitesGenerados = false;
    private Rectangle personajeRectangle;
    //Escenas de Juego
    private Stage escenaCuartoA;
    protected boolean cuartoAIniciado;
    private Stage escenaCuartoB;
    protected boolean cuartoBIniciado;
    private Stage escenaCuartoC;
    protected boolean cuartoCIniciado;
    private Stage escenaCuartoD;
    protected boolean cuartoDIniciado;
    private Stage escenaCuartoBossFinal;
    protected boolean cuartoBossFinalIniciado;
    private Stage escenaMenuPausa;
    private Stage escenaAboutUs;
    private Stage escenaSettings;
    /*
    //Joystick
    private Touchpad movJoystick;
    private Touchpad gunJoystick;*/
    Texture texturaBtnPausa;
    private ImageButton btnPausa;
    //
    public int prueba = 0;
    //
    private OrthographicCamera camera;

    //HUDEscenarioB
    protected OrthographicCamera camaraHUDEscenarioB;
    protected Viewport vistaHUDEscenarioB;

    //pantalla
    private Pantalla pantallaJuego;



	public Juego(){
		assetManager = new AssetManager();
	}

	public void iniciarJuego(float ANCHO, float ALTO){
	    vidaPersonaje = 1000;
	    juegoIniciado = true;
        personaje = new Personaje(ANCHO / 9.5f, ALTO / 2f, vidaPersonaje);
        bullets = new ArrayList<Bullet>();
        shootTimer = 0;
        shoot = Gdx.audio.newSound(Gdx.files.internal("Music/shoot.mp3"));
        texto = new Texto();
        personajeRectangle = personaje.getSprite().getBoundingRectangle();
        //generarJoysticks();
        generarBotonPausa();
        cuartoAIniciado = false;
        cuartoBIniciado = false;
        cuartoCIniciado = false;
        cuartoDIniciado = false;
        cuartoBossFinalIniciado = false;
    }

    //IniciarEscenas
    public void iniciarCuartoA(Viewport vista, OrthographicCamera camera){
	    System.out.println("Cuarto A");
	    enemy_list.clear();
        limites.clear();
        this.camera = camera;
        camera.update();
        objetos.clear();
        if(!cuartoAIniciado){
            escenaCuartoA = new Stage(vista);
            cuartoAIniciado = true;
            //cargarObjetosCuartoA;
        }
    }

    public void iniciarCuartoB(Viewport vista, OrthographicCamera camera){
        System.out.println("Cuarto B");
        enemy_list.clear();
        limites.clear();
        this.camera = camera;
        this.personaje.setPosition(-250,350);
        this.camera.position.set(-250,350,0);
        //this.camera.position.set(0, 0,0);
        camera.update();
        objetos.clear();
        if(!cuartoBIniciado){
            // Cámara HUD
            camaraHUDEscenarioB = new OrthographicCamera(Pantalla.ANCHO,Pantalla.ALTO);
            camaraHUDEscenarioB.position.set(-250, 350,0);
            camaraHUDEscenarioB.update();
            vistaHUDEscenarioB = new StretchViewport(Pantalla.ANCHO, Pantalla.ALTO, camaraHUDEscenarioB);
            escenaCuartoB = new Stage(vistaHUDEscenarioB);
            cuartoBIniciado = true;

        }

    }

    public void iniciarCuartoC(Viewport vista, OrthographicCamera camera){
        System.out.println("Cuarto C");
        enemy_list.clear();
        this.personaje.setPosition(Pantalla.ANCHO/2,60);
        limites.clear();
        this.camera = camera;
        camera.update();
        objetos.clear();
        if(!cuartoCIniciado){
            escenaCuartoC = new Stage(vista);
            cuartoCIniciado = true;
        }
    }

    public void iniciarCuartoD(Viewport vista, OrthographicCamera camera){
        enemy_list.clear();
        System.out.println("Cuarto D");
        limites.clear();
        this.camera = camera;
        camera.update();
        objetos.clear();
        if(!cuartoDIniciado){
            escenaCuartoD = new Stage(vista);
            cuartoDIniciado = true;
        }
    }

    public void iniciarCuartoBossFinal(Viewport vista, OrthographicCamera camera){
        enemy_list.clear();
        limites.clear();
        this.camera = camera;
        camera.update();
        objetos.clear();
        if(!cuartoBossFinalIniciado){
            escenaCuartoBossFinal = new Stage(vista);
            cuartoBossFinalIniciado = true;
            //generarJoysticks();
        }
    }

    //Get escenas
    public Stage getEscenaCuartoA(){
        return escenaCuartoA;
    }
    public Stage getEscenaCuartoB(){
        return escenaCuartoB;
    }
    public Stage getEscenaCuartoC(){
        return escenaCuartoC;
    }
    public Stage getEscenaCuartoD(){
        return escenaCuartoD;
    }
    public Stage getEscenaCuartoBossFinal(){
        return escenaCuartoBossFinal;
    }

    //Personaje
	public Personaje getPersonaje(){
	    return personaje;
    }

    //EstadoJuego
	public void setEstadoJuego(EstadoJuego estadoJuego){
	    this.estadoJuego = estadoJuego;
    }
    public EstadoJuego getEstadoJuego(){
	    return this.estadoJuego;
    }
    public void setVidaPersonaje(int vida){
        this.vidaPersonaje = vida;
    }

    //Objetos
    public ArrayList<ObjetoEscenario> getObjetos(){
        return this.objetos;
    }

    //Musica
    public void setMusic(Music music){
        if(this.music != null) {
            this.music.stop();
            this.music.dispose();
        }
        this.music = music;
        musicaCargada = true;
    }
    public Music getMusic(){
        return this.music;
    }

    //Balas
    public ArrayList<Bullet> getBullets(){
        return bullets;
    }

    //Enemigos
    public ArrayList<Enemy> getEnemy_list() {
        return enemy_list;
    }

    //Limites
    public ArrayList<Rectangle> getLimites(){
        return this.limites;
    }
    public void addLimites(Rectangle rectangle){
        this.limites.add(rectangle);
    }
    public void clearLimites(){
        this.limites.clear();
    }

    //Pantalla
    public void setPantallaJuego(Pantalla pantallaJuego){
        this.pantallaJuego = pantallaJuego;
    }
    /*
    //Joysticks
    public Touchpad getGunJoystick(){
        return gunJoystick;
    }
    public Touchpad getMovJoystick(){
        return movJoystick;
    }*/

    //Boton pausa
    public ImageButton getBtnPausa(){
        return btnPausa;
    }

    @Override
	public void create () {
		// Lo preparamos para que cargue mapas
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new PantallaCargando(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.clear();
	}

    public void controlMovPad(SpriteBatch batch, Touchpad pad, Touchpad movJoystick) {
        //Narvaez Logic

        Rectangle personajeRectangle = personaje.getSprite().getBoundingRectangle();
        personajeRectangle.setX(personaje.getPositionX()+17);
        personajeRectangle.setY(personaje.getPositionY());
        personajeRectangle.setWidth(30);
        personajeRectangle.setHeight(20);

        Vector2 v = new Vector2(movJoystick.getKnobPercentX(), movJoystick.getKnobPercentY());
        float ang = v.angle();
        double angle = ang*Math.PI/180.0;
        if(movJoystick.getKnobPercentX()!=0.000 && movJoystick.getKnobPercentY()!=0.000) {
            personajeRectangle.setX(personajeRectangle.getX() + (float) (Math.cos(angle) * 20));
            personajeRectangle.setY(personajeRectangle.getY() + (float) (Math.sin(angle) * 20));

            //Sprite a la Derecha
            if ((ang > 0 && ang < 45) || ang > 315 && ang < 360) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
                //Sprite a la Izq
            } else if (ang > 135 && ang < 225) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
            }else{
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
            }
            if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                    &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                    &&(!personajeRectangle.overlaps(this.getLimites().get(5))) ){
                personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
            }

        }else{
            personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());

        }
    }

    public void conMovPadGrande(SpriteBatch batch, Touchpad pad, Touchpad movJoystick) {
        if (cambiarDireccion) {
            if (pad.getKnobPercentX() > 0.20) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
            } else if (pad.getKnobPercentX() < -0.20) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
            } else if (pad.getKnobPercentX() == 0) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
            }
        }
        //Restricciones de movimiento(paredes)
        //Narvaez Logic


        Rectangle personajeRectangle = personaje.getSprite().getBoundingRectangle();
        personajeRectangle.setX(personaje.getPositionX()+17);
        personajeRectangle.setY(personaje.getPositionY());
        personajeRectangle.setWidth(30);
        personajeRectangle.setHeight(20);

        Vector2 v = new Vector2(movJoystick.getKnobPercentX(), movJoystick.getKnobPercentY());
        float ang = v.angle();
        double angle = ang*Math.PI/180.0;
        if(movJoystick.getKnobPercentX()!=0.000 && movJoystick.getKnobPercentY()!=0.000) {
            personajeRectangle.setX(personajeRectangle.getX()+ (float)(Math.cos(angle)*20));
            personajeRectangle.setY(personajeRectangle.getY()+ (float)(Math.sin(angle)*20));
            //Overlaps de diferentes niveles
            boolean h = pantallaJuego instanceof  PantallaCuartoA;
            System.out.println(h);
            if(pantallaJuego instanceof PantallaCuartoA){
                //((PantallaCuartoA) pantalla).generarOverlaps();
                if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(5)))){
                    personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
                }
                System.out.println("Ovelaps A");
            }
            if(pantallaJuego instanceof PantallaCuartoB){
                //((PantallaCuartoB) pantalla).generarOverlaps();
                if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(5)))&&(!personajeRectangle.overlaps(this.getLimites().get(6)))&&(!personajeRectangle.overlaps(this.getLimites().get(7)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(8)))&&(!personajeRectangle.overlaps(this.getLimites().get(9)))&&(!personajeRectangle.overlaps(this.getLimites().get(10)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(11)))){
                    personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
                }

                System.out.println("Ovelaps B");
            }
            if(pantallaJuego instanceof PantallaCuartoC){
                //((PantallaCuartoC) pantalla).generarOverlaps();
                if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(5)))&&(!personajeRectangle.overlaps(this.getLimites().get(6)))&&(!personajeRectangle.overlaps(this.getLimites().get(7)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(8)))){
                    personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
                }
                System.out.println("Ovelaps C");
            }
            if(pantallaJuego instanceof PantallaCuartoD){
                //((PantallaCuartoD) pantalla).generarOverlaps();
                if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(5)))&&(!personajeRectangle.overlaps(this.getLimites().get(6)))){
                    personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
                }
                System.out.println("Ovelaps D");
            }
            if(pantallaJuego instanceof PantallaCuartoEscenarioBoss){
                //((PantallaCuartoA) pantalla).generarOverlaps();
                if((!personajeRectangle.overlaps(this.getLimites().get(1)))&&(!personajeRectangle.overlaps(this.getLimites().get(0)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(2)))&&(!personajeRectangle.overlaps(this.getLimites().get(3)))&(!personajeRectangle.overlaps(this.getLimites().get(4)))
                        &&(!personajeRectangle.overlaps(this.getLimites().get(5)))&&(!personajeRectangle.overlaps(this.getLimites().get(6)))){
                    personaje.mover((float)(Math.cos(angle)), (float)(Math.sin(angle)));
                }
                System.out.println("Ovelaps Boss");
            }
        }
    }

    public void logicaDisparo(float delta, Touchpad gunJoystick,SpriteBatch batch){
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

    public void sistemaColisionesBala() {
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

    public void logicaEnemigo(float delta) {
        //****************************************Logica enemigos********************************************{
        enemyPosAlto = 0;
        enemyPosAncho = 0;
        for (Enemy ene : enemy_list) {
            ene.attack(personaje, enemyPosAncho, enemyPosAlto);
            enemyPosAncho += ene.sprite.getWidth() / 2;
            enemyPosAlto += ene.sprite.getHeight() / 2;
            ene.doDamage(this.personaje);
        }
    }

    public void dibujarObjetos(SpriteBatch batch, Texture textureEscenario){
        batch.draw(textureEscenario, Pantalla.ANCHO / 2 - textureEscenario.getWidth() / 2, Pantalla.ALTO / 2 - textureEscenario.getHeight() / 2);

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());

        for (int i = 0; i <= objetos.size()-1 ; i++) {
            objetos.get(i).dibujar(batch,Gdx.graphics.getDeltaTime());
        }

        //Enemigos
        for (Enemy ene : this.getEnemy_list()) {
            ene.render(batch, Gdx.graphics.getDeltaTime());
        }
        //Vida
        String lifeString = "Vida: " + personaje.getLife();
        texto.mostrarMensaje(batch, lifeString, camera.position.x-Pantalla.ANCHO*0.45f, camera.position.y+Pantalla.ALTO*0.47f);
        //Balas
        for (Bullet bullet : this.getBullets()) {
            bullet.render(batch);
        }
        /*
        //Items
        batch.draw(texturaItemHistoria, ANCHO*0.80f, ALTO*0.17f,
                texturaItemHistoria.getWidth()*0.20f,texturaItemHistoria.getHeight()*0.20f);*/
    }


    public void pausa(Viewport vista, SpriteBatch batch, Stage escenaJuego) {
        if (this.getEstadoJuego()==EstadoJuego.PAUSADO) {
            // Activar escenaPausa y pasarle el control
            if (this.escenaPausa==null) {
                this.escenaPausa = new PantallaPausa(vista, batch, this, escenaJuego, camera);
                this.escenaPausa.dibujar();
            }else{
                escenaPausa.setEscenaJuego(escenaJuego, camera);
                this.escenaPausa.dibujar();
            }
            this.escenaPausa.draw();
            Gdx.input.setInputProcessor(this.escenaPausa);
        }
    }

    public void jugar(float delta, SpriteBatch batch, Stage escenaJuego, Touchpad gunJoystick){
        if(this.getEstadoJuego()==EstadoJuego.JUGANDO) {
            if(Gdx.input.getInputProcessor()!= escenaJuego) {
                Gdx.input.setInputProcessor(escenaJuego);
            }
            this.logicaEnemigo(delta);
            //***************Balas***************
            this.logicaDisparo(delta, gunJoystick, batch);
            //***************Colision Bala/Enemigo***************
            this.sistemaColisionesBala();
        }
    }
    /*
    private void generarJoysticks(){
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
    }*/

    private void generarBotonPausa(){
        texturaBtnPausa = new Texture("Botones/button_pause.png");
        TextureRegionDrawable trdPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        btnPausa = new ImageButton(trdPausa);
        btnPausa.setPosition(Pantalla.ANCHO - btnPausa.getWidth() - 5, Pantalla.ALTO - btnPausa.getHeight() - 5);
    }


}
