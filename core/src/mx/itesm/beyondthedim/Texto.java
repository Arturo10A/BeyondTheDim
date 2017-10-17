package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Arturo on 12/09/17.
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
