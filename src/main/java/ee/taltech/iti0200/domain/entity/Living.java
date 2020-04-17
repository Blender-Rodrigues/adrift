package ee.taltech.iti0200.domain.entity;

import ee.taltech.iti0200.domain.World;
import ee.taltech.iti0200.graphics.Camera;
import ee.taltech.iti0200.graphics.Shader;
import ee.taltech.iti0200.physics.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class Living extends Damageable {

    private static final long serialVersionUID = 1L;

    protected transient World world;
    protected boolean alive = true;
    protected List<Gun> weapons;
    protected List<Equipment> equipment;
    protected Gun activeGun;

    protected enum Action {
        RUNNING, JUMPING, FALLING, IDLE;
    }

    protected enum Direction {
        LEFT, RIGHT;
    }

    protected Action action = Action.IDLE;
    protected Direction direction = Direction.LEFT;

    public Living(double mass, BoundingBox boundingBox, World world, int health) {
        super(mass, boundingBox, health);
        this.world = world;
        this.movable = true;
        this.weapons = new ArrayList<>();
        this.equipment = new ArrayList<>();
    }

    public Gun getActiveGun() {
        return activeGun;
    }

    public void setActiveGun(int index) {
        if (activeGun != null) activeGun.setActive(false);
        activeGun = weapons.get(index);
        activeGun.setActive(true);
    }

    public void addWeapon(Gun weapon) {
        weapons.add(weapon);
        equipment.add(weapon);
        weapon.setOwner(this);
    }

    public List<Gun> getWeapons() {
        return weapons;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isAlive() {
        return alive;
    }

    public Living setAlive(boolean alive) {
        this.alive = alive;
        return this;
    }

    public void update() {

    }

    @Override
    public void render(Shader shader, Camera camera, long tick) {
        super.render(shader, camera, tick);
        renderers.get("healthBar").render(shader, camera, tick);
    }

}
