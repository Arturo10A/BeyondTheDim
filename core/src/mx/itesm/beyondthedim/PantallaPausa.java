package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
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

    public PantallaPausa(Viewport vista, SpriteBatch batch, final Juego juego, Stage escenaJuego) {
        super(vista,batch);
        this.escenaJuego = escenaJuego;
        Pixmap pixmap = new Pixmap((int) (Pantalla.ANCHO), (int) (Pantalla.ALTO), Pixmap.Format.RGBA8888);
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
        btnSalir.setPosition(Pantalla.ANCHO/2-btnSalir.getWidth()/2, Pantalla.ALTO*0.2f);
        btnSalir.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Regresa al menú
                juego.getMusic().stop();
                juego.musicaCargada = false;
                juego.setScreen(new PantallaMenu(juego));

            }
        });
        this.addActor(btnSalir);

        // Continuar
        Texture texturabtnReintentar = new Texture("Botones/button_back_2.png");
        TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                new TextureRegion(texturabtnReintentar));
        ImageButton btnReintentar = new ImageButton(trdReintentar);
        btnReintentar.setPosition(Pantalla.ANCHO/2-btnReintentar.getWidth()/2, Pantalla.ALTO*0.5f);
        btnReintentar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Continuar el juego
                juego.setEstadoJuego(EstadoJuego.JUGANDO);
                // Regresa el control a la pantalla
            }
        });
        this.addActor(btnReintentar);
    }

    public void setEscenaJuego(Stage escenaJuego) {
        this.escenaJuego = escenaJuego;
    }


}
