/*
 *	 Herzog3D - 3D Real Time Strategy game.
 *   Copyright (C) 2005  Shannon Smith
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package modeledit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.ModelState;
import model.KeyFrameAnimation;

/**
 * @author Shannon Smith
 */
public class AnimationControl extends JPanel implements ActionListener {

    private KeyFrameAnimation ani;
    private JButton play = new JButton(">");
    private JButton stop = new JButton("||");
    private JPanel controls = new JPanel();
    private AnimationTimeSlider slider;
    private ModelState state;
    private Timer aniTimer = new Timer(10,this);
    private AnimationEditor editor;
    private long lastTime;
    
    
    public AnimationControl(AnimationEditor editor){
        super(new BorderLayout());
        slider = new AnimationTimeSlider(editor);
        controls.add(play);
        controls.add(stop);
        this.add(controls,BorderLayout.WEST);
        
        this.add(slider,BorderLayout.CENTER);
        play.addActionListener(this);
        stop.addActionListener(this);
        this.editor = editor;
    }
    
    public KeyFrameAnimation getAnimation(){
        return ani;
    }
    
    public void setAnimation(KeyFrameAnimation ani){
        this.ani = ani;
        slider.setAnimation(ani);
    }

    public void step(){
        float curTime = slider.getTime();
        curTime += (System.currentTimeMillis() - lastTime)/1000.0f;
        while (curTime > ani.getLength() ){
            curTime -= ani.getLength();
        }
        slider.setTime(curTime);
        lastTime = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == aniTimer){
            step();
        } else if (ae.getSource() == play) {
            aniTimer.start();
            lastTime = System.currentTimeMillis();
        } else if (ae.getSource() == stop) {
            aniTimer.stop();
            
        }
    }
    
    
}
