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
import fancyshader.service.FancyShaderLoginException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the Login GUI
 *
 * @author Reuben Steenekamp
 */
public class LoginController{
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
    
    
    @FXML
    public void onLoginPressed(ActionEvent e) {
        try {
            mainController.login(usernameField.getText(), passwordField.getText());
        } catch (FancyShaderInternalException | FancyShaderLoginException | RemoteException ex) {
            topLabel.setText(Helper.getErrorText(ex));
            Logger.getLogger(LoginController.class.getName()).log(Level.FINEST, null, ex);
            return;
        } finally {
            // Clear the password field and set it as the focus
            passwordField.setText("");
            passwordField.requestFocus();
        }
        exitDialog();
    }
    
    @FXML
    public void onCancelPressed(ActionEvent e) {
        exitDialog();
    }

    private void exitDialog() {
        ((Stage)rootPane.getScene().getWindow()).close();
    }
}
