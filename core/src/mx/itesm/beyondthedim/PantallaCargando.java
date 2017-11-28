package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Creado por Equipo 2
 * <p>
 * Arturo Amador Paulino
 * Monserrat Lira Sorcia
 * Jose Rodrigo Narvaez Berlanga
 * Jorge Alexis Rubio Sumano
 */

class PantallaCargando extends Pantalla {

    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private Sprite spriteCargando;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;

    // AssetManager
    private AssetManager manager;

    private Juego juego;
    private Pantallas siguientePantalla;
    private int avance; // % de carga
    private Texto texto;

    private Texture texturaCargando;

    public PantallaCargando(Juego juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("Cargando/cargando.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO / 2 - spriteCargando.getWidth() / 2, ALTO / 2 - spriteCargando.getHeight() / 2);
        cargarRecursosSigPantalla();
        texto = new Texto();
    }

    // Carga los recursos de la siguiente pantalla
    private void cargarRecursosSigPantalla() {
        manager = juego.getAssetManager();
        avance = 0;
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case CUARTO_A:
                cargarRecursosCuartoA();
                break;
            case CUARTO_B:
                cargarRecursosCuartoB();
                break;
            case CUARTO_C:
                cargarRecursosCuartoC();
                break;
            case CUARTO_D:
                cargarRecursosCuartoD();
                break;
            case CUARTO_BOSS:
                cargarRecursosBoss();
                break;
            case CUARTO_TUTORIAL:
                cargarRecursosTutorial();
                break;
            case CUARTO_JUEGO_LIBRE:
                cargarRecursosJuegoLibre();
                break;
        }
    }

    private void cargarRecursosMenu() {
        manager.load("Botones/button_play.png", Texture.class);
        manager.load("Botones/button_about-us.png", Texture.class);
        manager.load("Botones/button_settings.png", Texture.class);
        manager.load("Botones/button_free_mode.png", Texture.class);
        //texturaBtnInstructions = new Texture("Botones/button_instructions.png");
        manager.load("Stage/MenuFondo.jpg", Texture.class);
        manager.load("Music/bensound-slowmotion.mp3", Music.class);
        manager.load("Objetos_varios/begin_instruction.png", Texture.class);
        manager.load("Botones/button_free_mode_textura.png", Texture.class);
    }


    private void cargarRecursosCuartoA() {
        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Stage/fondo_nivel_uno_cerrado.jpg", Texture.class);
        manager.load("Stage/fondo_nivel_uno_abierto.jpg", Texture.class);
        manager.load("iconLife.png", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
    }

    private void cargarRecursosCuartoB() {
        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Stage/escenarioB_cerrado.jpg", Texture.class);
        manager.load("Stage/escenarioB_abiertoC.jpg", Texture.class);
        manager.load("Stage/escenarioB_abiertoD.jpg", Texture.class);
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        manager.load("Objetos_varios/mesa_de_control_2.png", Texture.class);
        manager.load("Stage/escenarioB_abiertoBoss.jpg", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
        manager.load("iconLife.png", Texture.class);
    }

    private void cargarRecursosCuartoC() {
        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Stage/escenarioC_abierto.jpg", Texture.class);

        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        manager.load("iconLife.png", Texture.class);
        manager.load("Objetos_varios/cpu_izq.png", Texture.class);
        manager.load("Objetos_varios/cpu_der.png", Texture.class);
        manager.load("Objetos_varios/cpu_central.png", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
    }

    private void cargarRecursosCuartoD() {

        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Stage/escenarioD_abierto.jpg", Texture.class);
        manager.load("Objetos_varios/cama_3_2.png", Texture.class);
        manager.load("Objetos_varios/cama_2_2.png", Texture.class);
        manager.load("Objetos_varios/cama_4_2.png", Texture.class);
        manager.load("Objetos_varios/cama_1_2.png", Texture.class);
        manager.load("Objetos_varios/computadora_2.png", Texture.class);
        manager.load("Objetos_varios/extintor.png", Texture.class);
        manager.load("iconLife.png", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
    }

    private void cargarRecursosTutorial() {
        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Botones/forward.png", Texture.class);
        manager.load("Stage/tutorial.jpg", Texture.class);
        manager.load("iconLife.png", Texture.class);
        manager.load("test.png", Texture.class);
        manager.load("test2.png", Texture.class);
        manager.load("test3.png", Texture.class);
        manager.load("test4.png", Texture.class);
        manager.load("test5.png", Texture.class);
        manager.load("test6.png", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
    }

    private void cargarRecursosBoss() {

        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        manager.load("Botones/button_pause.png", Texture.class);
        manager.load("Stage/escenario_final.png", Texture.class);
        manager.load("iconLife.png", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
    }

    private void cargarRecursosJuegoLibre() {

        manager.load("Joystick/joystick_movimiento.png", Texture.class);
        manager.load("Joystick/joystick_fondo.png", Texture.class);
        //texturaItemHistoria = new Texture("Objetos_varios/notas_prueba.png");
        manager.load("Objetos_varios/mesa_de_control_2.png", Texture.class);
        manager.load("Stage/escenario_libre.jpg", Texture.class);
        manager.load("Music/bensound-extremeaction.mp3",Music.class);
        manager.load("iconLife.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.1f, 0.1f, 0.1f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch, "Loading", ANCHO / 2, ALTO / 2);
        batch.end();
        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion <= 0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(20);
        }
        System.out.println(avance);
        // Actualizar carga
        actualizarCargaRecursos();
    }

    private void actualizarCargaRecursos() {
        //Se terminaron de cargar los recursos
        if (manager.update()) {
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(juego.getMenu());
                    break;
                case CUARTO_A:
                    juego.setScreen(juego.getCuartoA());
                    break;
                case CUARTO_B:
                    juego.setScreen(juego.getCuartoB());
                    break;
                case CUARTO_C:
                    juego.setScreen(juego.getCuartoC());
                    break;
                case CUARTO_D:
                    juego.setScreen(juego.getCuartoD());
                    break;
                case CUARTO_BOSS:
                    juego.setScreen(new PantallaCuartoEscenarioBoss(juego));
                    break;
                case CUARTO_TUTORIAL:
                    juego.setScreen(new PantallaTutorial(juego));
                    break;
                case CUARTO_JUEGO_LIBRE:
                    juego.setScreen(juego.getCuartoLibre());
                    break;
            }
        }
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
        texturaCargando.dispose();
    }

}

