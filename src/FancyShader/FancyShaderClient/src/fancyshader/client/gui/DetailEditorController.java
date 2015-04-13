/* 
 * Copyright (C) 2014 Reuben Steenekamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fancyshader.client.gui;

import fancyshader.entity.ShaderDemo;
import fancyshader.service.FancyShaderInternalException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the DetailEditor GUI
 *
 * @author Reuben Steenekamp
 */
public class DetailEditorController {

    @FXML
    private Pane rootPane;
    @FXML
    private Label topLabel;
    @FXML
    private TextField titleField;
    @FXML
    private TextField tagField;
    @FXML
    private ChoiceBox<String> availabilityBox;
    @FXML
    private Button doneButton;

    private String previousTitle = null;
    private ConsumerWithException<ShaderDemo> callback;
    private MainController mainController;

    public void initialize() {
        availabilityBox.getItems().addAll("Private", "Public");
    }

    /**
     * Set the shader demo to use
     * @param demo the shader demo to use
     */
    public void setShaderDemo(ShaderDemo demo) {
        previousTitle = demo.getTitle();
        titleField.setText(previousTitle == null ? "" : previousTitle);
        String joinedTags = Helper.toDelimitedTags(demo.getTags());
        tagField.setText(joinedTags);
        availabilityBox.getSelectionModel().select(demo.getAvailability());
    }

    /**
     * Set the callback to use to give the result
     * @param callback the callback to use
     */
    public void setCallback(ConsumerWithException<ShaderDemo> callback) {
        this.callback = callback;
    }

    public static final String TOP_LABEL_TEXT = "Enter demo details:";

    private void updateDialog() {
        // Validate the fields, update the top label to show the message and
        // disable the done button if not valid
        String text = TOP_LABEL_TEXT;
        boolean doneEnabled = true;
        try {
            if (titleField.getText().length() < 4) {
                text = "Title must be >= 4 characters";
            }
            if (!titleField.getText().equals(previousTitle) && mainController != null && mainController.getService().isDemoTitleTaken(titleField.getText())) {
                text = "Title is already taken";
                doneEnabled = false;
            }
        } catch (RemoteException | FancyShaderInternalException ex) {
            text = Helper.getErrorText(ex);
            Logger.getLogger(DetailEditorController.class.getName()).log(Level.SEVERE, null, ex);
            doneEnabled = false;
        }
        topLabel.setText(text);
        doneButton.setDisable(!doneEnabled);

    }

    @FXML
    public void onTitleFieldChanged(KeyEvent e) {
        updateDialog();
    }

    @FXML
    public void onDonePressed(ActionEvent e) {
        String title = titleField.getText();
        ShaderDemo demo = new ShaderDemo();
        int availability = availabilityBox.getSelectionModel().getSelectedIndex();
        demo.setTitle(title);
        demo.setTags(Helper.fromDelimitedTags(tagField.getText()));
        demo.setAvailability(availability);
        doCallback(demo);
    }

    private void doCallback(ShaderDemo demo) {
        // Try and perform the callback
        // show the error if an exception occurs
        try {
            callback.accept(demo);
            exitDialog();
        } catch (Exception ex) {
            topLabel.setText(Helper.getErrorText(ex));
            Logger.getLogger(DetailEditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exitDialog() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    public void onCancelPressed(ActionEvent e) {
        exitDialog();
    }

    /**
     * Set the main controller
     * @param mainController the main controller to set
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
