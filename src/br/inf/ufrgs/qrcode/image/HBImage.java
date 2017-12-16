package br.inf.ufrgs.qrcode.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

import java.awt.image.BufferedImage;

/**
 * HBImage
 * HagenBombardelliImage ;)
 */
public class HBImage extends WritableImage {

    public HBImage(PixelReader reader, int width, int height) {
        super(reader, width, height);

        toHalftoneErrorDiffusion();
    }

    public HBImage(PixelReader reader, double width, double height) {
        super(reader, (int) width, (int) height);

        toHalftoneErrorDiffusion();
    }

    private void toGrayScale() {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                Color original = getPixelReader().getColor(x, y);

                getPixelWriter().setColor(x, y, Color.color(original.getBrightness(), original.getBrightness(), original.getBrightness()));
            }
        }
    }

    private void toHalftone() {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                Color original = getPixelReader().getColor(x, y);

                if(original.getBrightness() < 1.0 / 2)
                    getPixelWriter().setColor(x, y, Color.BLACK);
                else
                    getPixelWriter().setColor(x, y, Color.WHITE);
            }
        }
    }

    private void toHalftoneErrorDiffusion() {
        MarvinImagePlugin imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.halftone.errorDiffusion.jar");
        MarvinImage image = toMarvinImage();

        imagePlugin.process(image, image);
        importMarvinImage(image);
    }

    public MarvinImage toMarvinImage() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(this, null);
        return new MarvinImage(bufferedImage);
    }

    public void importMarvinImage(MarvinImage marvinImage) {
        if(getHeight() != marvinImage.getHeight() || getWidth() != marvinImage.getWidth())
            return;

        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                getPixelWriter().setArgb(x, y, marvinImage.getIntColor(x, y));
            }
        }
    }

}

