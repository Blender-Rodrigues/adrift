package ee.taltech.iti0200.input;

import ee.taltech.iti0200.graphics.Camera;
import ee.taltech.iti0200.physics.Vector;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class Mouse {

    private Vector physicsPosition;
    private Camera camera;
    private long window;

    public Mouse(Camera camera, long window) {
        this.camera = camera;
        this.window = window;
    }

    public void update() {
        Vector currentPosition = getMousePosition();
        Vector windowSize = getWindowSize();
        screenToCamera(currentPosition, windowSize);
        physicsPosition = cameraToPhysics(currentPosition);
    }
    private Vector cameraToPhysics(Vector cameraPosition) {
        double x = cameraPosition.getX() - camera.getPosition().get(0);
        double y = cameraPosition.getY() + camera.getPosition().get(1);
        y *= -1;
        return new Vector(x, y);
    }

    private void screenToCamera(Vector screenPosition, Vector windowSize) {
        screenPosition.scaleAdd(-0.5, windowSize, screenPosition);
        screenPosition.scale(camera.getZoom());
    }

    private Vector getWindowSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return new Vector(width.get(0), height.get(0));
    }

    private Vector getMousePosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xBuffer, yBuffer);
        return new Vector(xBuffer.get(0), yBuffer.get(0));
    }

    public Vector getPhysicsPosition() {
        return physicsPosition;
    }

}