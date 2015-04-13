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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the Browser GUI
 * @author Reuben Steenekamp
 */
public class BrowserController {
    private MainController mainController;
    /**
     * Set the main controller
     * @param mainController the main controller to set 
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        Runnable callback = ()->{
            updateDemoList();
        };
        mainController.setRefreshCallback(callback);
        
        selectedItemProperty.addListener((observable, oldValue, newValue) -> { updateSelected(); });
        updateSelected(); // Initial update
    }
    
    private ObservableList<ShaderDemo> demos;
    
    @FXML private Pane rootPane;
    @FXML private TextField searchField;
    
    @FXML private ListView<ShaderDemo> demoList;
    private ReadOnlyObjectProperty<ShaderDemo> selectedItemProperty;
    
    @FXML private DemoViewer viewer;
    
    public void initialize() {
        demos = demoList.getItems();
        
        searchField.textProperty().addListener((observable, oldValue, newValue)-> { 
            // On search field change
            
            updateDemoList(); 
        }); 
        
        selectedItemProperty = demoList.getSelectionModel().selectedItemProperty();
        
        // Start the shader from the beginning when it changes, as some shaders may consist of one time events
        viewer.setResetOnChange(true); 
        
        // Set the viewer to play initially
        viewer.getDemoAnimator().play();
    }
    
    public void updateDemoList() {
        // Remove previous items
        demos.clear(); 
        
        // Check if there is a user session
        if(mainController!=null && mainController.getSessionProperty().getValue()!=null) {
            try {
                // Fetch the demos using FancyShsaderRemoteService
                List<ShaderDemo> resultDemos = mainController.getService().search(mainController.getSessionProperty().getValue(), searchField.getText());
                // Add to the observable list
                // will automatically update demoList`
                demos.addAll(resultDemos);
            } catch (FancyShaderSessionExpiredException | RemoteException ex) {
                // If the fetching fails, then the list will be empty as it had been cleared.
                // This is the behaviour that we want
                Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        
        //Since the items in the list have changed, the selection defaults to null and must be updated
        updateSelected();
    }
    
    public void updateSelected() {
        ShaderDemo selected = selectedItemProperty.getValue();
        if(selected != null) {
            try {
                mainController.getService().visit(mainController.getSessionProperty().getValue(), selectedItemProperty.getValue());
            } catch (FancyShaderSessionExpiredException | RemoteException ex) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Set the viewer's code to the selected demo's.
        // If the selected is null, then the code is null
        viewer.setCode(selected == null ? null : selected.getCode());
        
        // Menu items change state when the selected item is changed
        updateMenuItems();
    }
    
    @FXML private MenuItem loginMenuItem;
    @FXML private MenuItem registerMenuItem;
    @FXML private MenuItem logoutMenuItem;
    @FXML private MenuItem createMenuItem;
    @FXML private MenuItem forkMenuItem;
    @FXML private MenuItem editMenuItem;
    @FXML private MenuItem infoMenuItem;
    @FXML private MenuItem delightfulMenuItem;
    @FXML private MenuItem playMenuItem;
    @FXML private MenuItem pauseMenuItem;
    
    @FXML
    public void onLoginPressed(ActionEvent event) {
        // Open a login dialog
        mainController.createLogin(new Stage(), rootPane);
    }

    @FXML
    public void onRegisterPressed(ActionEvent event) {
        // Open a register dialog
        mainController.createRegister(new Stage(), rootPane);
    }

    @FXML
    public void onLogoutPressed(ActionEvent event) {
        // Attempt to logout
        try {
            mainController.logout();
        } catch (RemoteException ex) {
            Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void onCreatePressed(ActionEvent event) {
        // Open a detail editor dialog
        mainController.createDetailEditor(new Stage(), new ShaderDemo(), rootPane, (demo)->{
            // When done
            
            // Create a new demo using FancyShaderRemoteService
            ShaderDemo createdDemo = mainController.getService().create(mainController.getSessionProperty().getValue(), demo);
            
            // Trigger a refresh
            mainController.refresh();
            
            // Open an editor window for the new demo
            mainController.createEditor(new Stage(), createdDemo, rootPane);
        });
    }

    @FXML
    public void onForkPressed(ActionEvent event) {
        // Get the selected value which is guaranteed to be non-null
        // As the fork menu button would not be enabled otherwise
        ShaderDemo selected = selectedItemProperty.getValue();
        
        String code = selected.getCode();
        
        mainController.createDetailEditor(new Stage(), selected, rootPane, (demo)->{
            // When done
            
            // Set the code of the demo
            demo.setCode(code);
            
            // Create a new demo using the FancyShaderRemoteService
            ShaderDemo forkedDemo = mainController.getService().create(mainController.getSessionProperty().getValue(), demo);
            
            // Trigger a refresh
            mainController.refresh();
            
            // Open an editor window for the new demo
            mainController.createEditor(new Stage(), forkedDemo, rootPane);
        });
    }
    
    @FXML
    public void onInfoPressed(ActionEvent e) {
        // Get the selected value which is guaranteed to be non-null
        // As the info menu button would not be enabled otherwise
        ShaderDemo selected = selectedItemProperty.getValue();
        
        // Open a detail viewer dialog for the selected demo
        mainController.createDetailViewer(new Stage(), selected, viewer);
    }
    
    @FXML
    public void onDelightfulPressed(ActionEvent e) {
        try {
            // Get the selected value which is guaranteed to be non-null
            // As the delightful menu button would not be enabled otherwise
            ShaderDemo selected = selectedItemProperty.getValue();
            
            String sessionUUID = mainController.getSessionProperty().getValue();
            
            // Toggle the delightful status using the FancyShaderRemoteService
            mainController.getService().delightful(sessionUUID, selected);
            
            // The text of the delightful menu item has changed as it had toggled
            // so the menu items need to be updated
            updateMenuItems();
        } catch (FancyShaderSessionExpiredException | RemoteException ex) {
            Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void onEditPressed(ActionEvent event) {
        // Open an editor window for the selected item which is guaranteed to be non-null
        // As the edit menu button would not be enabled otherwise
        mainController.createEditor(new Stage(), selectedItemProperty.getValue(), rootPane);
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
    
    @FXML
    public void onAboutPressed(ActionEvent event) {
        // Open the about page in the default browser
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("\"file://"+Helper.getUserPath().getPath().replace(" ", "%20")+"/resources/about.html\""));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }
    
    @FXML
    public void onHelpContentsPressed(ActionEvent event) {
        // Open the help page in the default browser
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("file://"+Helper.getUserPath().getPath().replace(" ", "%20")+"/resources/browser.html"));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        
    }
    
    
    
    public void updateMenuItems() {
        boolean loggedIn = mainController != null && mainController.getSessionProperty().getValue()!=null;
        boolean selectedItem = loggedIn && selectedItemProperty != null && selectedItemProperty.getValue() != null;
        String sessionUUID = mainController.getSessionProperty().getValue();
        
        // Determine whether the user is the creator of the selected item using FancyShaderRemoteService
        boolean ownerSelectedItem = false;
        try {
            ownerSelectedItem = selectedItem && mainController.getService().getAccount(sessionUUID).equals(selectedItemProperty.getValue().getCreator());
        } catch (RemoteException | FancyShaderSessionExpiredException e) {
            Logger.getLogger(BrowserController.class.getName()).log(Level.WARNING, null, e);
        }
        
        // Determine whether the user is delighted by the selected item using FancyShaderRemoteService
        boolean delightedSelectedItem = false;
        try {
            delightedSelectedItem = selectedItem && mainController.getService().isDelighted(sessionUUID, selectedItemProperty.getValue());
        } catch (FancyShaderSessionExpiredException | RemoteException ex) {
            Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loginMenuItem.setDisable(loggedIn);
        registerMenuItem.setDisable(loggedIn);
        logoutMenuItem.setDisable(!loggedIn);
        createMenuItem.setDisable(!loggedIn);
        playMenuItem.setDisable(viewer.getDemoAnimator().isPlaying());
        pauseMenuItem.setDisable(!viewer.getDemoAnimator().isPlaying());
        editMenuItem.setDisable(!ownerSelectedItem);
        forkMenuItem.setDisable(!selectedItem);
        infoMenuItem.setDisable(!selectedItem);
        delightfulMenuItem.setDisable(!selectedItem);
        delightfulMenuItem.setText(delightedSelectedItem ? "Undelightful" : "Delightful");
        
    }

    
}
