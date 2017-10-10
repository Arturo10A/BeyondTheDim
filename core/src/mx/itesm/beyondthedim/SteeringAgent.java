package mx.itesm.beyondthedim;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.MatchVelocity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Arturo on 10/10/17.
 */

public class SteeringAgent implements Steerable {

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    Vector2 position;
    float orientation;
    Vector2 linearVelocity;
    float angularVelocity;
    float maxSpeed;
    boolean independenFacing;
    SteeringBehavior<Vector2> steeringBehavior;

    @Override
    public Vector getLinearVelocity() {

        //Do somethig
        return null;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        return 0;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return 0;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {

    }

    @Override
    public float getMaxLinearAcceleration() {
        return 0;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector vector) {
        return 0;
    }

    @Override
    public Vector angleToVector(Vector outVector, float angle) {
        return null;
    }

    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    //Actual implmentation depends on your coordinate system
    // Here we assume the y-axis is pointing upwards

    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float) Math.cos(angle);

        return outVector;
    }

    @Override
    public Location newLocation() {
        return null;
    }

    public void update(float delta){
        if (steeringBehavior != null){
            //Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

            /*
            *
            *
            *
            * Do something
            *
            *
            *
            * */


            //Apply steering acceleration to move this agent

            applyStering(steeringOutput, delta);

        }
    }

    public void applyStering(SteeringAcceleration<Vector2> steering, float time){
        //Update position and linear velocity. Velocity is trimmed to maximum speed
        this.position.mulAdd(linearVelocity,time);
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());

        if (independenFacing){
            this.orientation += angularVelocity * time;
            this.angularVelocity += steering.angular * time;
        }

        else {
            //in case of non-indepedent facing we have to aling to linear velocity

            float newOrientation = calculateOrientationFromLinearVelocity(this);
            this.orientation = newOrientation;
            if (newOrientation != this.orientation){
                this.angularVelocity = (newOrientation-this.orientation) * time;
                this.orientation = newOrientation;
            }
        }
    }

    private float calculateOrientationFromLinearVelocity(SteeringAgent steeringAgent) {
        return 0;
    }
}
