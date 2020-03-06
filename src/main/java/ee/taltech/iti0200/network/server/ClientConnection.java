package ee.taltech.iti0200.network.server;

import ee.taltech.iti0200.network.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class ClientConnection {

    private final ConcurrentLinkedQueue<Message> tcpOutbox = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Message> udpOutbox = new ConcurrentLinkedQueue<>();
    private final Logger logger = LogManager.getLogger(ClientConnection.class);
    private ObjectInputStream tcpInput;
    private ObjectOutputStream tcpOutput;
    private ObjectInputStream udpInput;
    private ObjectOutputStream udpOutput;
    private UUID id;
    private InetAddress address;
    private Integer tcpPort;
    private Integer udpPort;

    public ClientConnection(InetAddress address, Integer tcpPort) {
        this.address = address;
        this.tcpPort = tcpPort;
    }

    public InetAddress getAddress() {
        return address;
    }

    public ConcurrentLinkedQueue<Message> getTcpOutbox() {
        return tcpOutbox;
    }

    public ConcurrentLinkedQueue<Message> getUdpOutbox() {
        return udpOutbox;
    }

    public ObjectInputStream getUdpInput() {
        return udpInput;
    }

    public ClientConnection setUdpInput(ObjectInputStream udpInput) {
        this.udpInput = udpInput;
        return this;
    }

    public ObjectOutputStream getUdpOutput() {
        return udpOutput;
    }

    public ClientConnection setUdpOutput(ObjectOutputStream udpOutput) {
        this.udpOutput = udpOutput;
        return this;
    }

    public ObjectInputStream getTcpInput() {
        return tcpInput;
    }

    public ClientConnection setTcpInput(ObjectInputStream tcpInput) {
        this.tcpInput = tcpInput;
        return this;
    }

    public ObjectOutputStream getTcpOutput() {
        return tcpOutput;
    }

    public ClientConnection setTcpOutput(ObjectOutputStream tcpOutput) {
        this.tcpOutput = tcpOutput;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public ClientConnection setId(UUID id) {
        this.id = id;
        return this;
    }

    public Integer getUdpPort() {
        return udpPort;
    }

    public ClientConnection setUdpPort(Integer udpPort) {
        this.udpPort = udpPort;
        return this;
    }

    public void close() {
        Stream.of(tcpInput, tcpOutput)
            .forEach(closeable -> {
                if (closeable == null) {
                    return;
                }
                try {
                    closeable.close();
                } catch (IOException e) {
                    logger.error("Failed to close " + closeable.getClass() + " with: " + e.getMessage(), e);
                }
            });
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, tcpPort);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ClientConnection connection = (ClientConnection) other;
        return Objects.equals(address, connection.address) && Objects.equals(tcpPort, connection.tcpPort);
    }

}
