package br.inf.ufrgs.qrcode.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Lucas Hagen.
 */
public class QRCodeImage extends WritableImage {

    private static ErrorCorrectionLevel ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;

    private QRCode qrCode;
    private Map<EncodeHintType, String> hints;

    public QRCodeImage(String text, double width, double height) throws WriterException {
        super((int) width, (int) height);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        qrCode = Encoder.encode(text, ERROR_CORRECTION_LEVEL);
        BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, (int) width, (int) height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (byteMatrix.get(i, j)) {
                    getPixelWriter().setColor(i, j, Color.BLACK);
                }
            }
        }

        System.out.println(qrCode.getVersion().getDimensionForVersion());
        System.out.println(Arrays.toString(qrCode.getVersion().getAlignmentPatternCenters()));

    }

    private Map<EncodeHintType, ?> getHints() {
        if(hints == null) {
            hints = new LinkedHashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, Integer.toString(ERROR_CORRECTION_LEVEL.getBits()));
        }
        return hints;
    }

}
