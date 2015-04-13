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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the ConfirmExit GUI.
 * @author Reuben Steenekamp
 */
public class ConfirmExitController {
    @FXML private Pane rootPane;
    @FXML private Label topLabel;
    public static enum Result {
        SAVE,
        EXIT
    }
    private ConsumerWithException<Result> callback;
    /**
     * Set the callback to use to give the result
     * @param callback the callback to use
     */
    public void setCallback(ConsumerWithException<Result> callback) {
        this.callback = callback;
    }
    
    private void doCallback(Result result) {
        // Try and perform the callback
        // show the error if an exception occurs
        try {
            callback.accept(result);
            exitDialog();
        } catch (Exception ex) {
            topLabel.setText(Helper.getErrorText(ex));
            Logger.getLogger(ConfirmExitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void exitDialog() {
            ((Stage)rootPane.getScene().getWindow()).close();
    }
    @FXML
    public void onSavePressed(ActionEvent e) {
        doCallback(Result.SAVE);
    }
    
    @FXML 
    public void onExitPressed(ActionEvent e) {
        doCallback(Result.EXIT);
    }
    
    @FXML 
    public void onCancelPressed(ActionEvent e) {
        exitDialog();
    }
}
