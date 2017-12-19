package br.inf.ufrgs.qrcode.image;

import br.inf.ufrgs.qrcode.utils.QRCodeUtils;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * @author Lucas Hagen.
 */
public class QRCodeImage extends WritableImage {

    private static ErrorCorrectionLevel ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.H;
    public static int MODULE_SIZE = 12;
    private static int PIXEL_SIZE = MODULE_SIZE / 3;

    private String text;
    private QRCode qrCode;
    private int dimension;
    private BitMatrix dataMask;

    public static QRCodeImage fromImage(String text, Image image, boolean keepAspectRatio) throws WriterException {
        QRCode qrCode = getQrCode(text);
        return new QRCodeImage(text, qrCode, image, keepAspectRatio);
    }

    private QRCodeImage(String text, QRCode qrCode, Image sourceImage, boolean keepAspectRatio) throws WriterException {
        super(qrCode.getVersion().getDimensionForVersion() * MODULE_SIZE, qrCode.getVersion().getDimensionForVersion() * MODULE_SIZE);

        this.text = text;
        this.qrCode = qrCode;
        this.dimension = qrCode.getVersion().getDimensionForVersion();

        getQRCodeDataMask();

        if(sourceImage != null) {
            if(!(sourceImage instanceof HBImage))
                sourceImage = new HBImage(sourceImage);

            sourceImage = ((HBImage) sourceImage).resizeImage((int) getWidth(), (int) getHeight(), keepAspectRatio);
        }

        build(sourceImage);
    }

    private void build(Image sourceImage) {
        if(sourceImage != null)
            getPixelWriter().setPixels(0, 0, (int) getWidth(), (int) getHeight(), sourceImage.getPixelReader(), 0, 0);


        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (dataMask.get(x, y) && sourceImage != null) {
                    setDataPixel(x, y, getColor(qrCode.getMatrix().get(x, y)));
                } else {
                    setPixel(x, y, getColor(qrCode.getMatrix().get(x, y)));
                }
            }
        }

        fillIfTransparent(Color.WHITE);

    }

    private void setPixel(int x, int y, Color color) {
        x *= MODULE_SIZE;
        y *= MODULE_SIZE;

        for (int i = 0; i < MODULE_SIZE; i++) {
            for (int j = 0; j < MODULE_SIZE; j++) {
                getPixelWriter().setColor(x + i, y + j, color);
            }
        }
    }

    private void setDataPixel(int x, int y, Color color) {
        x = x * MODULE_SIZE + PIXEL_SIZE;
        y = y * MODULE_SIZE + PIXEL_SIZE;

        for (int i = 0; i < PIXEL_SIZE; i++) {
            for (int j = 0; j < PIXEL_SIZE; j++) {
                getPixelWriter().setColor(x + i, y + j, color);
            }
        }
    }

    private BitMatrix getQRCodeDataMask() throws WriterException {
        if (dataMask != null)
            return dataMask;

        ByteMatrix matrix = new ByteMatrix(qrCode.getVersion().getDimensionForVersion(), qrCode.getVersion().getDimensionForVersion());
        QRCodeUtils.buildMatrix(ERROR_CORRECTION_LEVEL, qrCode.getVersion(), qrCode.getMaskPattern(), matrix);

        dataMask = new BitMatrix(matrix.getWidth(), matrix.getHeight());

        for (int x = 0; x < dataMask.getWidth(); x++) {
            for (int y = 0; y < dataMask.getHeight(); y++) {
                if (matrix.get(x, y) == -1) {
                    dataMask.set(x, y);
                } else {
                    dataMask.unset(x, y);
                }
            }
        }

        return dataMask;
    }

    private QRCode getQrCode() throws WriterException {
        if (qrCode != null)
            return qrCode;

        qrCode = Encoder.encode(text, ERROR_CORRECTION_LEVEL);
        return qrCode;
    }

    private static QRCode getQrCode(String text) throws WriterException {
        return Encoder.encode(text, ERROR_CORRECTION_LEVEL);
    }

    private static Color getColor(byte value) {
        switch (value) {
            case 0:
                return Color.WHITE;
            case 1:
                return Color.BLACK;
            default:
                return Color.color(0,0,0,0);
        }
    }

    private void fillIfTransparent(Color color) {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                if(getPixelReader().getColor(x, y).getOpacity() != 1) {
                    getPixelWriter().setColor(x, y, color);
                }
            }
        }
    }

}
