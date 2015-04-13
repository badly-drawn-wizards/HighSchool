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

import fancyshader.service.FancyShaderInternalException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the Register GUI
 *
 * @author Reuben Steenekamp
 */
public class RegisterController {

    private MainController mainController;

    /**
     * Set the main controller
     * @param mainController the main controller to set 
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML private Pane rootPane;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label topLabel;
    @FXML private Button registerButton;

    private final static String TOP_LABEL_TEXT = "Create your account:";

    public void updateDialog() {
        // Validate the fields, update the top label to show the message and
        // disable the register button if not valid
        String text = TOP_LABEL_TEXT;
        boolean valid = false;
        try {
            if (usernameField.getText().length() < 4) {
                text = "Username must be >= 4 characters";
            } else if (mainController != null && mainController.getService().isUsernameTaken(usernameField.getText())) {
                text = "Username is taken";
            } else if (passwordField.getText().length() < 4) {
                text = "Password must be >= 4 characters";
            } else valid = true;
            topLabel.setText(text);
        } catch (FancyShaderInternalException | RemoteException e) {
            topLabel.setText(Helper.getErrorText(e));
            Logger.getLogger(RegisterController.class.getName()).log(Level.WARNING, null, e);
        }
        registerButton.setDisable(!valid);
    }

    @FXML
    public void initialize() {
        registerButton.setDisable(true);
    }

    @FXML
    public void onUsernameChanged(KeyEvent e) {
        updateDialog();
    }

    @FXML
    public void onPasswordChanged(KeyEvent e) {
        updateDialog();
    }

    @FXML
    public void onRegisterPressed(ActionEvent e) {
        try {
            mainController.getService().registerAccount(usernameField.getText(), passwordField.getText());
        } catch (FancyShaderInternalException | RemoteException ex) {
            topLabel.setText(Helper.getErrorText(ex));
            return;
        }
        passwordField.setText("");
        passwordField.requestFocus();
        ((Stage) rootPane.getScene().getWindow()).close();
    }
    
    @FXML
    public void onCancelPressed(ActionEvent e) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

}
