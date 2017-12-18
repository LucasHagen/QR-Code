package br.inf.ufrgs.qrcode.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public HBImage(Image image) {
        this(image.getPixelReader(), image.getWidth(), image.getHeight());
    }

    public HBImage(PixelReader reader, int width, int height) {
        super(reader, width, height);
    }

    public HBImage(PixelReader reader, double width, double height) {
        super(reader, (int) width, (int) height);
    }

    public HBImage(int newWidth, int newHeight) {
        super(newWidth, newHeight);
    }

    public void toGrayScale() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Color original = getPixelReader().getColor(x, y);

                getPixelWriter().setColor(x, y, Color.color(original.getBrightness(), original.getBrightness(), original.getBrightness()));
            }
        }
    }

    public void toHalftone() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Color original = getPixelReader().getColor(x, y);

                if (original.getBrightness() < 1.0 / 2)
                    getPixelWriter().setColor(x, y, Color.BLACK);
                else
                    getPixelWriter().setColor(x, y, Color.WHITE);
            }
        }
    }

    public void toHalftoneErrorDiffusion() {
        MarvinImagePlugin imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.halftone.errorDiffusion.jar");
        MarvinImage image = toMarvinImage();

        imagePlugin.process(image, image);
        importMarvinImage(image);
    }

    public void toHalftoneDithering() {
        MarvinImagePlugin imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.halftone.dithering.jar");
        MarvinImage image = toMarvinImage();

        imagePlugin.process(image, image);
        importMarvinImage(image);
    }

    public MarvinImage toMarvinImage() {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(this, null);
        return new MarvinImage(bufferedImage);
    }

    public void importMarvinImage(MarvinImage marvinImage) {
        if (getHeight() != marvinImage.getHeight() || getWidth() != marvinImage.getWidth())
            return;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                getPixelWriter().setArgb(x, y, marvinImage.getIntColor(x, y));
            }
        }
    }

    public HBImage resizeImage(int newWidth, int newHeight) {
        return resizeImage(newWidth, newHeight, false);
    }

    public HBImage resizeImage(int newWidth, int newHeight, boolean keepAspectRatio) {
        return resizeImage(this, newWidth, newHeight, keepAspectRatio);
    }

    public static HBImage resizeImage(Image image, int newWidth, int newHeight, boolean keepAspectRatio) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newHeight);
        imageView.setImage(image);
        imageView.setPreserveRatio(keepAspectRatio);

        WritableImage snapshot = imageView.snapshot(null, null);
        HBImage newImage = new HBImage(newWidth, newHeight);
        newImage.getPixelWriter().setPixels((int) (newWidth - snapshot.getWidth()) / 2,
                (int) (newHeight - snapshot.getHeight()) / 2,
                (int) snapshot.getWidth(), (int) snapshot.getHeight(),
                snapshot.getPixelReader(),
                0, 0);

        return newImage;
    }

}


