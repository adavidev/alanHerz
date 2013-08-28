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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Model;
import model.ModelSection;

import org.jdom.input.SAXBuilder;

import util.Vector3f;

public class ModifySectionDialog extends JDialog implements ChangeListener, ActionListener {

    private ModelSection section;
    private VectorControl connectPos = new VectorControl(this);
    private VectorControl connectAxis = new VectorControl(this);
    private JButton close = new JButton("Close");
//    private JComboBox rootSection = new JComboBox();
    private Model model;
    private AnimationEditor parent;
    private JFileChooser fileChooser = new JFileChooser();
    private SAXBuilder xmlLoader = new SAXBuilder();
    
    public ModifySectionDialog(Model model, AnimationEditor parent){
        super(parent,false);
        this.setSize(192,256);
        
        this.parent = parent;
        this.model = model;
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.PAGE_AXIS));
        
//        this.getContentPane().add(rootSection);
        this.getContentPane().add(connectPos);
        this.getContentPane().add(connectAxis);
        this.getContentPane().add(close);
        Iterator sections = model.getSections().iterator();
//        rootSection.addItem("null");
//        while (sections.hasNext()){
//            ModelSection section = (ModelSection)sections.next();
//            rootSection.addItem(section.getName());
//        }
        
        close.addActionListener(this);
    }
    
    public float getConnectPosX(){
        return (float)connectPos.getValueX();
    }
    
    public float getConnectPosY(){
        return (float)connectPos.getValueY();
    }

    public float getConnectPosZ(){
        return (float)connectPos.getValueZ();
    }

    public float getConnectAxisX(){
        return (float)connectAxis.getValueX();
    }
    
    public float getConnectAxisY(){
        return (float)connectAxis.getValueY();
    }

    public float getConnectAxisZ(){
        return (float)connectAxis.getValueZ();
    }

    
    public void setSection(ModelSection section){
        this.section = section;
        Vector3f conPoint = section.getConnector().getPosition();
        Vector3f conAxis = section.getConnector().getAxis();
        connectPos.setValue(conPoint.x,conPoint.y,conPoint.z);
        connectAxis.setValue(conAxis.x,conAxis.y,conAxis.z);
//        if (section.getParent() != null){
//            rootSection.setSelectedItem(section.getParent().getName());
//        } else {
//            rootSection.setSelectedItem(null);
//        }
    }

    
    
    public void stateChanged(ChangeEvent arg0) {
        parent.sectionChanged(section);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == close){
            this.setVisible(false);
        }
    }

}
