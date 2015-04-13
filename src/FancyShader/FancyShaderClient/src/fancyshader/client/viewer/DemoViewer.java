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
package fancyshader.client.viewer;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;
import javax.swing.JComponent;

/**
 * The viewer for demos. The fragment shader source code is set through the {@link DemoViewer#setCode(String)} and the controls for the animation
 * are obtained through {@link DemoViewer#getDemoAnimator()}.
 * @author Reuben Steenekamp
 */
public class DemoViewer extends AnchorPane{
    private DemoRunner runner;
    private SwingNode node;
    private DemoAnimator animator;
    private boolean resetOnChange = false;

    public DemoViewer() {
        super();
        node = new SwingNode();
        AnchorPane.setLeftAnchor(node, 0.);
        AnchorPane.setRightAnchor(node, 0.);
        AnchorPane.setTopAnchor(node, 0.);
        AnchorPane.setBottomAnchor(node, 0.);
        getChildren().add(node);
        
        animator = new DemoAnimator();
        
        runner = new DemoRunner();
        animator.setDemoRunner(runner);
        
        JComponent panel = runner.getGLJPanel();
        node.setContent(panel);
        
        setCode(null);
    }
    
    /**
     * Set whether to call {@link DemoAnimator#reset()} on the viewer's {@link DemoAnimator} when {@link DemoViewer#setCode(String)} is called.
     * It is off by default.
     * @param resetOnChange whether to reset on change.
     */
    public void setResetOnChange(boolean resetOnChange) {
        this.resetOnChange = resetOnChange;
    }
    
    /**
     * Set the source code to use for the fragment shader
     * @param code the source code
     */
    public void setCode(String code) {
        if(code == null) code = "";
        if(resetOnChange)
            animator.reset();
        runner.setFragCode(code);
        runner.getGLJPanel().repaint();
    }
    
    /**
     * Get the demo animator that controls the animation of the viewer.
     * @return the demo animator
     */
    public DemoAnimator getDemoAnimator() {
        return animator;
    }
}
