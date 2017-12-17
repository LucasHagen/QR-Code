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

    private static ErrorCorrectionLevel ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;
    private static int PIXEL_SIZE = 3;

    private String text;
    private QRCode qrCode;
    private int dimension;
    private BitMatrix dataMask;

    public static QRCodeImage fromImage(String text, Image image) throws WriterException {
        QRCode qrCode = getQrCode(text);

        return new QRCodeImage(text, qrCode, image);
    }

    private QRCodeImage(String text, QRCode qrCode, Image sourceImage) throws WriterException {
        super(qrCode.getVersion().getDimensionForVersion() * PIXEL_SIZE, qrCode.getVersion().getDimensionForVersion() * PIXEL_SIZE);

        this.text = text;
        this.qrCode = qrCode;
        this.dimension = qrCode.getVersion().getDimensionForVersion();

        getQRCodeDataMask();

        if(sourceImage != null) {
            if(!(sourceImage instanceof HBImage))
                sourceImage = new HBImage(sourceImage);

            sourceImage = ((HBImage) sourceImage).resizeImage((int) getWidth(), (int) getHeight());
        }

        build(sourceImage);
    }

    private void build(Image sourceImage) {

        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (dataMask.get(x, y)) {
                    if (qrCode.getMatrix().get(x, y) == 0) {
                        setDataPixel(x, y, Color.WHITE, sourceImage);
                    } else {
                        setDataPixel(x, y, Color.BLACK, sourceImage);
                    }
                } else {
                    if (qrCode.getMatrix().get(x, y) == 0) {
                        setPixel(x, y, Color.WHITE);
                    } else {
                        setPixel(x, y, Color.BLACK);
                    }
                }
            }
        }

    }

    private void setPixel(int x, int y, Color color) {
        x *= PIXEL_SIZE;
        y *= PIXEL_SIZE;

        for (int i = 0; i < PIXEL_SIZE; i++) {
            for (int j = 0; j < PIXEL_SIZE; j++) {
                getPixelWriter().setColor(x + i, y + j, color);
            }
        }
    }

    private void setDataPixel(int x, int y, Color color, Image sourceImage) {
        x *= PIXEL_SIZE;
        y *= PIXEL_SIZE;

        for (int i = 0; i < PIXEL_SIZE; i++) {
            for (int j = 0; j < PIXEL_SIZE; j++) {

                if((i == 0 || j == 0 || i == PIXEL_SIZE - 1 || j == PIXEL_SIZE - 1) && sourceImage != null) {
                    getPixelWriter().setColor(x + i, y + j, sourceImage.getPixelReader().getColor(x + i, y + i));
                } else {
                    getPixelWriter().setColor(x + i, y + j, color);
                }

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

}
