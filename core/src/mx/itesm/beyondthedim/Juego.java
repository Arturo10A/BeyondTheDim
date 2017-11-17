package mx.itesm.beyondthedim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

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
	private EstadoJuego estadoJuego;
	private Music music;
	private Personaje personaje;
	private int vidaPersonaje = 1000;
	protected boolean musicOn = true;
	protected boolean juegoIniciado = false;



	public Juego(){
		assetManager = new AssetManager();
	}

	public void createPersonaje(float ANCHO, float ALTO){
        personaje = new Personaje(ANCHO / 9.5f, ALTO / 2f, vidaPersonaje);
    }

	public Personaje getPersonaje(){
	    return personaje;
    }

	public void setEstadoJuego(EstadoJuego estadoJuego){
	    this.estadoJuego = estadoJuego;
    }

    public EstadoJuego getEstadoJuego(){
	    return this.estadoJuego;
    }

    public void setMusic(Music music){
        this.music = music;
    }

    public Music getMusic(){
        return this.music;
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

    public void controlJoystickMovimiento(SpriteBatch batch, Touchpad pad, Touchpad movJoystick,boolean cambiarDireccion, Personaje obstacle) {
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
}
