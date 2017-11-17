package mx.itesm.beyondthedim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

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
    protected Pantalla.EscenaPausa escenaPausa;
    //Enemy block
    private ArrayList<Enemy> enemy_list = new ArrayList<Enemy>();
    //Historia
    private Texture texturaItemHistoria;


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

    public void controlJoystickMovimiento(SpriteBatch batch, Touchpad pad, Touchpad movJoystick, Personaje obstacle) {
        if(cambiarDireccion) {
            if (pad.getKnobPercentX() > 0.20) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA, batch, Gdx.graphics.getDeltaTime());
            } else if (pad.getKnobPercentX() < -0.20) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA, batch, Gdx.graphics.getDeltaTime());
            } else if (pad.getKnobPercentX() == 0) {
                personaje.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO, batch, Gdx.graphics.getDeltaTime());
            }
        }
        //Restricciones de movimiento(paredes)
        //Right
        if (((personaje.getPositionX() >= 1120 && personaje.getPositionY() > 400) && movJoystick.getKnobPercentX() > 0)) {
            personaje.mover(-2,pad.getKnobPercentY());
        }
        if ((personaje.getPositionX() >= 1120 && personaje.getPositionY() > 400) && movJoystick.getKnobPercentX() > 0) {
            personaje.mover(-1,pad.getKnobPercentY());
        }
        //Left
        else if (personaje.getPositionX() <= 116.42 && movJoystick.getKnobPercentX() < 0) {
            personaje.mover(10,pad.getKnobPercentY());
        }
        //TOP
        else if (personaje.getPositionY() >= 549.42 && movJoystick.getKnobPercentY() > 0) {
            personaje.mover(pad.getKnobPercentX(), -10);
        }
        //Bottom
        else if (personaje.getPositionY() <= 110.0 && movJoystick.getKnobPercentY() < 0) {
            personaje.mover(pad.getKnobPercentX(), 10);
        } else {
            Rectangle rp = personaje.getSprite().getBoundingRectangle();
            Rectangle ro = obstacle.getSprite().getBoundingRectangle();
            Gdx.app.log("Choque",rp.toString()+","+ro.toString());
            rp.setX(rp.getX()+10);
            if(! rp.overlaps(ro)){
                Gdx.app.log("CHOQUE", "SI PUEDE CAMINAR");
                personaje.mover(pad.getKnobPercentX(),pad.getKnobPercentY());
            } else{
                Gdx.app.log("Choque ","NO SE PUEDE");
            }
        }
    }

    public void logicaDisparo(float delta, Touchpad gunJoystick, SpriteBatch batch){
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

    public void dibujarObjetos(SpriteBatch batch, Texture textureEscenario, Personaje obstacle){
        batch.draw(textureEscenario, Pantalla.ANCHO / 2 - textureEscenario.getWidth() / 2, Pantalla.ALTO / 2 - textureEscenario.getHeight() / 2);

        //Personaje Jett
        personaje.dibujar(batch, Gdx.graphics.getDeltaTime());

        obstacle.dibujar(batch, Gdx.graphics.getDeltaTime());

        //Enemigos
        for (Enemy ene : this.getEnemy_list()) {
            ene.render(batch, Gdx.graphics.getDeltaTime());
        }
        //Vida
        String lifeString = "Vida: " + personaje.getLife();
        texto.mostrarMensaje(batch, lifeString, 98, Pantalla.ALTO / 1.03f);
        //Balas
        for (Bullet bullet : this.getBullets()) {
            bullet.render(batch);
        }
        /*
        //Items
        batch.draw(texturaItemHistoria, ANCHO*0.80f, ALTO*0.17f,
                texturaItemHistoria.getWidth()*0.20f,texturaItemHistoria.getHeight()*0.20f);*/
    }
}
