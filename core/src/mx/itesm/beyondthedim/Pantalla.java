package mx.itesm.beyondthedim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public abstract class Pantalla implements Screen {

    //Atributos disponible en todas las clases del proyecto
    public static  final float ANCHO = 1280;
    public static  final float ALTO = 720;

    //Atributos disponibles en las subclases
    //Todas las pantallas tiene una cámara y la vista
    protected OrthographicCamera camara;
    protected Viewport vista;
    //Betacolliders
    protected Box2DDebugRenderer b2dr;
    protected World world;
    protected Body player;

    //Todas las pantallas sibujan
    protected SpriteBatch batch;

    public Pantalla(){

        camara = new OrthographicCamera(ANCHO,ALTO);


        world = new World(new Vector2(0,0f),false);
        b2dr = new Box2DDebugRenderer();

        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
        batch = new SpriteBatch();
    }

    protected void borrarPantalla(){
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    protected  void borrarPantalla(float r, float g, float b){

        Gdx.gl.glClearColor(r,g,b,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    //Borra la pantalla con el color RGB(r,g,b)
    @Override
    public void resize(int width, int height) {

        vista.update(width,height);
        camara.viewportHeight=ALTO;
        camara.viewportWidth=ANCHO;
        camara.update();
    }

    @Override
    public void hide() {
        dispose();

        /// Libera los recursos asignados por cada pantalla
    }

    public void update(float delta){
        world.step(1/60f,6,2);

    }

    public Body createObject(float x, float y){
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x,y);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.PPM / 2, Constants.PPM /2);

        pBody.createFixture(shape,1.0f);
        shape.dispose();

        return pBody;
    }

}
