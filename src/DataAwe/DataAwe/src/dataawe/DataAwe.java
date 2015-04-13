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
package dataawe;

import dataawe.gui.MainFrame;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Reuben Steenekamp
 */
public class DataAwe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        JFrame mainFrame = new MainFrame();
        mainFrame.setTitle("Data Aware Task");
        mainFrame.setSize(500, 400);
        mainFrame.setVisible(true);
    }

}
