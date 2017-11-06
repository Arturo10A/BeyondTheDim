package mx.itesm.beyondthedim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creado por Equipo 2
 *
 *Arturo Amador Paulino
 *Monserrat Lira Sorcia
 *Jose Rodrigo Narvaez Berlanga
 *Jorge Alexis Rubio Sumano
 *
 */

public class Enemy extends Objeto{

    //Texturas enemigo
    private Texture enemyTexture;
    private TextureRegion enemyTextureCompleta;
    //Movimiento
    private float x;
    private float y;
    //
    private float life;
    //
    private float timerAnimacion;
    private int damage;
    public static final int SPEED=1;
    private Animation<TextureRegion> spriteAnimado;
    private TextureRegion[][] texturaEnemigo;
    protected Personaje.EstadoMovimiento estadoMovimiento = Personaje.EstadoMovimiento.QUIETO;


    public Enemy(float x, float y, float life, int damage){
        this.x = x;
        this.y = y;
        this.life = life;
        this.damage = damage;
        enemyTexture = new Texture("Enemigos/mosca_completo.png");
        enemyTextureCompleta = new TextureRegion(enemyTexture);

        //Divide en 4 frames 63x100
        texturaEnemigo = enemyTextureCompleta.split(90,80);

        //Se crea la animacion con tiempo de 0.25 segundos entre frames
        spriteAnimado = new Animation(0.1f, texturaEnemigo[0][3], texturaEnemigo[0][2], texturaEnemigo[0][1]);

        //Animación infinita
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaEnemigo[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    public void render(SpriteBatch batch, float tiempo){
        //batch.draw(enemyTexture, x, y);
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += tiempo;
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion, true);
                if (estadoMovimiento== Personaje.EstadoMovimiento.MOV_DERECHA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                batch.draw(region,x,y);
                break;
            case QUIETO:
            case INICIANDO:
                batch.draw(texturaEnemigo[0][0], x, y);
                break;
        }
    }


    public void mover(float dx, float dy){
        x += dx;
        y += dy;
    }

    public void goBack(){
        mover(50,0);
    }

    public void atack(Personaje target, float enemyPosAncho, float enemyPosAlto){
        if(target.getPositionX()>this.x)
            setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
        else{
            setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
        }
        this.x +=  ((float) ((target.getPositionX() - this.getPositionX()) * 0.02));
        this.y +=  ((float) ((target.getPositionY() - this.getPositionY() + enemyPosAlto) * 0.02));

    }

    public void doDamage(Personaje target){
        if (distance(target) < 80){
            target.damage(this.damage);
        }
    }

    public double distance(Personaje target){

        //character position
        double personajeX = target.getPositionX();
        double personajeY = target.getPositionY();

        //Enemy position
        double currentX = this.getPositionX();
        double currentY = this.getPositionY();

        double distance = Math.sqrt( Math.pow(personajeX-currentX,2) + Math.pow(personajeY-currentY,2));
        return distance;

    }

    public boolean isDead(){
        if (this.life > 0){
            return false;
        }
        return true;
    }


    public void receiveDamage(int damage){

        this.life-=damage;
        System.out.println("Enemy life: "+this.life);
    }

    public void setEstadoMovimiento(Personaje.EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public float getPositionX(){return this.x;}
    public float getPositionY(){return this.y;}

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
    }

}
