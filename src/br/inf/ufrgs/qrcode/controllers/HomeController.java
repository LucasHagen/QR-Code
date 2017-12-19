package br.inf.ufrgs.qrcode.controllers;

import br.inf.ufrgs.qrcode.Main;
import br.inf.ufrgs.qrcode.image.HBImage;
import br.inf.ufrgs.qrcode.image.Mode;
import br.inf.ufrgs.qrcode.image.QRCodeImage;
import com.google.zxing.WriterException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private Mode mode = Mode.SIMPLE_HALFTONE;

    private QRCodeImage qrCode;

    @FXML
    TextArea textInput;
    @FXML
    ImageView originalImage;
    @FXML
    ImageView sourceImage;
    @FXML
    ImageView qrCodeImage;

    @FXML
    CheckMenuItem keepAspectRatio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInput.textProperty().addListener((observable, oldValue, newValue) -> updateQRCode());
    }

    public void updateQRCode() {
        if (textInput.getText() != null && textInput.getText().isEmpty()) {
            return;
        }

        try {
            if (mode == Mode.COLOR_IMAGE)
                qrCode = QRCodeImage.fromImage(textInput.getText(), originalImage.getImage(), keepAspectRatio.isSelected());
            else
                qrCode = QRCodeImage.fromImage(textInput.getText(), sourceImage.getImage(), keepAspectRatio.isSelected());
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if (qrCode != null)
            qrCodeImage.setImage(qrCode);
    }

    public void loadNewImage() {
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = chooser.showOpenDialog(Main.getInstance().getStage());

        if (file == null)
            return;

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            originalImage.setImage(image);

            updateSourceImage();
            updateQRCode();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setModeToSimpleHalftone() {
        mode = Mode.SIMPLE_HALFTONE;
        updateSourceImage();
        updateQRCode();
    }

    public void setModeToErrorDiffusionHalftone() {
        mode = Mode.ERROR_DIFFUSAL_HALFTONE;
        updateSourceImage();
        updateQRCode();
    }

    public void setModeToDitheringHalftone() {
        mode = Mode.DITHERING_HALFTONE;
        updateSourceImage();
        updateQRCode();
    }

    public void setModeToColorImage() {
        mode = Mode.COLOR_IMAGE;
        updateSourceImage();
        updateQRCode();
    }


    private void updateSourceImage() {
        if (originalImage.getImage() == null)
            return;

        HBImage image = new HBImage(originalImage.getImage().getPixelReader(), originalImage.getImage().getWidth(), originalImage.getImage().getHeight());

        switch (mode) {
            case SIMPLE_HALFTONE:
                image.toHalftone();
                break;
            case ERROR_DIFFUSAL_HALFTONE:
                image.toHalftoneErrorDiffusion();
                break;
            case DITHERING_HALFTONE:
                image.toHalftoneDithering();
                break;
        }

        sourceImage.setImage(image);
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Fundamentals of Image Processing\n" +
                "Image-Based QR Code"
        );
        alert.setContentText("Authors:\n" +
                " - Lucas Hagen\n" +
                " - Leonardo Bombardelli\n\n" +
                " Professor: Manuel de Oliveira Neto"
        );

        alert.showAndWait();
    }

    public void saveSourceImage() {
        saveImage(sourceImage.getImage());
    }

    public void saveQRCode() {
        saveImage(qrCode);
    }

    private void saveImage(Image image) {
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        chooser.getExtensionFilters().addAll(extFilterJPG);

        File file = chooser.showSaveDialog(Main.getInstance().getStage());

        if(file == null)
            return;

        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
