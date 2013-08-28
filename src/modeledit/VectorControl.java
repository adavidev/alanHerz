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

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Shannon Smith
 */
public class VectorControl extends JPanel implements ChangeListener {

    private JSpinner x = new JSpinner(new SpinnerNumberModel(0.0,-1.0,1.0,0.01));
    private JSpinner y = new JSpinner(new SpinnerNumberModel(0.0,-1.0,1.0,0.01));
    private JSpinner z = new JSpinner(new SpinnerNumberModel(0.0,-1.0,1.0,0.01));
    
    private boolean ignoreChange;
    
    private ChangeListener parent;
    
    public VectorControl(ChangeListener parent){
        super(new FlowLayout());
        this.parent = parent;
        this.add(x);
        this.add(y);
        this.add(z);
        x.addChangeListener(this);
        y.addChangeListener(this);
        z.addChangeListener(this);
        x.setPreferredSize(new Dimension(48,20));
        y.setPreferredSize(new Dimension(48,20));
        z.setPreferredSize(new Dimension(48,20));
        ignoreChange = false;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent arg0) {
        if (!ignoreChange){
            parent.stateChanged(new ChangeEvent(this));
        }
    }
    
    public void setValue(float x, float y, float z){
        ignoreChange = true;
        this.x.setValue(new Double(x));
        this.y.setValue(new Double(y));
        this.z.setValue(new Double(z));
        ignoreChange = false;
    }
    
   public double getValueX() {
       return ((Double)x.getValue()).doubleValue();
   }
    
   public double getValueY() {
       return ((Double)y.getValue()).doubleValue();
   }

   public double getValueZ() {
       return ((Double)z.getValue()).doubleValue();
   }

   
}
