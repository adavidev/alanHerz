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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import util.GLUtils;
import util.Matrix4f;
import util.Vector3f;
import util.Vector4f;

/**
 * @author Shannon Smith
 */
public class Model {

    private final String name;
    private final Map<String,ModelSection> sections;
    private final Map<String,KeyFrameAnimation> animations;
    private String skinName; 
    private ModelSection rootSection;
    
    public Model(String name){
        this.name = name;
        sections = new HashMap<String,ModelSection>();
        animations = new HashMap<String,KeyFrameAnimation>();
    }
    
    public String getName(){
        return name;
    }
    
    public String getSkinName(){
    	return skinName;
    }
    
    public void setSkinName(String skinName){
    	this.skinName = skinName;
    }
    
    private void transformToSection(String name, ModelState state){
        LinkedList selectionPath = getSelectionPath(name);
        while (!selectionPath.isEmpty()){
            ModelSection curSection = (ModelSection)selectionPath.removeLast();
            curSection.getConnector().transformGL((float)(state.getAngle(curSection.getName())*180.0f/Math.PI));
        } 
    }
    
    public Connector getTransformedConnector(String name, ModelState state){
        Connector con = getSection(name).getConnector();
        transformToSection(name, state);
        Matrix4f m = GLUtils.getModelViewMatrix();
        Vector4f pos = new Vector4f(con.getPosition().x,con.getPosition().y,con.getPosition().z,1);
        Vector4f axis = new Vector4f(con.getAxis().x + pos.x,con.getAxis().y + pos.y,con.getAxis().z + pos.z,1);
        m.transform(pos);
        m.transform(axis);
        Vector3f scaledAxis = new Vector3f(axis.x - pos.x,axis.y - pos.y,axis.z - pos.z);
        scaledAxis.normalise();
        Connector c = new Connector(new Vector3f(pos.x,pos.y,pos.z),scaledAxis);
        return c;
    }
    
    public LinkedList getSelectionPath(String name){
        LinkedList<ModelSection> sectionPath = new LinkedList<ModelSection>();
        ModelSection section = getSection(name);
        while (section.getParent() != null){
            sectionPath.addLast(section);
            section = section.getParent();
        }
        sectionPath.addLast(section);
        return sectionPath;
    }
    
    public Collection<ModelSection> getSections(){
        return sections.values();
    }
    
    public Collection<KeyFrameAnimation> getAnimations(){
        return animations.values();
    }
    
    
    public void addSection(ModelSection section, ModelSection parent){
    	if (parent == null){
    		rootSection = section;
    	} else {
    		parent.addChild(section);
    	}
        sections.put(section.getName(),section);
    }
    
    public void addAnimation(KeyFrameAnimation ani){
        animations.put(ani.getName(),ani);
    }
    
    public ModelSection getSection(String name){
        return sections.get(name);
    }
    
    public KeyFrameAnimation getAnimation(String name){
        return animations.get(name);
    }
    
    public ModelSection getRootSection(){
        return rootSection;
    }

    public void draw(ModelState state){
    	if (rootSection != null){
    		rootSection.draw(state);
    	}
    }
    
    

}
