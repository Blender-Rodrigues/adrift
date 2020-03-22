package ee.taltech.iti0200.graphics;

import ee.taltech.iti0200.physics.BoundingBox;
import org.joml.Vector3f;

import static ee.taltech.iti0200.graphics.Graphics.defaultTexture;

public interface Drawable {

    default void initializeGraphicsTest() {
        float[] vertices = new float[]{
            -1f, 1f, 0,
            1f, 1f, 0,
            1f, -1f, 0,
            -1f, -1f, 0
        };

        float[] texture = new float[]{
            0, 0,
            1, 0,
            1, 1,
            0, 1
        };

        int[] indices = new int[]{
            0, 1, 2,
            2, 3, 0
        };

//        this.model = new Model(vertices, texture, indices);
        setModel(new Model(vertices, texture, indices));
//        this.texture = defaultTexture;
//        this.texture = new Animation(6, "anim");
        //todo create texture in the class that implements this.
        setTexture(defaultTexture);



        Transform transform = new Transform();
        transform.scale = new Vector3f((float) getBoundingBox().getSize().getX(), (float) getBoundingBox().getSize().getY(), 1);
        setTransform(transform);
    }

//    public void changeTexture(String filename) {
//        this.texture = new Texture(filename);
//    }

    default void renderTest(Shader shader, Camera camera, long tick) {
        getTransform().pos.set(new Vector3f((float) getBoundingBox().getCentre().x, (float) getBoundingBox().getCentre().y, 0));

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", this.getTransform().getProjection(camera.getProjection()));
        getTexture().bind(0);
        getModel().render();
    }

    void setModel(Model model);

    Model getModel();

    void setTransform(Transform transform);

    Transform getTransform();

    void setTexture(Texture texture);

    Texture getTexture();

    BoundingBox getBoundingBox();
}
