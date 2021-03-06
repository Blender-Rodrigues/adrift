package ee.taltech.iti0200.domain.entity;

import ee.taltech.iti0200.graphics.Shader;
import ee.taltech.iti0200.graphics.ViewPort;
import ee.taltech.iti0200.graphics.renderer.EntityRenderer;
import ee.taltech.iti0200.physics.Body;
import ee.taltech.iti0200.physics.BoundingBox;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static ee.taltech.iti0200.graphics.renderer.EntityRenderFacade.DEFAULT;

public class Entity extends Body {

    private static final long serialVersionUID = 1L;

    private UUID id = UUID.randomUUID();
    private boolean onFloor;
    public transient HashMap<String, EntityRenderer> renderers = new HashMap<>();

    protected boolean movable = false;

    public Entity(double mass, BoundingBox boundingBox) {
        super(mass, boundingBox);
    }

    public boolean isMovable() {
        return movable;
    }

    public boolean isOnFloor() {
        return onFloor;
    }

    public void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
    }

    public UUID getId() {
        return id;
    }

    public Entity setId(UUID id) {
        this.id = id;
        return this;
    }

    public Entity setRenderers(HashMap<String, EntityRenderer> renderers) {
        this.renderers = renderers;
        return this;
    }

    public void render(Shader shader, ViewPort viewPort, long tick) {
        renderers.get(DEFAULT).render(shader, viewPort, tick);
    }

    public boolean hasMoved() {
        return boundingBox.hasMoved();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + boundingBox.getCentre().rounded() + "[" + id + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
