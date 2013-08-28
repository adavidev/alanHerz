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
package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Shannon Smith
 */
public class ModelState {

    private final Map<String,ModelJoint> joints;
    
    public ModelState(){
        joints = new HashMap<String,ModelJoint>();
    }
    
    public ModelState(Model model){
        joints = new HashMap<String,ModelJoint>();
        for(ModelSection section : model.getSections()){
            ModelJoint joint = new ModelJoint(section.getName(),0.0f);
            joints.put(joint.getSection(),joint);
        }
    }
    
    public ModelJoint getJoint(String section, boolean add){
        ModelJoint joint = (ModelJoint)joints.get(section);
        if(joint == null && add){
            joint = new ModelJoint(section,0.0f);
            joints.put(section, joint);
        }
        return joint;
    }
    

    public Iterator getJoints(){
        return joints.values().iterator();
    }
    
    public void addJoint(ModelJoint joint){
        joints.put(joint.getSection(), joint);
    }
    
    public float getAngle(String section){
        if (joints.get(section) == null){
            System.out.println("No such joint: " + section);
        }
        return ((ModelJoint)joints.get(section)).getAngle();
    }
    
    public void setAngle(String section, float angle){
        if (!joints.containsKey(section)){
            joints.put(section,new ModelJoint(section,angle));
        }
        ((ModelJoint)joints.get(section)).setAngle(angle);
    }
    
    public boolean hasJoint(String name){
        return joints.containsKey(name);
    }

    
}
