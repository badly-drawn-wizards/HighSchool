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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

/**
 * Renders a shader program onto a plane occupying the screen with the given fragment shader
 * @author Reuben Steenekamp
 */
public class DemoRunner implements GLEventListener{
    
    private final String VERT_CODE = "void main(){ gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; }";
    private final String ERROR_CODE = "uniform float time; uniform vec2 resolution; void main() { vec2 pos = gl_FragCoord.xy/resolution-.5; pos*=10.; float mul = .5*(1.+sin(length(pos)-time/1000.)); gl_FragColor = mul*vec4(1,0,0,0); }";
    private String fragCode = null;
    private boolean shadersReady = false;
    private GLJPanel panel;
    private GLProfile profile;
    private GL2 gl;
    private boolean error;
    private String vertLog, fragLog, programLog;
    private int timeLocation, resolutionLocation;
    private DemoAnimator animator;
    
    public DemoRunner() {
        profile = GLProfile.get(GLProfile.GL2);
        panel = new GLJPanel(new GLCapabilities(profile));
        addEventListeners();
    }
    
    private void addEventListeners() {
        panel.addGLEventListener(this);
    }
    

    private int program = -1;

    private boolean getLinkAndValidateStatus(int program) {
        IntBuffer status = IntBuffer.allocate(1);
        gl.glGetProgramiv(program, GL2.GL_LINK_STATUS, status);
        if(status.get() == GL2.GL_FALSE) return false;
        status.rewind();
        gl.glGetProgramiv(program, GL2.GL_VALIDATE_STATUS, status);
        if(status.get() == GL2.GL_FALSE) return false;
        return true;
    }
    private String getInfoLog(int object, boolean program) {
        IntBuffer logLength = IntBuffer.allocate(1);
        if(program)
            gl.glGetProgramiv(object, GL2.GL_INFO_LOG_LENGTH, logLength);
        else
            gl.glGetShaderiv(object, GL2.GL_INFO_LOG_LENGTH, logLength);
        ByteBuffer logBytes = ByteBuffer.allocate(logLength.get(0));
        gl.glGetInfoLogARB(object, logBytes.capacity(), null, logBytes);
        String log = Charset.forName("US-ASCII").decode(logBytes).toString();
        return log.trim();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = panel.getGL().getGL2();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        gl.glDeleteProgram(program);
    }

    private void initShaders(GL2 gl, String code) {
        program = gl.glCreateProgramObjectARB();
        if(program<1) throw new RuntimeException("Shader program could not be created");
        
        int vertShader = gl.glCreateShaderObjectARB(GL2.GL_VERTEX_SHADER),
            fragShader = gl.glCreateShaderObjectARB(GL2.GL_FRAGMENT_SHADER);
        
        
        gl.glShaderSourceARB(vertShader, 1, new String[]{VERT_CODE}, null);
        gl.glCompileShader(vertShader);
        gl.glAttachObjectARB(program, vertShader);
        
        gl.glShaderSourceARB(fragShader, 1, new String[]{code}, null);
        gl.glCompileShader(fragShader);
        gl.glAttachObjectARB(program, fragShader);
        
        gl.glLinkProgramARB(program);
        
        vertLog = getInfoLog(vertShader, false);
        fragLog = getInfoLog(fragShader, false); 
        programLog = getInfoLog(program, true);
        error = !getLinkAndValidateStatus(program);
        if(error || fragLog.length() > 0 || programLog.length() > 0) {
            gl.glDeleteProgram(program);
            gl.glDeleteObjectARB(vertShader);
            gl.glDeleteObjectARB(fragShader);
            if(code == (Object)ERROR_CODE)
                program = 0;
            else
                initShaders(gl, ERROR_CODE);
            return;
        }
        
        timeLocation = gl.glGetUniformLocationARB(program, "time");
        resolutionLocation = gl.glGetUniformLocationARB(program, "resolution");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        
        if(!shadersReady) {
            if(fragCode == null)
                return;
            initShaders(gl, fragCode);
            shadersReady = true;
        }
        
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -1); // Not certain if necesary
        
        
        gl.glUseProgramObjectARB(program);
        if(timeLocation > -1)
            gl.glUniform1f(timeLocation, animator.getElapsedTime());
        if(resolutionLocation > -1)
            gl.glUniform2f(resolutionLocation, drawable.getWidth(), drawable.getHeight());
        
        
        gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex3i(-1,-1, 0);
            gl.glVertex3i( 1,-1, 0);
            gl.glVertex3i( 1, 1, 0);
            
            gl.glVertex3i( 1, 1, 0);
            gl.glVertex3i( -1,-1, 0);
            gl.glVertex3i(-1, 1, 0);
        gl.glEnd();
        
        gl.glUseProgramObjectARB(0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

    /**
     * Get the GLJPanel that the Runner renders on.
     * @return the GLJPanel
     */
    public GLJPanel getGLJPanel() {
        return panel;
    }

    /**
     * Set the fragment shader source code to be compiled into the shader program used to render on the GLJPanel
     * @param fragCode the fragment shader source code to be set
     */
    public void setFragCode(String fragCode) {
        this.fragCode = fragCode;
        this.shadersReady = false;
    }
    
    /**
     * Set the animator from which the elapsed time can be retrieved
     * @param animator the {@link DemoAnimator} to set
     */
    public void setDemoAnimator(DemoAnimator animator) {
        this.animator = animator;
    }
    
}
