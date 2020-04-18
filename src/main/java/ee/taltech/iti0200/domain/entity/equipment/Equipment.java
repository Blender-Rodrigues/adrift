package ee.taltech.iti0200.domain.entity.equipment;

import ee.taltech.iti0200.domain.entity.Entity;
import ee.taltech.iti0200.domain.entity.Living;
import ee.taltech.iti0200.graphics.renderer.Renderer;
import ee.taltech.iti0200.physics.BoundingBox;

import static ee.taltech.iti0200.graphics.renderer.EntityRenderFacade.DEFAULT;

public class Equipment extends Entity {

    protected Living owner;
    protected boolean isActive;

    public Equipment(BoundingBox boundingBox) {
        super(0, boundingBox);
    }

    public Living getOwner() {
        return owner;
    }

    public Renderer getRenderer() {
        return renderers.get(DEFAULT);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

}