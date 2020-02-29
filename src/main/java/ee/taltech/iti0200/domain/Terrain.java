package ee.taltech.iti0200.domain;

import ee.taltech.iti0200.physics.Body;

import javax.vecmath.Vector2d;

public class Terrain extends Entity {

    private static final Vector2d size = new Vector2d(2.0, 2.0);
    private static final double mass = Double.POSITIVE_INFINITY;
    private static final double elasticity = 0.9;

    public Terrain(Vector2d position) {
        super(new Body(mass, new Vector2d(size), position, true, true), true);
        setElasticity(elasticity);
    }

}
