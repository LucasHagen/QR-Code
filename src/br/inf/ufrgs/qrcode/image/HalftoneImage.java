package br.inf.ufrgs.qrcode.image;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * @author Lucas Hagen.
 */
public class HalftoneImage extends WritableImage {

    public HalftoneImage(PixelReader reader, int width, int height) {
        super(reader, width, height);

        toHalftone();
    }

    public HalftoneImage(PixelReader reader, double width, double height) {
        super(reader, (int) width, (int) height);

        toHalftone();
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

    private void toHalftoneRegion() {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {

            }
        }
    }

}
