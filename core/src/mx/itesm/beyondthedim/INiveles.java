package mx.itesm.beyondthedim;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

public interface INiveles {

    public Stage escenarioJuego = null;

    public void crearEnemigos();

    public void ganar();

    public void perder();

    public void pausa();

    public void jugar(float delta);

    public void generarLimites();

    public void cargarTexturas();

    public void cargarMusica();

    public void crearEscena();

    //public void generarOverlaps();

}
