package ee.taltech.iti0200.network.server;

import com.google.inject.Inject;
import ee.taltech.iti0200.domain.Score;
import ee.taltech.iti0200.domain.World;
import ee.taltech.iti0200.domain.entity.Entity;
import ee.taltech.iti0200.domain.entity.Player;
import ee.taltech.iti0200.domain.event.EventBus;
import ee.taltech.iti0200.domain.event.UpdateScore;
import ee.taltech.iti0200.domain.event.entity.CreateEntity;
import ee.taltech.iti0200.domain.event.entity.CreatePlayer;
import ee.taltech.iti0200.domain.event.Subscriber;
import ee.taltech.iti0200.network.message.Receiver;
import ee.taltech.iti0200.network.Messenger;
import ee.taltech.iti0200.network.message.LoadWorld;
import ee.taltech.iti0200.physics.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static ee.taltech.iti0200.network.message.Receiver.ALL_CLIENTS;
import static java.util.Arrays.asList;

public class PlayerJoinHandler implements Subscriber<CreatePlayer> {

    private final Logger logger = LogManager.getLogger(PlayerJoinHandler.class);

    private World world;
    private Messenger messenger;
    private EventBus eventBus;
    private Score score;

    @Inject
    public PlayerJoinHandler(World world, Messenger messenger, EventBus eventBus, Score score) {
        this.world = world;
        this.messenger = messenger;
        this.eventBus = eventBus;
        this.score = score;
    }

    @Override
    public void handle(CreatePlayer event) {
        if (world.getEntity(event.getId()) != null) {
            logger.warn("Player {} is already present in world.", event.getId());
            return;
        }

        ArrayList<Entity> entities = new ArrayList<>(world.getEntities());

        Vector position = world.nextSpawnPoint();

        Player player = (Player) event.getEntity();
        player.setPosition(position);

        world.addEntity(player);
        score.addPlayer(player);

        logger.info("Added new {} to the world", player);

        messenger.writeOutbox(asList(
            new CreateEntity(player, new Receiver(event.getId()).exclude()),
            new LoadWorld(entities, position, new Receiver(event.getId()))
        ));
        eventBus.dispatch(new UpdateScore(score.getPlayerScores(), ALL_CLIENTS));
    }

}
