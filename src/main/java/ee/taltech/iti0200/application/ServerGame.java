package ee.taltech.iti0200.application;

import ee.taltech.iti0200.ai.Intelligence;
import ee.taltech.iti0200.domain.event.entity.GunShot;
import ee.taltech.iti0200.domain.event.handler.GunShotHandler;
import ee.taltech.iti0200.network.Network;
import ee.taltech.iti0200.network.server.ServerNetwork;

import java.util.UUID;

public class ServerGame extends Game {

    public static final UUID SERVER_ID = new UUID(0, 0);

    private Network network;
    private Integer tcpPort;

    public ServerGame(Integer tcpPort) {
        super(SERVER_ID);
        this.tcpPort = tcpPort;
        Game.isServer = true;
    }

    @Override
    protected void initialize() throws Exception {
        world.initialize();
        eventBus.subscribe(GunShot.class, new GunShotHandler(world));
        network = new ServerNetwork(world, tcpPort);
        components.add(network);
        components.add(new Intelligence(world));
    }

    @Override
    protected void loop(long tick) {
        network.propagate(tick);
    }

    @Override
    protected boolean isGameRunning() {
        return true;
    }

}
