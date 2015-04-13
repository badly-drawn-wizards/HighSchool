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

import fancyshader.client.viewer.DemoViewer;
import fancyshader.entity.ShaderDemo;
import fancyshader.service.FancyShaderSessionExpiredException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the Editor GUI
 *
 * @author Reuben Steenekamp
 */
public class EditorController {
    private MainController mainController;
    private ShaderDemo shaderDemo;

    @FXML private Pane rootPane;
    @FXML private TextArea codeArea;
    @FXML private DemoViewer viewer;
    
    @FXML private MenuItem playMenuItem;
    @FXML private MenuItem pauseMenuItem;
    @FXML private MenuItem resetMenuItem;
    
    private boolean unsavedWork = false;
    
    public void initialize() {
        updateMenuItems();
    }    
    
    /**
     * Set the main controller
     * @param mainController the main controller to set 
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        viewer.getDemoAnimator().play();
        
        ((Stage)rootPane.getScene().getWindow()).onCloseRequestProperty().set((e)->{
            // On an request to close
            
            // Vito the close request
            e.consume();
            
            // Open a confirm exit dialog if there is unsaved work, otherwise just exit
            if(unsavedWork) {
                mainController.createConfirmExit(new Stage(), rootPane, (result)->{
                    switch(result){
                        case SAVE:
                            save();
                            exitWindow();
                            break;
                        case EXIT:
                            exitWindow();
                            break;
                        default:
                            throw new AssertionError(result.name());

                    }
                });
            }
            else exitWindow();
        });
        codeArea.textProperty().addListener((observable, oldValue, newValue)-> {
            unsavedWork = true;
            updatePreview();
        });
    }
    
    private void save() {
        try {
            // Set the code to that of the code area
            shaderDemo.setCode(codeArea.getText());
            
            // Update the demo using FancyShaderRemoteService
            mainController.getService().update(mainController.getSessionProperty().getValue(), shaderDemo);
            
            // Clear the unsaved work flag
            unsavedWork = false;
            
            // Trigger a refresh, as the change in code must be reflected in the browser
            mainController.refresh();
        } catch (FancyShaderSessionExpiredException | RemoteException ex) {
            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void exitWindow() {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    /**
     * Set the shader demo to use
     * @param shaderDemo the shader demo to use
     */
    public void setShaderDemo(ShaderDemo shaderDemo) {
        this.shaderDemo = shaderDemo;
        codeArea.setText(shaderDemo.getCode());
        unsavedWork = false;
    }
    
    private void updatePreview() {
        if(shaderDemo != null)
            viewer.setCode(codeArea.getText());
    }
    
    
    @FXML
    public void onAboutPressed(ActionEvent e) {
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("file://"+Helper.getUserPath().getPath().replace(" ", "%20")+"/resources/about.html"));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }
    
    @FXML
    public void onHelpContentPressed(ActionEvent e) {
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("file://"+Helper.getUserPath().getPath().replace(" ", "%20")+"/resources/editor.html"));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }
    
    @FXML
    public void onSavePressed(ActionEvent e) {
        save();
    }
    
    @FXML
    public void onEditDetailsPressed(ActionEvent e) {
        // Open a detail editor
        mainController.createDetailEditor(new Stage(), shaderDemo, rootPane, (ShaderDemo input)->{
            // When done
            
            // Set the details of the demo
            shaderDemo.setTitle(input.getTitle());
            shaderDemo.setTags(input.getTags());
            shaderDemo.setAvailability(input.getAvailability());
            
            // Update the demo using FancyShaderRemoteService
            mainController.getService().update(mainController.getSessionProperty().getValue(), shaderDemo);
            
            // Trigger a refresh, as the change in the name of the demo must be reflected in the browser
            mainController.refresh();
        });
    }
    
    @FXML
    public void onDeletePressed(ActionEvent e) {
        try {
            // Delete the demo using FancyShaderRemoteService
            mainController.getService().delete(mainController.getSessionProperty().getValue(), shaderDemo);
            
            // Trigger a refresh, as the removal of the demo must be reflected in the browser
            mainController.refresh();
            exitWindow();
        } catch (RemoteException | FancyShaderSessionExpiredException ex) {
            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML 
    public void onPlayPressed(ActionEvent e) {
        viewer.getDemoAnimator().play();
        
        // The play menu item is disabled when the viewer is playing
        // so the menu items need to be updated
        updateMenuItems();
    }
    
    @FXML 
    public void onPausePressed(ActionEvent e) {
        viewer.getDemoAnimator().pause();
        
        // The pause menu item is disabled when the viewer is paused
        // so the menu items need to be updated
        updateMenuItems();
    }
    
    @FXML 
    public void onResetPressed(ActionEvent e) {
        viewer.getDemoAnimator().reset();
    }

    public void updateMenuItems() {
        playMenuItem.setDisable(viewer.getDemoAnimator().isPlaying());
        pauseMenuItem.setDisable(!viewer.getDemoAnimator().isPlaying());
    }
}
