package ee.taltech.iti0200.physics;

import ee.taltech.iti0200.domain.Fall;
import ee.taltech.iti0200.domain.World;
import ee.taltech.iti0200.domain.entity.HealthGlobe;
import ee.taltech.iti0200.domain.entity.Player;
import ee.taltech.iti0200.domain.entity.Projectile;
import ee.taltech.iti0200.domain.event.EventBus;
import ee.taltech.iti0200.domain.event.entity.DealDamage;
import ee.taltech.iti0200.domain.event.entity.EntityCollide;
import ee.taltech.iti0200.domain.event.entity.RemoveEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static ee.taltech.iti0200.network.message.Receiver.EVERYONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServerPhysicsTest {

    @Test
    void testMoveDamageableOutOfBounds() {
        ArgumentCaptor<DealDamage> captor = ArgumentCaptor.forClass(DealDamage.class);
        UUID id = UUID.randomUUID();

        World world = new World(0, 100, 0, 100, 0.05);
        EventBus eventBus = mock(EventBus.class);
        Physics physics = new ServerPhysics(world, eventBus, id);
        Player player = new Player(new Vector(50, 50), world);
        player.setId(id);
        world.mapTerrain();
        world.addEntity(player);

        player.move(new Vector(100, 0));
        physics.update(1);

        verify(eventBus).dispatch(captor.capture());
        DealDamage event = captor.getValue();
        assertThat(event.getReceiver()).isEqualTo(EVERYONE);
        assertThat(event.getTarget().getId()).isEqualTo(id);
        assertThat(event.getSource()).isInstanceOf(Fall.class);
        assertThat(event.getTarget().getMaxHealth()).isLessThan(event.getSource().getDamage());
    }

    @Test
    void testMoveNonDamageableOutOfBounds() {
        ArgumentCaptor<RemoveEntity> captor = ArgumentCaptor.forClass(RemoveEntity.class);
        UUID id = UUID.randomUUID();

        World world = new World(0, 100, 0, 100, 0.05);
        EventBus eventBus = mock(EventBus.class);
        Physics physics = new ServerPhysics(world, eventBus, id);

        Projectile projectile = new Projectile(new Vector(50, 50), new Vector(100, 0), 1, null);
        projectile.setId(id);
        world.mapTerrain();
        world.addEntity(projectile);

        projectile.move(new Vector(100, 0));
        physics.update(1);

        verify(eventBus).dispatch(captor.capture());
        RemoveEntity event = captor.getValue();
        assertThat(event.getReceiver()).isEqualTo(EVERYONE);
        assertThat(event.getId()).isEqualTo(id);
    }

    @Test
    void testHealthGlobeAndPlayerCollision() {
        ArgumentCaptor<EntityCollide> captor = ArgumentCaptor.forClass(EntityCollide.class);
        UUID playerId = UUID.randomUUID();
        UUID globeId = UUID.randomUUID();

        World world = new World(0, 100, 0, 100, 0.05);
        EventBus eventBus = mock(EventBus.class);
        Physics physics = new ServerPhysics(world, eventBus, playerId);
        world.mapTerrain();

        Player player = new Player(new Vector(50, 50), world);
        player.setId(playerId);
        HealthGlobe healthGlobe = new HealthGlobe(new Vector(50, 50));
        healthGlobe.setId(globeId);

        world.addEntity(player);
        world.addEntity(healthGlobe);

        physics.update(1);

        verify(eventBus).dispatch(captor.capture());
        EntityCollide event = captor.getValue();
        assertThat(event.getReceiver()).isEqualTo(EVERYONE);
        assertThat(event.getOther().getId()).isEqualTo(playerId);
        assertThat(event.getEntity().getId()).isEqualTo(globeId);
    }

}
