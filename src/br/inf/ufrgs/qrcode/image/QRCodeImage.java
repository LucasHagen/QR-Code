package br.inf.ufrgs.qrcode.image;

import br.inf.ufrgs.qrcode.utils.QRCodeUtils;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Random;

/**
 * @author Lucas Hagen.
 */
public class QRCodeImage extends WritableImage {

    private static ErrorCorrectionLevel ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;
    private static int PIXEL_SIZE = 3;

    private Random random;

    private String text;
    private QRCode qrCode;
    private BitMatrix dataMask;
    private Map<EncodeHintType, String> hints;

    public QRCodeImage() {
        super(300, 300);
    }

    public QRCodeImage(String text, double width, double height) throws WriterException {
        super((int) width, (int) height);

        this.random = new Random(System.currentTimeMillis());
        this.text = text;
        getQrCode();
        getQRCodeDataMask();


        for (int x = 0; x < getQRCodeDataMask().getWidth(); x++) {
            for (int y = 0; y < getQRCodeDataMask().getHeight(); y++) {
                if (getQRCodeDataMask().get(x, y)) {
                    if (getQrCode().getMatrix().get(x, y) == 0)
                        setDataPixel(x, y, Color.WHITE);
                    else
                        setDataPixel(x, y, Color.BLACK);
                } else {
                    if (getQrCode().getMatrix().get(x, y) == 0)
                        setPixel(x, y, Color.WHITE);
                    else
                        setPixel(x, y, Color.BLACK);
                }
            }
        }

    }

    public void setPixel(int x, int y, Color color) {
        x *= PIXEL_SIZE;
        y *= PIXEL_SIZE;

        for (int i = 0; i < PIXEL_SIZE; i++) {
            for (int j = 0; j < PIXEL_SIZE; j++) {
                getPixelWriter().setColor(x + i, y + j, color);
            }
        }
    }

    public void setDataPixel(int x, int y, Color color) {
        x *= PIXEL_SIZE;
        y *= PIXEL_SIZE;

        for (int i = 0; i < PIXEL_SIZE; i++) {
            for (int j = 0; j < PIXEL_SIZE; j++) {
                if((i == 0 || j == 0 || i == PIXEL_SIZE - 1 || j == PIXEL_SIZE - 1) && random.nextBoolean()) {
                    if (random.nextBoolean())
                        getPixelWriter().setColor(x + i, y + j, Color.BLACK);
                    else
                        getPixelWriter().setColor(x + i, y + j, Color.WHITE);
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

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        qrCode = Encoder.encode(text, ERROR_CORRECTION_LEVEL);
        return qrCode;
    }

}
