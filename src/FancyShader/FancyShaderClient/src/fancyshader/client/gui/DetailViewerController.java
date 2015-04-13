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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller for the DetailViewer GUI
 * @author Reuben Steenekamp
 */
public class DetailViewerController {
    private ShaderDemo shaderDemo;
    
    @FXML Pane rootPane;
    @FXML TextField titleField;
    @FXML TextField tagField;
    @FXML TextField creatorField;
    @FXML TextField dateCreatedField;
    @FXML Label infoLabel;
    
    /**
     * Set the shader demo to use
     * @param shaderDemo the shader demo to use
     */
    public void setShaderDemo(ShaderDemo shaderDemo) {
        this.shaderDemo = shaderDemo;
        initFields();
    }

    private void initFields() {
        titleField.setText(shaderDemo.getTitle());
        tagField.setText(Helper.toDelimitedTags(shaderDemo.getTags()));
        creatorField.setText(shaderDemo.getCreator().getUsername());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateCreatedField.setText(formatter.format(shaderDemo.getDateCreated()));
        infoLabel.setText("Visited: "+(shaderDemo.getVisitors()==null?0:shaderDemo.getVisitors().size())+"\tDelighted: "+(shaderDemo.getVisitors()==null?0:shaderDemo.getDelighted().size()));
    }
    
    @FXML
    public void onClosePressed(ActionEvent e) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }
}
