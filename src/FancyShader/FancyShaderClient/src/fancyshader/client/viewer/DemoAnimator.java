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

import com.jogamp.opengl.util.Animator;

/**
 * Controls the animation of the demo. Allows playing, pausing and resetting.
 * @author Reuben Steenekamp
 */
public class DemoAnimator {
    private long previousElapsed;
    private DemoRunner runner;
    private Animator animator;
    private boolean firstSet = false;
    
    public DemoAnimator() {
        previousElapsed = 0;
        animator = new Animator();
        animator.setUpdateFPSFrames(1, null);
        animator.start();
    }
    
    /**
     * Set the {@link DemoRunner} to animate.
     * @param runner the {@link DemoRunner} to set
     */
    public void setDemoRunner(DemoRunner runner) {
        boolean wasPlaying = isPlaying();
        if(wasPlaying) pause();
        if(this.runner != null && !firstSet) {
            firstSet = true;
            animator.remove(this.runner.getGLJPanel());
            runner.setDemoAnimator(null);
        }
        
        this.runner = runner;
       
        if(runner != null) {
            runner.setDemoAnimator(this);
            animator.add(runner.getGLJPanel());
        }
        if(wasPlaying) play();
    }
    
    /**
     * Play/Resume the animator
     */
    public void play() {
        animator.resume();
    }
    
    /**
     * Pause the animator
     */
    public void pause() {
        bankTime();
        animator.pause();
    }
    
    /**
     * Reset the animator
     */
    public void reset() {
        bankTime();
        previousElapsed = 0;
        if(!animator.isAnimating()) {
            play();
            pause();
        }
    }
    
    /**
     * @return whether the animator is currently playing
     */
    public boolean isPlaying() {
        return !animator.isPaused();
    }
    
    /**
     * Get the total time elapsed while the animator is playing since the last reset
     * @return the time elapsed
     */
    public long getElapsedTime() {
        //bankTime();
        return previousElapsed+animator.getTotalFPSDuration();
    }
    
    private void bankTime() {
        previousElapsed += animator.getTotalFPSDuration();
        animator.resetFPSCounter();
    }
}