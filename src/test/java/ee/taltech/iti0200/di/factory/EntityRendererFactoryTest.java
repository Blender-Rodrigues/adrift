package ee.taltech.iti0200.di.factory;

import ee.taltech.iti0200.domain.Score;
import ee.taltech.iti0200.graphics.CoordinateConverter;
import ee.taltech.iti0200.graphics.renderer.Animateable;
import ee.taltech.iti0200.graphics.Animation;
import ee.taltech.iti0200.graphics.renderer.Drawable;
import ee.taltech.iti0200.graphics.renderer.EntityRenderer;
import ee.taltech.iti0200.graphics.renderer.RotatingDrawable;
import ee.taltech.iti0200.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EntityRendererFactoryTest {

    private RendererFactory factory;
    private Score score;
    private CoordinateConverter converter;

    @BeforeEach
    void setUp() {
        score = mock(Score.class);
        converter = mock(CoordinateConverter.class);
        factory = new RendererFactory(score, converter);
    }

    @Test
    void createReturnsRotating() {
        Texture texture = mock(Texture.class);

        EntityRenderer renderer = factory.create(texture, RotatingDrawable.class);

        assertThat(renderer).isInstanceOf(RotatingDrawable.class);
    }

    @Test
    void createReturnsDrawable() {
        Texture texture = mock(Texture.class);

        factory = new RendererFactory(score, converter);

        EntityRenderer renderer = factory.create(texture);

        assertThat(renderer).isInstanceOf(Drawable.class);
    }

    @Test
    void createReturnsAnimatable() {
        Animation animation = mock(Animation.class);

        factory = new RendererFactory(score, converter);

        EntityRenderer renderer = factory.create(animation);

        assertThat(renderer).isInstanceOf(Animateable.class);
    }

}
