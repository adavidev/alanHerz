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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

/**
 * @author Shannon Smith
 */
public class ModelSection {
	
    private String name;
    private ModelSection parent;
    private Connector connector;
    private List<ModelSection> children;
    private Mesh mesh;
    
    public ModelSection(String name){
        this.name = name;
        children = new ArrayList<ModelSection>();
    }
    
    public Mesh getMesh(){
    	return mesh;
    }
    
    public ModelSection getParent(){
        return parent;
    }
    
    public Connector getConnector(){
        return connector;
    }
    
    public Collection<ModelSection> getChildren(){
        return children;
    }
    
    public void setMesh(Mesh mesh){
    	this.mesh = mesh;
    }
    
    public void setParent(ModelSection parent){
        this.parent = parent;
    }
    
    protected void addChild(ModelSection child){
        children.add(child);
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setConnector(Connector connector){
        this.connector = connector;
    }
    
    public void draw(ModelState state){
        GL11.glPushMatrix();
        connector.transformGL((float)(state.getAngle(name)*180.0f/Math.PI));
        
        if (mesh != null){
        	mesh.draw();
        }
        for(int i = 0; i < children.size();i++){
            ((ModelSection)children.get(i)).draw(state);
        }
        GL11.glPopMatrix();
    }

    
    public String toString(){
        return "ModelSection: " + name;
    }
    
}
