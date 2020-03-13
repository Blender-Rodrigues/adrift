package ee.taltech.iti0200.physics;

import ee.taltech.iti0200.graphics.Camera;
import ee.taltech.iti0200.graphics.Model;
import ee.taltech.iti0200.graphics.Shader;
import ee.taltech.iti0200.graphics.Texture;
import ee.taltech.iti0200.graphics.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import java.io.IOException;

import static ee.taltech.iti0200.graphics.Graphics.defaultTexture;

public class Body {

    private final Logger logger = LogManager.getLogger(Body.class);

    protected double mass;
    protected double inverseMass;
    protected Vector speed;
    protected BoundingBox boundingBox;
    protected boolean moved;
    protected double elasticity;
    protected double frictionCoefficient;
    protected double dragFromSurface;
    private boolean collideable;

    private Model model;
    private Texture texture;
    private Transform transform;

    public Body(double mass, BoundingBox boundingBox, boolean collideable) {
        this.mass = mass;
        this.speed = new Vector(0.0, 0.0);
        this.inverseMass = 1 / mass;
        this.boundingBox = boundingBox;
        this.collideable = collideable;
        this.elasticity = 1;
        this.frictionCoefficient = 1;
    }

    public Body(double mass, Vector min, Vector max, boolean collideable) {
        this(mass, new BoundingBox(min, max), collideable);
    }

    public Body(
        double mass,
        Vector size,
        Vector position,
        boolean collideable,
        boolean usingPositionAndSize
    ) {
        this(mass, new BoundingBox(position, size, usingPositionAndSize), collideable);
    }

    public void move(double timeToMove) {
        Vector moveDelta = new Vector(this.speed);
        moveDelta.scale(timeToMove);
        this.move(moveDelta);
    }

    public void move(Vector moveDelta) {
        this.boundingBox.move(moveDelta);
    }

    public void accelerate(Vector accelerateDelta) {
        this.speed.add(accelerateDelta);
    }

    public void drag() {
        this.setXSpeed(this.dragFromSurface * this.frictionCoefficient * speed.getX());
    }

    public double getMass() {
        return mass;
    }

    public Vector getSpeed() {
        return speed;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean intersects(Body otherBody) {
        return this.boundingBox.intersects(otherBody.getBoundingBox());
    }

    public boolean isCollideable() {
        return collideable;
    }

    public void setXSpeed(double speed) {
        this.speed = new Vector(speed, this.speed.getY());
    }

    public void setYSpeed(double speed) {
        this.speed = new Vector(this.speed.getX(), speed);
    }

    public void setElasticity(double elasticity) {
        this.elasticity = elasticity;
    }

    public double getElasticity() {
        return this.elasticity;
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public void setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public double getDragFromSurface() {
        return dragFromSurface;
    }

    public void setDragFromSurface(double dragFromSurface) {
        this.dragFromSurface = dragFromSurface;
    }

    public void onCollide(Body otherBody) {

    }

    public void initializeGraphics() {
        float[] vertices = new float[]{
            -1f, 1f, 0,
            1f, 1f, 0,
            1f, -1f, 0,
            -1f, -1f, 0
        };

        float[] texture = new float[]{
            0, 0,
            1, 0,
            1, 1,
            0, 1
        };

        int[] indices = new int[]{
            0, 1, 2,
            2, 3, 0
        };

        model = new Model(vertices, texture, indices);
        this.texture = defaultTexture;

        transform = new Transform();
        transform.scale = new Vector3f((float) getBoundingBox().getSize().getX(), (float) getBoundingBox().getSize().getY(), 1);
    }

    public void changeTexture(String filename) {
        try {
            this.texture = new Texture(filename);
        } catch (IOException e) {
            logger.error("Failed to change texture: " + e.getMessage(), e);
        }
    }

    public void render(Shader shader, Camera camera) {
        transform.pos.set(new Vector3f((float) this.boundingBox.getCentre().x, (float) this.boundingBox.getCentre().y, 0));

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

}
