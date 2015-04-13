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
import fancyshader.service.FancyShaderLoginException;
import fancyshader.service.FancyShaderRemoteService;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller responsible for providing the {@link fancyshader.service.FancyShaderRemoteService}, 
 * helper methods to create new windows, managing the user session and providing a refresh function 
 * and callback.
 * @author Reuben Steenekamp
 */
public final class MainController {

    private Property<String> sessionProperty;
    private FancyShaderRemoteService service;
    private Runnable refreshCallback;

    public MainController(FancyShaderRemoteService service) {
        this.service = service;
        this.sessionProperty = new SimpleObjectProperty<>(null);
        sessionProperty.addListener((observable, oldValue, newValue) -> {
            refresh();
        });
    }
    
    /**
     * Set the refresh callback
     * @param refreshCallback the callback to set
     */
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    /**
     * Create a new browser window
     * @param stage the stage to use
     * @param node the parent
     */
    public void createBrowser(Stage stage, Node node) {
        try {
            URL url = getClass().getResource("Browser.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            BrowserController browserController = loader.getController();
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(true);
            stage.setTitle("FancyBrowser");
            
            browserController.setMainController(this);
            
            if(node!=null) {
                stage.initOwner(node.getScene().getWindow());
            }
            
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new editor window
     * @param stage the stage to use
     * @param demo the demo to edit
     * @param node the parent
     */
    public void createEditor(Stage stage, ShaderDemo demo, Node node) {
        try {
            URL url = getClass().getResource("Editor.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            EditorController editorController = loader.getController();
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(true);
            stage.setTitle("FancyEditor");
            
            editorController.setMainController(this);
            editorController.setShaderDemo(demo);
            
            if(node!=null) {
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new exit confirmation dialog
     * @param stage the stage to use
     * @param node the parent
     * @param callback the callback upon save/exit
     */
    public void createConfirmExit(Stage stage, Node node, ConsumerWithException<ConfirmExitController.Result> callback) {
        try {
            URL url = getClass().getResource("ConfirmExit.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            ConfirmExitController confirmExitController = loader.getController();
            
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("FancyConfirmExit");
            
            confirmExitController.setCallback(callback);
            
            if(node!=null) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new detail editor dialog
     * @param stage the stage to use
     * @param demo the demo to edit details of
     * @param node the parent
     * @param callback the callback upon completion of the dialog
     */
    public void createDetailEditor(Stage stage, ShaderDemo demo, Node node, ConsumerWithException<ShaderDemo> callback) {
        try {
            URL url = getClass().getResource("DetailEditor.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            DetailEditorController detailEditorController = loader.getController();
            detailEditorController.setCallback(callback);
            
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("FancyDetailEditor");
            
            
            detailEditorController.setMainController(this);
            detailEditorController.setShaderDemo(demo);
            
            if(node!=null) {
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a detail viewer window
     * @param stage the stage to use
     * @param demo the demo to view details of
     * @param node the parent
     */
    public void createDetailViewer(Stage stage, ShaderDemo demo, Node node) {
        try {
            URL url = getClass().getResource("DetailViewer.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            DetailViewerController detailViewerController = loader.getController();
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("FancyDetailViewer");
            
            
            detailViewerController.setShaderDemo(demo);
            
            if(node!=null) {
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a login dialog
     * @param stage the stage to use
     * @param node the parent
     */
    public void createLogin(Stage stage, Node node) {
        try {
            URL url = getClass().getResource("Login.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            LoginController loginController = loader.getController();
            
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("FancyLogin");
            
            loginController.setMainController(this);
            
            if(node!=null) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new register dialog
     * @param stage the stage to use
     * @param node the parent
     */
    public void createRegister(Stage stage, Node node) {
        try {
            URL url = getClass().getResource("Register.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            RegisterController registerController = loader.getController();
            
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("FancyRegister");
            
            registerController.setMainController(this);
            
            if(node!=null) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(node.getScene().getWindow());
            }
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a splash screen
     * @param stage the stage to use
     */
    public static void createSplash(Stage stage) {
        try {
            URL url = MainController.class.getResource("Splash.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            
            stage.setScene(scene);
            stage.getIcons().add(new Image(MainController.class.getResource("FancyShaderIcon.png").toExternalForm()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Trigger the refresh callback
     */
    public void refresh() {
        refreshCallback.run();
    }
    
    /**
     * Login using the {@link FancyShaderRemoteService} and set the sessionProperty to the created session
     * @param username
     * @param password
     * @throws FancyShaderLoginException
     * @throws FancyShaderInternalException
     * @throws RemoteException 
     */
    public void login(String username, String password) throws FancyShaderLoginException, FancyShaderInternalException, RemoteException {
        String uuid = getService().login(username, password);
        getSessionProperty().setValue(uuid);
    }
    
    /**
     * Logout using the {@link FancyShaderRemoteService} and set the sessionProperty to null
     * @throws RemoteException 
     */
    public void logout() throws RemoteException {
        getService().logout(getSessionProperty().getValue());
        getSessionProperty().setValue(null);
    }

    /**
     * @return the sessionProperty
     */
    public Property<String> getSessionProperty() {
        return sessionProperty;
    }

    /**
     * @return the service
     */
    public FancyShaderRemoteService getService() {
        return service;
    }
}
