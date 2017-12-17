/*package br.inf.ufrgs.qrcode.controllers;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class CustomSizeDialog extends Dialog{
    public CustomSizeDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Insert new size values: ");

        ButtonType confirmValuesType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        NumberTextField width = new NumberTextField();
        width.setPromptText("Width");
        NumberTextField height = new NumberTextField();
        height.setPromptText("Height");

        grid.add(new Label("New width:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("New height:"), 0, 1);
        grid.add(new Label("New width:"),1, 1);

        Node confirmValues = dialog.getDialogPane().lookupButton(confirmValuesType);
        confirmValues.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter();
    }
}
*/