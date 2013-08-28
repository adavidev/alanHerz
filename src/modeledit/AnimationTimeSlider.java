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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.KeyFrame;
import model.KeyFrameAnimation;

/**
 * @author Shannon Smith
 */
public class AnimationTimeSlider extends JPanel implements ChangeListener {

    private static final Color DARK_RED = new Color(128,0,0);
    private static final Color DARK_BLUE = new Color(0,0,128);

    private KeyFrameAnimation ani;
    private JSlider position;
    private AnimationEditor parent;
    private KeyFrameBar select = new KeyFrameBar(this);
    
    public AnimationTimeSlider(AnimationEditor parent){
        this.setLayout(new BorderLayout());
        this.parent = parent;
        position = new JSlider();
        this.add(position, BorderLayout.NORTH);
        this.add(select, BorderLayout.CENTER);
        position.addChangeListener(this);
        position.setPaintLabels(true);
        position.setPaintTicks(true);
    }
    
    
    public float getTime(){
        return position.getValue()/1000.0f;
    }
    
    public void setTime(float time){
        position.setValue((int)(time*1000.0f));
        select.setTime(time);
    }
    
    public void setAnimation(KeyFrameAnimation ani){
        this.ani = ani;
        select.setAnimation(ani);
        position.setMaximum((int)(ani.getLength()*1000.0f));
        position.setMajorTickSpacing(100);
        position.setMinorTickSpacing(10);
        position.setSnapToTicks(true);
        
        
        
    }
    
    public void stateChanged(ChangeEvent arg0) {
        if (ani != null){
            parent.setAniPos(position.getValue()/1000.0f);
            select.setTime(position.getValue()/1000.0f);
        }
        
    }
    
    private class KeyFrameBar extends JPanel implements MouseListener {
        
        
        /**
         * 
         */
        private KeyFrameAnimation ani;
        private float time;
        private AnimationTimeSlider parent;
        
        public KeyFrameBar(AnimationTimeSlider parent){
            this.parent = parent;
            this.addMouseListener(this);
            this.setBorder(BorderFactory.createEmptyBorder(2,13,2,13));
            
        }
        
        public void setTime(float time){
            this.time = time;
            this.repaint();
        }
        
        public void setAnimation(KeyFrameAnimation ani){
            this.ani = ani;
        }
 
        public void paint(Graphics g){
            super.paint(g);
            Insets insets = this.getBorder().getBorderInsets(this);
            int width = this.getWidth() - (insets.left + insets.right);
            int height = this.getHeight() - (insets.top + insets.bottom);
            if (ani != null){
                KeyFrame selectedKey = ani.getKeyFrame(time, AnimationEditor.KEY_FRAME_SNAP);
                Iterator i = ani.getKeyFrames();
                while (i.hasNext()){
                    KeyFrame keyFrame = (KeyFrame)i.next();
                    
                    if (keyFrame == selectedKey){
                        g.setColor(DARK_RED);
                    } else {
                        g.setColor(DARK_BLUE);
                    }
                    g.fillRect((int)((keyFrame.getTime()/ani.getLength())*width) + insets.left-2,insets.top,4,height);

                    if (keyFrame == selectedKey){
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.BLUE);
                    }

                    g.drawRect((int)((keyFrame.getTime()/ani.getLength())*width) + insets.left-2,insets.top,4,height);
                }
            }
        }

        public void mouseClicked(MouseEvent arg0) {
            Insets insets = this.getBorder().getBorderInsets(this);
            float width = this.getWidth() - (insets.left + insets.right);
            time = (((float)arg0.getX() - insets.left)/width)*ani.getLength();
            parent.setTime(time);
        }

        public void mousePressed(MouseEvent arg0) {
        }

        public void mouseReleased(MouseEvent arg0) {
        }

        public void mouseEntered(MouseEvent arg0) {
        }

        public void mouseExited(MouseEvent arg0) {
        }
        
    }
    

}
