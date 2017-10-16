package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Arturo on 05/09/17.
 */

class PantallaJuego extends Pantalla{

    private final Juego juego;
    private Texture textureEcenario;  //Imagen del ecenario

    private float DX = 28;
    private int pasos = 20;
    private float timerPasos = 0;

    //Jett start
    private  Personaje personaje;

    //Jett Speed
    private int DX_PERSONAJE=25;
    private int DY_PERSONAJE =25;


    //Enemy block
    private Enemy enemy;
    private int DX_ENEMY = 100;
    private int DY_ENMEY = 100;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private Enemy enemy5;


    private Stage escenaJuego;
    private Texture texturaBtnGoBack; //Boton de regreso

    //Texto
    private Texto texto;

    //Variable of control
    private float time;

    public PantallaJuego(Juego juego) {


        this.juego = juego;
    }

    public void cargarTexturas(){
        texturaBtnGoBack = new Texture("button_pause.png");

    }

    public void crearEscena(){
        escenaJuego = new Stage(vista);


        //Boton GOBACK -> check variable and conflic agins problems

        TextureRegionDrawable trdGoBack = new TextureRegionDrawable(new TextureRegion(texturaBtnGoBack));
        ImageButton btnGoBack = new ImageButton(trdGoBack);

        btnGoBack.setPosition(ANCHO-btnGoBack.getWidth()-5,ALTO-btnGoBack.getHeight()-5);

        escenaJuego.addActor(btnGoBack);

        btnGoBack.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("clicked","***GO BACK***");
                juego.setScreen(new PantallaMenu(juego));
            }

        });



    }



    @Override
    public void show() {

        cargarTexturas();
        crearEscena();

        textureEcenario = new Texture("inicio.png");
        personaje = new Personaje(ANCHO/4,ALTO/2, 1000000);

        //Class enemy test
        enemy = new Enemy(0, 0, 100, 20);
        enemy1 = new Enemy(20, 50, 100, 30);
        enemy2 = new Enemy(70, 90, 100, 40);
        enemy3 = new Enemy(180, 680,100, 50);
        enemy4 = new Enemy(600, 500,100, 100);

        Gdx.input.setInputProcessor(escenaJuego);
        Gdx.input.setInputProcessor(new ProcesadorEventos());

        texto = new Texto();
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        //If time equals 10 move the enemy to the current location of the character
        //Start damage

        time += Gdx.graphics.getDeltaTime();
        if (time >= 1){

            enemy.atack(personaje);
            enemy1.atack(personaje);
            enemy2.atack(personaje);
            enemy3.atack(personaje);
            enemy4.atack(personaje);

            enemy.doDamage(personaje);
            enemy2.doDamage(personaje);
            enemy3.doDamage(personaje);
            enemy4.doDamage(personaje);

                // If the life of jett is equal 0 youy return to LoseScreen.

            if (personaje.getLife() <= 0){
                System.out.println("You die");
                juego.setScreen(new LoseScreen(juego));
            }

            time = 0;

            //System.out.println("X: "+personaje.getPositionX()+" Y: "+ personaje.getPositionY());

        }

        batch.begin();

        batch.draw(textureEcenario,Pantalla.ANCHO/2-textureEcenario.getWidth()/2, Pantalla.ALTO/2-textureEcenario.getHeight()/2);

        //Drawing jett
        personaje.render(batch);

        //Drawing test of class enemy
        enemy.render(batch);
        enemy1.render(batch);
        enemy2.render(batch);
        enemy3.render(batch);
        enemy4.render(batch);

        texto.mostrarMensaje(batch,"Vida: 100%",95,Pantalla.ALTO/1.07f);



        batch.end();

        batch.begin();
        escenaJuego.draw();
        batch.end();

        escenaJuego.act(Gdx.graphics.getDeltaTime());
        escenaJuego.draw();


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


    private class ProcesadorEventos implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {

            if (keycode == Input.Keys.LEFT){

                if (personaje.getPositionX() <= 145.f){

                }else{
                    personaje.mover(-DX_PERSONAJE,0);
                }

            }if (keycode == Input.Keys.RIGHT){

                if (personaje.getPositionX() >= 1070.0){

                }else{
                    personaje.mover(DX_PERSONAJE,0);
                }

            }if (keycode == Input.Keys.DOWN){

                if (personaje.getPositionY() <= 110.0){}else{
                    personaje.mover(0,-DY_PERSONAJE);
                }

            }if (keycode == Input.Keys.UP){

                if (personaje.getPositionY() >= 585.0){}
                else {
                    personaje.mover(0,DY_PERSONAJE);
                }

            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
