package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class Texto {

    private BitmapFont font;

    public Texto(){
        font = new BitmapFont(Gdx.files.internal("fuentecuadro.fnt"));
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje,float x, float y){
        GlyphLayout glyph = new GlyphLayout();

        glyph.setText(font, mensaje);

        float anchoTexto = glyph.width;

        font.draw(batch,glyph, x-anchoTexto/2,y);
    }

}
