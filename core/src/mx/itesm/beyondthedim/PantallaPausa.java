package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public class PantallaPausa extends Stage{

    private Stage escenaJuego;
    private Image imgRectangulo;
    private Juego juego;
    private ImageButton btnSalir;
    private ImageButton btnReintentar;
    protected boolean elementosDibujados;
    private float posX;
    private float posY;

    public PantallaPausa(Viewport vista, SpriteBatch batch, final Juego juego, Stage escenaJuego, float posX, float posY) {
        super(vista,batch);
        this.escenaJuego = escenaJuego;
        this.juego = juego;
        this.posX = posX;
        this.posY = posY;
        Pixmap pixmap = new Pixmap((int) (Pantalla.ANCHO), (int) (Pantalla.ALTO), Pixmap.Format.RGBA8888);
        pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.4f );
        pixmap.fillRectangle(0, 0,pixmap.getWidth(),pixmap.getHeight());
        Texture texturaRectangulo = new Texture(pixmap);
        pixmap.dispose();
        imgRectangulo = new Image(texturaRectangulo);
        Texture texturaBtnSalir = new Texture("Botones/button_inicio.png");
        TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                new TextureRegion(texturaBtnSalir));
        btnSalir = new ImageButton(trdSalir);
        // Continuar
        Texture texturabtnReintentar = new Texture("Botones/button_back_2.png");
        TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                new TextureRegion(texturabtnReintentar));
        btnReintentar = new ImageButton(trdReintentar);

    }

    public void setEscenaJuego(Stage escenaJuego, OrthographicCamera camera, float posX, float posY) {
        this.escenaJuego = escenaJuego;
        this.posX = posX;
        this.posY = posY;
    }

    public void dibujar(){
        imgRectangulo.setPosition(posX-Pantalla.ANCHO/2, posY-Pantalla.ALTO/2);
        System.out.println(imgRectangulo.getX()+ "*********************" + imgRectangulo.getY());
        System.out.println(posX + " kkk "+ posY);
        this.addActor(imgRectangulo);

        // Salir
        btnSalir.setPosition(posX-btnSalir.getWidth()/2, posY - btnSalir.getHeight());
        btnSalir.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Regresa al menú
                juego.getMusic().stop();
                juego.musicaCargada = false;
                juego.setEstadoJuego(EstadoJuego.JUGANDO);
                juego.reiniciarJuego();
                juego.setScreen(juego.getMenu());
                /*
                juego.getCuartoA().dispose();
                juego.getCuartoB().dispose();
                juego.getCuartoC().dispose();
                juego.getCuartoD().dispose();
                juego.getEscenaCuartoTutorial().dispose();
                juego.getEscenaCuartoBossFinal().dispose();*/
            }
        });
        this.addActor(btnSalir);
        btnReintentar.setPosition(posX-btnReintentar.getWidth()/2, posY+btnReintentar.getHeight()/2);
        btnReintentar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Continuar el juego
                juego.setEstadoJuego(EstadoJuego.JUGANDO);
                // Regresa el control a la pantalla
            }
        });
        this.addActor(btnReintentar);
        elementosDibujados = true;
    }


}
