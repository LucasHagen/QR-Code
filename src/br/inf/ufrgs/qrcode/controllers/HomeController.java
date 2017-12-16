package br.inf.ufrgs.qrcode.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML TextArea textInput;
    @FXML ImageView originalImage;
    @FXML ImageView halftoneImage;
    @FXML ImageView qrCodeImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInput.textProperty().addListener((observable, oldValue, newValue) -> setQRCode(newValue));
    }

    private void setQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 300;
        int height = 300;

        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        if(bufferedImage != null)
            qrCodeImage.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

    }



}