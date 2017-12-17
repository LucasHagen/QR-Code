package br.inf.ufrgs.qrcode.controllers;

import br.inf.ufrgs.qrcode.Main;
import br.inf.ufrgs.qrcode.image.HBImage;
import br.inf.ufrgs.qrcode.image.Mode;
import br.inf.ufrgs.qrcode.image.QRCodeImage;
import com.google.zxing.WriterException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
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

    @FXML TextArea textInput;
    @FXML ImageView originalImage;
    @FXML ImageView halftoneImage;
    @FXML ImageView qrCodeImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInput.textProperty().addListener((observable, oldValue, newValue) -> setQRCode(newValue));
    }

    private void setQRCode(String text) {
        if(text.isEmpty()) {
            return;
        }

        try {
            qrCode = new QRCodeImage(text, qrCodeImage.getFitWidth(), qrCodeImage.getFitHeight());
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if(qrCode != null)
            qrCodeImage.setImage(qrCode);
    }

    public void loadNewImage() {
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = chooser.showOpenDialog(Main.getInstance().getStage());

        if(file == null)
            return;

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            originalImage.setImage(image);

            updateHalftoneImage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setModeToSimpleHalftone(){
        mode = Mode.SIMPLE_HALFTONE;
        updateHalftoneImage();
    }

    public void setModeToErrorDiffusionHalftone(){
        mode = Mode.ERROR_DIFFUSAL_HALFTONE;
        updateHalftoneImage();
    }

    public void setModeToDitheringHalftone(){
        mode = Mode.DITHERING_HALFTONE;
        updateHalftoneImage();
    }


    private void updateHalftoneImage() {
        if(originalImage.getImage() == null)
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
            default:
                image.toHalftone();
                break;
        }


        halftoneImage.setImage(image);
    }

}
