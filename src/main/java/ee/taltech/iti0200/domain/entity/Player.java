package ee.taltech.iti0200.domain.entity;

import ee.taltech.iti0200.domain.World;
import ee.taltech.iti0200.graphics.Shader;
import ee.taltech.iti0200.graphics.ViewPort;
import ee.taltech.iti0200.physics.BoundingBox;
import ee.taltech.iti0200.physics.Vector;


public class Player extends Living {

    private static final long serialVersionUID = 1L;

    public static final int JUMP_AMOUNT_LIMIT = 2;

    private static final Vector SIZE = new Vector(0.875, 0.875);
    private static final double MASS = 70.0;
    private static final double ELASTICITY = 0.15;
    private static final double FRICTION_COEFFICIENT = 0.99;
    private static final double JUMP_DELTA_V = 10.0;
    private static final int MAX_HEALTH = 200;
    private static final int MAX_LIVES = 10;
    private static final double PERMEABILITY = 1;

    private String name = "Unknown";
    private int lives;
    private int jumpsLeft;
    private Vector lookingAt;

    public Player(Vector position, World world) {
        super(MASS, new BoundingBox(position, SIZE), world, MAX_HEALTH);
        this.elasticity = ELASTICITY;
        this.jumpsLeft = JUMP_AMOUNT_LIMIT;
        this.frictionCoefficient = FRICTION_COEFFICIENT;
        this.lookingAt = new Vector(1f, 0f);
        this.permeability = PERMEABILITY;
        this.lives = MAX_LIVES;
    }

    public void moveLeft() {
        if (!isAlive()) {
            return;
        }
        if (isOnFloor()) {
            accelerate(new Vector(-2.5, 0.0));
            action = Action.RUNNING;
        } else {
            accelerate(new Vector(-0.05, 0.0));
        }
    }

    public void moveRight() {
        if (!isAlive()) {
            return;
        }
        if (isOnFloor()) {
            accelerate(new Vector(2.5, 0.0));
            action = Action.RUNNING;
        } else {
            accelerate(new Vector(0.05, 0.0));
        }
    }

    public void jump() {
        if (isAlive() && getJumpsLeft() > 0) {
            setJumpsLeft(getJumpsLeft() - 1);
            accelerate(new Vector(0.0, getJumpDeltaV()));
        }
    }

    public void setLookingAt(Vector targetPosition) {
        targetPosition.sub(boundingBox.getCentre());
        targetPosition.normalize();
        lookingAt = targetPosition;
        activeGun.setRotation(lookingAt);
    }

    public Vector getLookingAt() {
        return lookingAt;
    }

    public double getJumpDeltaV() {
        return JUMP_DELTA_V;
    }

    public void setJumpsLeft(int jumpsLeft) {
        this.jumpsLeft = jumpsLeft;
    }

    public int getJumpsLeft() {
        return this.jumpsLeft;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getLives() {
        return lives;
    }

    public Player setLives(int lives) {
        this.lives = lives;
        return this;
    }

    @Override
    public void update() {
        if (jumpsLeft < JUMP_AMOUNT_LIMIT) {
            action = Action.JUMPING;
        }
    }

    @Override
    public void render(Shader shader, ViewPort viewPort, long tick) {
        direction = getLookingAt().getX() > 0 ? Direction.RIGHT : Direction.LEFT;
        renderers.get(action + "." + direction).render(shader, viewPort, tick);
        renderers.get("healthBar").render(shader, viewPort, tick);
        action = Action.IDLE;
    }

}
