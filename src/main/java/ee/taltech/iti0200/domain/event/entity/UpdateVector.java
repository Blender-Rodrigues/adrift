package ee.taltech.iti0200.domain.event.entity;

import ee.taltech.iti0200.domain.entity.Entity;
import ee.taltech.iti0200.domain.event.Event;
import ee.taltech.iti0200.network.message.Receiver;
import ee.taltech.iti0200.physics.Vector;
import org.apache.logging.log4j.core.net.Protocol;

import java.util.UUID;

import static java.lang.String.format;

public class UpdateVector extends Event {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Vector position;
    private final Vector speed;
    private final long tick;

    public UpdateVector(Entity entity, long tick, Receiver receiver) {
        super(receiver);
        this.tick = tick;
        this.id = entity.getId();
        this.position = entity.getBoundingBox().getCentre();
        this.speed = entity.getSpeed();
    }

    public UUID getId() {
        return id;
    }

    public long getTick() {
        return tick;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    @Override
    public Protocol getChannel() {
        return Protocol.UDP;
    }

    @Override
    public String toString() {
        return format("UpdateVector{%s[%s] -> %s}", id, position, speed);
    }

}
